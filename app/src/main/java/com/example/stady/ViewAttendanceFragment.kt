package com.example.stady
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class ViewAttendanceFragment : Fragment() {

    private lateinit var attendanceDao: AttendanceDao
    private lateinit var listView: ListView
    private val attendanceList = mutableListOf<Attendance>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        attendanceDao = AppDatabase.getDatabase(requireContext()).attendanceDao()

        val view = inflater.inflate(R.layout.fragment_view_attendance, container, false)
        listView = view.findViewById(R.id.list_view_attendance)

        view.findViewById<Button>(R.id.btn_load_attendance).setOnClickListener {
            loadAttendance()
        }

        return view
    }

    private fun loadAttendance() {
        lifecycleScope.launch {
            val classNumber = 1

            // Загружаем посещаемость
            val attendanceRecords = attendanceDao.getAttendance(classNumber)

            // Создаем список для хранения студентов
            val attendanceList = mutableListOf<StudentAttendance>()

            // Получаем всех студентов
            val studentDao = AppDatabase.getDatabase(requireContext()).studentDao()

            studentDao.getAllStudents().collect { students ->
                attendanceList.clear()

                // Теперь заполняем список attendanceList с состоянием присутствия студентов
                attendanceList.addAll(students.map { student ->
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
