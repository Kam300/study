package com.example.stady
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class TeacherFragment : Fragment() {

    private lateinit var studentDao: StudentDao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        studentDao = AppDatabase.getDatabase(requireContext()).studentDao()

        val view = inflater.inflate(R.layout.fragment_teacher, container, false)

        view.findViewById<Button>(R.id.btn_add_student).setOnClickListener {
            findNavController().navigate(R.id.action_teacherFragment_to_addStudentFragment2)
        }

        view.findViewById<Button>(R.id.btn_view_attendance).setOnClickListener {
            findNavController().navigate(R.id.action_teacherFragment_to_viewAttendanceFragment2)
        }

        return view
    }
}
