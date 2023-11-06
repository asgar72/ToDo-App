package com.asgar72.todo

import android.os.Bundle
import android.os.SystemClock
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.asgar72.todo.databinding.FragmentStopWatchBinding

class StopWatch : Fragment() {

    private var _binding: FragmentStopWatchBinding? = null
    private val binding get() = _binding!!

    private var isRunning = false
    private var elapsedTimeWhenStopped: Long = 0L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        //data binding
        _binding = FragmentStopWatchBinding.inflate(inflater, container, false)

        val lapList = ArrayList<String>()
        val arrayAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, lapList)

        binding.listView.adapter = arrayAdapter
        binding.lap.setOnClickListener {
            if (isRunning) {
                lapList.add(binding.chronometer.text.toString())
                arrayAdapter.notifyDataSetChanged()
            }
        }

        binding.restore.setOnClickListener {
            if (isRunning) {
                binding.chronometer.base = SystemClock.elapsedRealtime()
                binding.run.text = "RUN"
                isRunning = false
                binding.chronometer.stop()
                lapList.clear()
                arrayAdapter.notifyDataSetChanged()
            }
        }

        binding.run.setOnClickListener {
            if (!isRunning) {
                isRunning = true
                binding.chronometer.base = SystemClock.elapsedRealtime() - elapsedTimeWhenStopped
                binding.run.text = "STOP"
                binding.chronometer.start()
            } else {
                // Save the elapsed time when stopping
                elapsedTimeWhenStopped = SystemClock.elapsedRealtime() - binding.chronometer.base
                binding.run.text = "RUN"
                isRunning = false
                binding.chronometer.stop()
            }
        }
        return view
    }
}
