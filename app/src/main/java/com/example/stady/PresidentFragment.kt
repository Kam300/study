package com.example.stady

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class PresidentFragment : Fragment() {

    private lateinit var attendanceDao: AttendanceDao
    private lateinit var studentDao: StudentDao
    private lateinit var recyclerView: RecyclerView
    private lateinit var studentAttendanceAdapter: StudentAttendanceAdapter
    private val studentAttendanceList = mutableListOf<StudentAttendance>()

    private lateinit var groupSpinner: Spinner
    private lateinit var classNumberSpinner: Spinner

    private val groups = listOf("ИСпП-22-1", "Группа 2", "Группа 3") // Группы
    private val classNumbers = listOf(1, 2, 3, 4, 5, 6, 7, 8) // Примеры номеров пар

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        attendanceDao = AppDatabase.getDatabase(requireContext()).attendanceDao()
        studentDao = AppDatabase.getDatabase(requireContext()).studentDao()

        val view = inflater.inflate(R.layout.fragment_president, container, false)
        recyclerView = view.findViewById(R.id.recycler_view_attendance)
        groupSpinner = view.findViewById(R.id.spinner_group)
        classNumberSpinner = view.findViewById(R.id.spinner_class_number)

        // Настройка адаптеров для спиннеров
        groupSpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, groups)
        classNumberSpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, classNumbers)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Устанавливаем адаптер для RecyclerView
        studentAttendanceAdapter = StudentAttendanceAdapter(studentAttendanceList) { student, isPresent ->
            // Устанавливаем присутствие для студента
            studentAttendanceList.find { it.student.id == student.id }?.isPresent = isPresent
        }
        recyclerView.adapter = studentAttendanceAdapter

        // Загружаем студентов по выбранной группе
        loadStudents()

        // Обработчик для кнопки сохранения посещаемости
        view.findViewById<Button>(R.id.btn_save_attendance).setOnClickListener {
            saveAttendance()
        }

        // Обработчик для изменения выбора группы
        groupSpinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                loadStudents() // Перезагрузить студентов при выборе группы
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        })

        return view
    }

    private fun loadStudents() {
        lifecycleScope.launch {
            // Получение списка студентов из БД
            val selectedGroup = groupSpinner.selectedItem.toString()
            studentDao.getStudentsByGroup(selectedGroup).collect { students ->
                studentAttendanceList.clear()
                studentAttendanceList.addAll(students.map { StudentAttendance(it, false) }) // Изначально все отсутствуют
                studentAttendanceAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun saveAttendance() {
        lifecycleScope.launch {
            // Получаем выбранные данные
            val selectedClassNumber = classNumberSpinner.selectedItem as Int

            studentAttendanceList.forEach { studentAttendance ->
                val attendance = Attendance(
                    studentId = studentAttendance.student.id,
                    classNumber = selectedClassNumber,
                    isPresent = studentAttendance.isPresent
                )
                attendanceDao.insertAttendance(attendance)
            }
            Toast.makeText(requireContext(), "Посещаемость сохранена", Toast.LENGTH_SHORT).show()
        }
    }
}
