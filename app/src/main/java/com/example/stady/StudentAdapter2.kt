package com.example.stady

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StudentAdapter2(private val studentList: MutableList<Student>,
                      private val onDelete: (Student) -> Unit) : RecyclerView.Adapter<StudentAdapter2.StudentViewHolder>() {

    class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val studentName: TextView = itemView.findViewById(R.id.tv2_student_name)

        val deleteButton: Button = itemView.findViewById(R.id.btn_delete_student)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_student2, parent, false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = studentList[position]
        holder.studentName.text = "${student.name} - ${student.group}"

        holder.deleteButton.setOnClickListener {
            onDelete(student)
        }
    }

    override fun getItemCount(): Int = studentList.size

    fun updateStudents(newStudents: List<Student>) {
        studentList.clear()
        studentList.addAll(newStudents)
        notifyDataSetChanged()
    }
}
