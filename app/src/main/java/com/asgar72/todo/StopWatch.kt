package com.asgar72.todo

import android.os.Bundle
import android.os.SystemClock
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

class StopWatch : Fragment() {
    private var isRunning = false
    private var elapsedTimeWhenStopped: Long = 0L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_stop_watch, container, false)

        val lapList = ArrayList<String>()
        val arrayAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, lapList)
        val listView = view.findViewById<ListView>(R.id.listView)
        listView.adapter = arrayAdapter

        val chronometer = view.findViewById<Chronometer>(R.id.chronometer)
        val lapButton = view.findViewById<ImageView>(R.id.lap)
        val restoreButton = view.findViewById<ImageView>(R.id.restore)
        val runButton = view.findViewById<Button>(R.id.run)

        lapButton.setOnClickListener {
            if (isRunning) {
                lapList.add(chronometer.text.toString())
                arrayAdapter.notifyDataSetChanged()
            }
        }

        restoreButton.setOnClickListener {
            if (isRunning) {
                chronometer.base = SystemClock.elapsedRealtime()
                runButton.text = "RUN"
                isRunning = false
                chronometer.stop()
                lapList.clear()
                arrayAdapter.notifyDataSetChanged()
            }
        }

        runButton.setOnClickListener {
            if (!isRunning) {
                isRunning = true
                chronometer.base = SystemClock.elapsedRealtime() - elapsedTimeWhenStopped
                runButton.text = "STOP"
                chronometer.start()
            } else {
                // Save the elapsed time when stopping
                elapsedTimeWhenStopped = SystemClock.elapsedRealtime() - chronometer.base
                runButton.text = "RUN"
                isRunning = false
                chronometer.stop()
            }
        }
        return view
    }
}
