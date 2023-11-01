package com.asgar72.todo.fragments.update

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.asgar72.todo.R
import com.asgar72.todo.databinding.FragmentUpdateBinding

class UpdateFragment : Fragment() {


    private var _binding: FragmentUpdateBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        _binding = FragmentUpdateBinding.inflate(inflater, container, false)


        //set menu
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_fragment_menu,menu)
    }
}