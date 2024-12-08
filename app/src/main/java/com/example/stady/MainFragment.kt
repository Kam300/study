package com.example.stady

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class MainFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        view.findViewById<Button>(R.id.btn_president).setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_presidentFragment)
        }

        view.findViewById<Button>(R.id.btn_teacher).setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_teacherFragment3)
        }

        return view
    }
}

