package com.asgar72.todo.fragments.list

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.asgar72.todo.R
import com.asgar72.todo.data.models.ToDoData
import com.asgar72.todo.data.viewModel.ToDoViewModel
import com.asgar72.todo.databinding.FragmentListBinding
import com.asgar72.todo.fragments.SharedViewModel
import com.asgar72.todo.fragments.list.adapter.ListAdapter
import com.asgar72.todo.fragments.list.adapter.SwipeToDelete
import com.google.android.material.snackbar.Snackbar
import jp.wasabeef.recyclerview.animators.LandingAnimator

class ListFragment : Fragment(), SearchView.OnQueryTextListener {

    private val mToDoViewModel: ToDoViewModel by viewModels()
    private val mSharedViewModel: SharedViewModel by viewModels()

    private val adapter: ListAdapter by lazy { ListAdapter() }

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private var isGridView = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Data binding
        _binding = FragmentListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.mSharedViewModel = mSharedViewModel

        // Setup recyclerview
        setupRecyclerview()

        // Observe LiveData
        mToDoViewModel.getAllData.observe(viewLifecycleOwner, Observer { data ->
            mSharedViewModel.checkIfDatabaseEmpty(data)
            val reversedData = data.reversed() // Display data in reversed order
            adapter.setData(reversedData)
        })

        // Set menu
        setHasOptionsMenu(true)

        return binding.root
    }

    private fun setupRecyclerview() {
        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter

        // Set initial layout manager
        toggleLayoutManager()

        // This is animation in recyclerview
        recyclerView.itemAnimator = LandingAnimator().apply {
            addDuration = 300
        }

        // Swipe to Delete
        swipeToDelete(recyclerView)
    }

    private fun swipeToDelete(recyclerView: RecyclerView) {
        val swipeToDeleteCallback = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedItem = adapter.dataList[viewHolder.adapterPosition]

                // Delete item
                mToDoViewModel.deleteItem(deletedItem)
                adapter.notifyItemChanged(viewHolder.adapterPosition)

                // Restore deleted data
                restoreDeletedData(viewHolder.itemView, deletedItem, viewHolder.adapterPosition)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun restoreDeletedData(view: View, deleteItem: ToDoData, position: Int) {
        val snackBar = Snackbar.make(
            view, "Deleted '${deleteItem.title}'", Snackbar.LENGTH_LONG
        )
        snackBar.setAction("Undo") {
            mToDoViewModel.insertData(deleteItem)
            adapter.notifyItemChanged(position)
        }
        snackBar.show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_fragment_menu, menu)

        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_delete_all -> confirmRemoval()
            R.id.menu_priority_high -> mToDoViewModel.sortByHighPriority.observe(this, Observer { adapter.setData(it) })
            R.id.menu_priority_low -> mToDoViewModel.sortByLowPriority.observe(this, Observer { adapter.setData(it) })
            R.id.menu_share -> shareMenu()
            R.id.menu_grid -> {
                toggleLayout()
                updateLayoutIcon(item)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // Toggle GridView
    private fun toggleLayout() {
        isGridView = !isGridView
        toggleLayoutManager()
    }

    private fun toggleLayoutManager() {
        val recyclerView = binding.recyclerView
        if (isGridView) {
            recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        } else {
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
        }
        adapter.notifyDataSetChanged()
    }

    private fun updateLayoutIcon(item: MenuItem) {
        if (isGridView) {
            item.setIcon(R.drawable.list_view)  // Use the icon for List View
        } else {
            item.setIcon(R.drawable.grid_view)  // Use the icon for Grid View
        }
    }

    // Share this App
    private fun shareMenu() {
        val packageName = requireContext().packageName
        val appLink = "https://play.google.com/store/apps/details?id=$packageName"
        val textToShare = "ToDo Timer - Click the link to download the App :\n $appLink"

        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, textToShare)
        }
        startActivity(Intent.createChooser(sendIntent, "Share ToDo App"))
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            searchThroughDatabase(query)
        }
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if (query != null) {
            searchThroughDatabase(query)
        }
        return true
    }

    private fun searchThroughDatabase(query: String) {
        val searchQuery = "%$query%"

        mToDoViewModel.searchDatabase(searchQuery).observe(this, Observer { list ->
            list?.let {
                adapter.setData(it)
            }
        })
    }

    // Show AlertDialog to Confirm Removal of All items from the database table
    private fun confirmRemoval() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            mToDoViewModel.deleteAll()
            Toast.makeText(
                requireContext(), "Successfully Deleted Everything..",
                Toast.LENGTH_SHORT
            ).show()
        }
        builder.setNegativeButton("No") { _, _ -> }
        builder.setTitle("Delete Everything?")
        builder.setMessage("Are you sure you want to remove everything?")
        builder.create().show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
