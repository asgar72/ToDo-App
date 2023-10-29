package com.asgar72.todo.fragments.list

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.findNavController
import com.asgar72.todo.R

class ListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_list, container, false)
        //this is connect to add fragment
        view.findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.floatingActionButton)
            .setOnClickListener {
                findNavController().navigate(R.id.action_listFragment_to_addFragment)
            }

        view.findViewById<ConstraintLayout>(R.id.listLayout).setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_updateFragment)
        }

        //Set menu
        setHasOptionsMenu(true)
        return view

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_fragment_menu,menu)
    }
}