package org.projecttracker

import android.support.multidex.MultiDexApplication
import org.projecttracker.di.*
import org.projecttracker.model.db.AppDatabase
import org.projecttracker.model.entity.User
import com.jakewharton.threetenabp.AndroidThreeTen
import com.raizlabs.android.dbflow.config.DatabaseConfig
import com.raizlabs.android.dbflow.config.FlowConfig
import com.raizlabs.android.dbflow.config.FlowLog
import com.raizlabs.android.dbflow.config.FlowManager
import org.ktoggl.android.AndroidKToggl
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty
import org.slf4j.LoggerFactory

class ProjectTrackerApp : MultiDexApplication() {

    private lateinit var applicationComponent: ApplicationComponent
    private lateinit var userComponent: UserComponent

    companion object {
        private val logger = LoggerFactory.getLogger(ProjectTrackerApp::class.java)
        var INSTANCE: ProjectTrackerApp by NotNullSingleValueVar()

        fun getAppComponent() = INSTANCE.applicationComponent

        fun getUserComponent() = INSTANCE.userComponent

        fun createUserComponent(user: User) {
            logger.debug("Create user component from $user")

            val app = INSTANCE
            app.userComponent = app.applicationComponent
                .plus(UserModule(user))
        }
    }

    override fun onCreate() {
        logger.trace("on create app start")
        super.onCreate()
        AndroidKToggl.init(this)
        AndroidThreeTen.init(this)
        INSTANCE = this
        initDatabase()

        initComponents()
    }

    private fun initDatabase() {

        FlowLog.setMinimumLoggingLevel(FlowLog.Level.V)
        val databaseConfig = DatabaseConfig.builder(AppDatabase.javaClass)
            .databaseName("AppDatabase")
            .build()

        val flowConfig = FlowConfig.builder(this)
            .addDatabaseConfig(databaseConfig)
            .build()

        FlowManager.init(flowConfig)
    }

    private fun initComponents() {

        applicationComponent = DaggerApplicationComponent.builder()
            .applicationModule(ApplicationModule(this))
            .build()

        val selectedUser = applicationComponent.userService().getSelectedUser()

        createUserComponent(selectedUser ?: User())
    }

    private class NotNullSingleValueVar<T> : ReadWriteProperty<Any?, T> {

        private var value: T? = null
        override fun getValue(thisRef: Any?, property: KProperty<*>): T {
            return value ?: throw IllegalStateException("${property.name} not initialized")
        }

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
            this.value = if (this.value == null) value
            else throw IllegalStateException("${property.name} already initialized")
        }
    }
}