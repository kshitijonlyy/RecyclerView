package com.kshitij.recyclerview

import android.app.Dialog
import android.os.AsyncTask
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.kshitij.recyclerview.databinding.ActivityMainBinding
import com.kshitij.recyclerview.databinding.LayoutDeleteBinding
import com.kshitij.recyclerview.databinding.LayoutDialogBinding
import com.kshitij.recyclerview.databinding.LayoutEditBinding

class MainActivity : AppCompatActivity(), StudentList {
    lateinit var binding: ActivityMainBinding
    lateinit var name: String

    lateinit var studentDb: StudentDb
    var list = ArrayList<StudentTable>()

    lateinit var recyclerAdapter: RecyclerAdapter
    lateinit var layoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        studentDb = StudentDb.getDatabase(this)
        recyclerAdapter = RecyclerAdapter(list, this)
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = recyclerAdapter
        binding.recyclerView.layoutManager = layoutManager

        getFromDb()

        binding.btnAdd.setOnClickListener {
            val dialog = Dialog(this)
            val dialogBinding = LayoutDialogBinding.inflate(layoutInflater)
            dialog.setContentView(dialogBinding.root)
            dialogBinding.tvName.setText("")
            dialogBinding.btnAdd.setOnClickListener {
                name = dialogBinding.tvName.text.toString()
                saveInDb(name ?: "")

                dialog.dismiss()
            }
            dialog.show()
        }
    }

    fun saveInDb(name: String) {
        class Save : AsyncTask<Void, Void, Void>() {
            override fun doInBackground(vararg p0: Void?): Void? {
                studentDb.studentDao().insertStudent(StudentTable(name = name))
                return null
            }

            override fun onPostExecute(result: Void?) {
                super.onPostExecute(result)
                getFromDb()
            }
        }
        Save().execute()
    }

    fun getFromDb() {
        list.clear()
        class Get : AsyncTask<Void, Void, Void>() {
            override fun doInBackground(vararg p0: Void?): Void? {
                list.addAll(studentDb.studentDao().getStudent())
                return null
            }

            override fun onPostExecute(result: Void?) {
                super.onPostExecute(result)
                recyclerAdapter.notifyDataSetChanged()
            }
        }
        Get().execute()
    }

    fun editDb(studentTable: StudentTable) {
        class Edit : AsyncTask<Void, Void, Void>() {
            override fun doInBackground(vararg p0: Void?): Void? {
                studentDb.studentDao().editStudent(studentTable)
                return null
            }

            override fun onPostExecute(result: Void?) {
                super.onPostExecute(result)
                getFromDb()
            }
        }
        Edit().execute()
    }

    fun deleteDb(studentTable: StudentTable) {
        class Delete : AsyncTask<Void, Void, Void>() {
            override fun doInBackground(vararg p0: Void?): Void? {
                studentDb.studentDao().deleteStudent(studentTable)
                return null
            }

            override fun onPostExecute(result: Void?) {
                super.onPostExecute(result)
                getFromDb()
            }
        }
        Delete().execute()
    }

    override fun onEdit(studentTable: StudentTable) {
        val edit = Dialog(this)
        val editBinding = LayoutEditBinding.inflate(layoutInflater)
        edit.setContentView(editBinding.root)
        editBinding.tvName.setText(studentTable.name)
        edit.show()
        editBinding.tvName.requestFocus()
        editBinding.btnEdit.setOnClickListener {
            studentTable.name = editBinding.tvName.text.toString()
            editDb(studentTable)
            edit.dismiss()
        }
    }

    override fun onDelete(studentTable: StudentTable) {
        val delete = Dialog(this)
        val deleteBinding = LayoutDeleteBinding.inflate(layoutInflater)
        delete.setContentView(deleteBinding.root)
        deleteBinding.tvName.setText("Delete '" + studentTable.name + "'?")
        delete.show()
        deleteBinding.btnEdit.setOnClickListener {
            deleteDb(studentTable)
            delete.dismiss()
        }
    }
}