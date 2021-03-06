package org.projecttracker.view.timer

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import org.projecttracker.R
import org.projecttracker.ProjectTrackerApp
import org.projecttracker.databinding.TimerActivityBinding
import org.projecttracker.di.TimerViewModelModule
import org.projecttracker.view.BaseActivity
import org.projecttracker.viewmodel.timer.DailyTimerViewModel
import kotlinx.android.synthetic.main.timer_activity_content.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.projecttracker.event.TodayTimerDataRefreshedEvent
import org.projecttracker.viewmodel.NetworkStateMonitor
import org.slf4j.LoggerFactory
import javax.inject.Inject

class TimerActivity : BaseActivity() {

    companion object {
        private val logger = LoggerFactory.getLogger(TimerActivity::class.java)
        fun newIntent(context: Context): Intent {
            return Intent(context, TimerActivity::class.java)
        }
    }

    @Inject
    lateinit var dailyTimerViewModel: DailyTimerViewModel
    @Inject
    lateinit var networkStateMonitor: NetworkStateMonitor

    private lateinit var projectAdapter: ProjectAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        logger.trace("on create start")
        super.onCreate(savedInstanceState)

        setUpSwipeRefresh()
        setUpProjectList()
    }

    override fun onStart() {
        super.onStart()

        networkStateMonitor.startMonitoring()

        EventBus.getDefault().register(this)
        EventBus.getDefault().register(projectAdapter)
    }

    override fun onStop() {
        super.onStop()

        EventBus.getDefault().unregister(this)
        EventBus.getDefault().unregister(projectAdapter)

        networkStateMonitor.stopMonitoring()
    }

    override fun inject() {
        ProjectTrackerApp.getUserComponent()
            .plus(TimerViewModelModule(this))
            .inject(this)
    }

    override fun setUpBinding() {

        val binding: TimerActivityBinding = DataBindingUtil.setContentView(this, R.layout.timer_activity)
        binding.viewModel = dailyTimerViewModel
    }

    private fun setUpSwipeRefresh() {
        swipeRefresh.setOnRefreshListener { dailyTimerViewModel.refresh() }
    }

    private fun setUpProjectList() {
        projectAdapter = ProjectAdapter(dailyTimerViewModel, supportFragmentManager)

        val layoutManager = GridLayoutManager(this, 2)

        recycleView.layoutManager = layoutManager
        recycleView.adapter = projectAdapter
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onTodayTimerDataRefreshed(event: TodayTimerDataRefreshedEvent) {

        logger.info("Refreshing finished")

        swipeRefresh.isRefreshing = false
    }
}
