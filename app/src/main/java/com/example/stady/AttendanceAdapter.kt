package com.example.stady


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class AttendanceAdapter(
    private val context: Context,
    private val attendanceList: List<StudentAttendance>
) : BaseAdapter() {

    override fun getCount(): Int {
        return attendanceList.size
    }

    override fun getItem(position: Int): Any {
        return attendanceList[position]
    }

    override fun getItemId(position: Int): Long {
        return attendanceList[position].student.id
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_2, parent, false)

        val studentName = view.findViewById<TextView>(android.R.id.text1)
        val attendanceStatus = view.findViewById<TextView>(android.R.id.text2)

        val studentAttendance = attendanceList[position]
        studentName.text = studentAttendance.student.name
        attendanceStatus.text = if (studentAttendance.isPresent) "Присуствует" else "Отсуствует или нет информации"

        return view
    }
}
