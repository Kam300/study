package com.example.stady
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class ViewAttendanceFragment : Fragment() {

    private lateinit var attendanceDao: AttendanceDao
    private lateinit var listView: ListView
    private lateinit var spinnerGroup: Spinner
    private lateinit var spinnerClassNumber: Spinner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        attendanceDao = AppDatabase.getDatabase(requireContext()).attendanceDao()

        val view = inflater.inflate(R.layout.fragment_view_attendance, container, false)
        listView = view.findViewById(R.id.list_view_attendance)
        spinnerGroup = view.findViewById(R.id.spinner_group)
        spinnerClassNumber = view.findViewById(R.id.spinner_class_number)

        view.findViewById<Button>(R.id.btn_load_attendance).setOnClickListener {
            loadAttendance()
        }

        // Здесь вы можете установить адаптеры для Spinner
        setupSpinners()

        return view
    }

    private fun setupSpinners() {
        lifecycleScope.launch {
            // Получение групп и классов из базы данных
            val groups = attendanceDao.getAllGroups() // Предположим, что у вас есть метод для получения групп
            val classNumbers = attendanceDao.getAllClassNumbers().sorted() // Сортируем номера классов

            // Установка адаптеров
            spinnerGroup.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, groups)
            spinnerClassNumber.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, classNumbers)
        }
    }


    private fun loadAttendance() {
        lifecycleScope.launch {
            val selectedGroup = spinnerGroup.selectedItem.toString()
            val selectedClassNumber = spinnerClassNumber.selectedItem.toString().toInt()

            // Загружаем посещаемость
            val attendanceRecords = attendanceDao.getAttendance(selectedClassNumber)

            // Создаем список для хранения студентов
            val attendanceList = mutableListOf<StudentAttendance>()

            // Получаем всех студентов
            val studentDao = AppDatabase.getDatabase(requireContext()).studentDao()

            studentDao.getAllStudents().collect { students ->
                attendanceList.clear()

                // Фильтруем студентов по группе
                val filteredStudents = students.filter { it.group == selectedGroup }

                // Заполняем список attendanceList с состоянием присутствия студентов
                attendanceList.addAll(filteredStudents.map { student ->
                    StudentAttendance(
                        student = student,
                        isPresent = attendanceRecords.any { it.studentId == student.id && it.isPresent }
                    )
                })

                if (attendanceList.isEmpty()) {
                    Toast.makeText(requireContext(), "Нет записей о посещаемости", Toast.LENGTH_SHORT).show()
                } else {
                    // Обновляем адаптер для отображения данных в ListView
                    val adapter = AttendanceAdapter(requireContext(), attendanceList)
                    listView.adapter = adapter
                }
            }
        }
    }
}
