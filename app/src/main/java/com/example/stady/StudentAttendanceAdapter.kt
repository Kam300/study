package com.example.stady


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.stady.R
import com.example.stady.Student
class StudentAttendanceAdapter(
    private val students: List<StudentAttendance>,
    private val onAttendanceChange: (Student, Boolean) -> Unit
) : RecyclerView.Adapter<StudentAttendanceAdapter.StudentAttendanceViewHolder>() {

    class StudentAttendanceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val studentName: TextView = itemView.findViewById(R.id.tv_student_name)
        val attendanceCheckBox: CheckBox = itemView.findViewById(R.id.checkbox_attendance)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentAttendanceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_student_attendance, parent, false)
        return StudentAttendanceViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudentAttendanceViewHolder, position: Int) {
        val studentAttendance = students[position]
        holder.studentName.text = studentAttendance.student.name
        holder.attendanceCheckBox.isChecked = studentAttendance.isPresent

        holder.attendanceCheckBox.setOnCheckedChangeListener { _, isChecked ->
            onAttendanceChange(studentAttendance.student, isChecked)
        }
    }

    override fun getItemCount(): Int = students.size
}
