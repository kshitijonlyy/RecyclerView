package com.kshitij.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter(var list: MutableList<StudentTable>, var studentList: StudentList) :
    RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder>() {

    class RecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvId: TextView = view.findViewById(R.id.tvId)
        var tvName: TextView = view.findViewById(R.id.tvName)
        var edit: ImageView = view.findViewById(R.id.imgEdit)
        var delete: ImageView = view.findViewById(R.id.imgDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_item, parent, false)
        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.tvId.setText(list[position].id.toString())
        holder.tvName.setText(list[position].name)
        holder.tvId.text = (position + 1).toString()

        holder.edit.setOnClickListener {
            studentList.onEdit(list[position])
        }

        holder.delete.setOnClickListener {
            studentList.onDelete(list[position])
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}