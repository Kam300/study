package com.example.stady

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class FragmentTeacherView2 : Fragment() {

    private lateinit var studentDao: StudentDao
    private lateinit var recyclerView: RecyclerView
    private lateinit var groupSpinner: Spinner
    private lateinit var studentAdapter2: StudentAdapter2 // Обратите внимание на правильное имя адаптера
    private val studentList = mutableListOf<Student>()

    private val groups = listOf("ИСпП-22-1", "Группа 2", "Группа 3") // Группы

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        studentDao = AppDatabase.getDatabase(requireContext()).studentDao()

        // Изменим разметку на fragment_teacher_view2.xml
        val view = inflater.inflate(R.layout.fragment_teacher_view2, container, false)
        recyclerView = view.findViewById(R.id.recycler_view_attendance)
        groupSpinner = view.findViewById(R.id.spinner_group)

        // Настройка адаптеров для спиннера
        groupSpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, groups)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Инициализация адаптера **до** его установки
        studentAdapter2 = StudentAdapter2(studentList) { student -> deleteStudent(student) }
        recyclerView.adapter = studentAdapter2 // Установка адаптера

        // Загружаем студентов по выбранной группе
        loadStudents()

        // Обработчик для изменения выбора группы
        groupSpinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                loadStudents() // Перезагрузка студентов при выборе группы
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
                // Обновление списка студентов в адаптере
                studentList.clear()
                studentList.addAll(students) // Добавление загруженных студентов
                studentAdapter2.notifyDataSetChanged() // Уведомление адаптера об изменениях
            }
        }
    }

    private fun deleteStudent(student: Student) {
        lifecycleScope.launch {
            studentDao.deleteStudent(student) // Удаление студента из БД
            loadStudents() // обновляем список студентов
            Toast.makeText(requireContext(), "Студент удален", Toast.LENGTH_SHORT).show()
        }
    }
    // Логика для сохранения изменений может быть добавлена позже
}
