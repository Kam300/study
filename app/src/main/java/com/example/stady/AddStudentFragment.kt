package com.example.stady


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class AddStudentFragment : Fragment() {

    private lateinit var studentDao: StudentDao
    private lateinit var spinnerGroup: Spinner

    private lateinit var recyclerView: RecyclerView
    private lateinit var studentAdapter: StudentAdapter
    private val studentList = mutableListOf<Student>()

    private var studentToEdit: Student? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        studentDao = AppDatabase.getDatabase(requireContext()).studentDao()
        val view = inflater.inflate(R.layout.fragment_add_student, container, false)

        val etStudentName: EditText = view.findViewById(R.id.et_student_name)
        spinnerGroup = view.findViewById(R.id.spinner_group)

        recyclerView = view.findViewById(R.id.recycler_view_students)

        // Установка данных в Spinner для группы
        val groups = listOf("ИСпП-22-1", "Группа 2", "Группа 3")
        val groupAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, groups)
        groupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerGroup.adapter = groupAdapter




        // Инициализация RecyclerView
        studentAdapter = StudentAdapter(studentList, { student -> editStudent(student) }) { student -> deleteStudent(student) }
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = studentAdapter

        view.findViewById<Button>(R.id.btn_add_student).setOnClickListener {
            val name = etStudentName.text.toString()
            val group = spinnerGroup.selectedItem.toString()


            if (name.isNotBlank()) {
                lifecycleScope.launch {
                    if (studentToEdit == null) {
                        // Добавление нового студента
                        val student = Student(name = name, group = group, )
                        studentDao.insertStudent(student)
                        Toast.makeText(requireContext(), "Студент добавлен", Toast.LENGTH_SHORT).show()
                    } else {
                        // Обновление существующего студента
                        val updatedStudent = studentToEdit!!.copy(name = name, group = group )
                        studentDao.updateStudent(updatedStudent) // Обновление студента в БД
                        studentToEdit = null // Обнуляем объект редактируемого студента
                        Toast.makeText(requireContext(), "Студент обновлен", Toast.LENGTH_SHORT).show()
                    }
                    etStudentName.text.clear() // Очищаем поле ввода
                    loadStudents() // Обновляем список студентов
                }
            } else {
                Toast.makeText(requireContext(), "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show()
            }
        }

        loadStudents() // загрузка студентов при создании фрагмента

        return view
    }

    private fun loadStudents() {
        lifecycleScope.launch {
            studentDao.getAllStudents().collect { students ->
                studentList.clear()
                studentList.addAll(students)
                studentAdapter.updateStudents(students)
                recyclerView.visibility = if (students.isNotEmpty()) View.VISIBLE else View.GONE
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

    private fun editStudent(student: Student) {
        studentToEdit = student // Сохраняем студента, который нужно редактировать
        val etStudentName: EditText = view?.findViewById(R.id.et_student_name)!!
        etStudentName.setText(student.name) // Заполняем поле имени

        // Установка группы
        val groupPosition = (spinnerGroup.adapter as ArrayAdapter<String>).getPosition(student.group)
        if (groupPosition >= 0) {
            spinnerGroup.setSelection(groupPosition)
        }

    }
}
