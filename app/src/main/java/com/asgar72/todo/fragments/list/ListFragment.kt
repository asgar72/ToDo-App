package com.asgar72.todo.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.LinearLayout
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isInvisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.asgar72.todo.R
import com.asgar72.todo.data.ToDoDatabase
import com.asgar72.todo.data.viewModel.ToDoViewModel
import com.asgar72.todo.databinding.FragmentListBinding
import com.asgar72.todo.fragments.SharedViewModel

class ListFragment : Fragment() {

    private val mToDoViewModel: ToDoViewModel by viewModels()

    private val  mSharedViewModel: SharedViewModel by viewModels()

    private val adapter: ListAdapter by lazy { ListAdapter() }

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentListBinding.inflate(inflater, container, false)

        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        mToDoViewModel.getAllData.observe(viewLifecycleOwner, Observer {data ->
            mSharedViewModel.checkIfDatabaseEmpty(data)
             adapter.setData(data)
        })

        //for check database is empty or not
        mSharedViewModel.emptyDatabase.observe(viewLifecycleOwner, Observer {
            showEmptyDatabaseViews(it)
        })


        //this is connect to add fragment
        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }

        //Set menu
        setHasOptionsMenu(true)

        return binding.root
    }

    private fun showEmptyDatabaseViews(emptyDatabase: Boolean) {
            if(emptyDatabase){
                binding.noDataImageview?.visibility = View.VISIBLE
            }else{
                binding.noDataImageview?.visibility = View.INVISIBLE
            }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_delete_all){
            confirmRemoval()
        }
        return super.onOptionsItemSelected(item)
    }

    //show AlertDialog to Confirm Removal of All item from database table
    private fun confirmRemoval() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){ _, _ ->
            mToDoViewModel.deleteAll()
            Toast.makeText(requireContext(),"Successfully Deleted Everything..",
                Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("No"){ _, _ ->}
        builder.setTitle("Delete Everything ?")
        builder.setMessage("Are you sure want to remove everything?")
        builder.create().show()
    }

}