package com.example.calendarapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.calendarapplication.databinding.ItemTaskBinding
import com.example.calendarapplication.model.TaskItem

class TaskListAdapter(
    private val tasks: List<TaskItem>,
    private val onDeleteTask: (TaskItem) -> Unit
) : RecyclerView.Adapter<TaskListAdapter.TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding, onDeleteTask)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.bind(task)
    }

    override fun getItemCount(): Int = tasks.size

    class TaskViewHolder(
        private val binding: ItemTaskBinding,
        private val onDeleteTask: (TaskItem) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(task: TaskItem) {
            binding.tvTaskId.text = "Task ID: ${task.task_id}"
            binding.tvTaskTitle.text = "Title: ${task.task_detail.title}"
            binding.tvTaskDescription.text = "Description: ${task.task_detail.description}"
            binding.deleteTask.visibility = View.GONE

            binding.root.setOnLongClickListener {
                binding.deleteTask.visibility = View.VISIBLE
                true
            }

            binding.root.setOnClickListener() {
                binding.deleteTask.visibility = View.GONE
                false
            }

            binding.deleteTask.setOnClickListener {
                onDeleteTask(task)
            }
        }
    }
}