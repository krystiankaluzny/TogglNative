package org.projecttracker.view.timer

import android.support.v4.app.FragmentManager
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.projecttracker.R
import org.projecttracker.databinding.ProjectViewBinding
import org.projecttracker.event.ProjectsUpdatedEvent
import org.projecttracker.view.bind
import org.projecttracker.viewmodel.timer.DailyTimerViewModel

class ProjectAdapter(private val dailyTimerViewModel: DailyTimerViewModel, private val supportFragmentManager: FragmentManager)
    : RecyclerView.Adapter<ProjectAdapter.ViewHolder>() {

    override fun getItemCount() = dailyTimerViewModel.projectsCount()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(parent.bind(R.layout.project_view))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.viewModel = dailyTimerViewModel.singleProjectViewModel(position)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onProjectsUpdated(projectsUpdatedEvent: ProjectsUpdatedEvent) {
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ProjectViewBinding) : RecyclerView.ViewHolder(binding.content) {

        init {
            binding.content.setOnClickListener {
                binding.viewModel?.let {
                    dailyTimerViewModel.toggleProject(it)
                }
            }

            binding.content.setOnLongClickListener {
                val startProjectTimeEntryDialog = StartProjectTimeEntryDialog()

                startProjectTimeEntryDialog.show(supportFragmentManager, "Start Project Time Entry")

                return@setOnLongClickListener true
            }
        }
    }
}