package com.asgar72.todo.fragments.update

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.asgar72.todo.R
import com.asgar72.todo.data.models.ToDoData
import com.asgar72.todo.data.viewModel.ToDoViewModel
import com.asgar72.todo.databinding.FragmentUpdateBinding
import com.asgar72.todo.fragments.SharedViewModel
import java.text.SimpleDateFormat
import java.util.*

class UpdateFragment : Fragment() {

    private val args by navArgs<UpdateFragmentArgs>()
    private val mSharedViewModel: SharedViewModel by viewModels()
    private val mToDoViewModel: ToDoViewModel by viewModels()

    private var _binding: FragmentUpdateBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //Data binding
        _binding = FragmentUpdateBinding.inflate(inflater, container, false)
        binding.args = args

        //set menu
        setHasOptionsMenu(true)

        //Spinner Item Selected Listener
        binding.currentPrioritySpinner.onItemSelectedListener = mSharedViewModel.listener

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_save -> updateItem()
            R.id.menu_delete -> confirmItemRemoval()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateItem() {
        val title = binding.currentTitleEt.text.toString()
        val description = binding.currentDescriptionEt.text.toString()
        val priority = binding.currentPrioritySpinner.selectedItem.toString()
        val currentDateAndTime = SimpleDateFormat("dd-MM-yyyy HH:mm").format(Date())

        val validation = mSharedViewModel.verifyDataFromUser(title, description)
        if (validation) {
            //update current item
            val updatedItem = ToDoData(
                args.currentItem.id,
                title,
                mSharedViewModel.parsePriority(priority),
                description,
                currentDateAndTime
            )
            mToDoViewModel.updateData(updatedItem)
            Toast.makeText(requireContext(), "Successfully Updated!", Toast.LENGTH_SHORT).show()

            //navigate back and pop the back stack
            findNavController().navigate(
                R.id.action_updateFragment_to_listFragment,
                null,
                NavOptions.Builder()
                    .setPopUpTo(R.id.listFragment, true)
                    .build()
            )
        } else {
            Toast.makeText(requireContext(), "Please fill out all fields.", Toast.LENGTH_SHORT)
                .show()
        }
    }


    //Show alertdialog to confirm item removal
    private fun confirmItemRemoval() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            mToDoViewModel.deleteItem(args.currentItem)
            Toast.makeText(
                requireContext(),
                "Successfully Deleted: '${args.currentItem.title}'",
                Toast.LENGTH_SHORT
            ).show()
//            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
        builder.setNegativeButton("No") { _, _ -> }
        builder.setTitle("Delete '${args.currentItem.title}'?")
        builder.setMessage("Are you sure want to remove '${args.currentItem.title}'?")
        builder.create().show()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}