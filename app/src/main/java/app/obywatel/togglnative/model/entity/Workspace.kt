package app.obywatel.togglnative.model.entity

import app.obywatel.togglnative.model.repository.AppDatabase
import com.raizlabs.android.dbflow.annotation.*

@Table(database = AppDatabase::class)
class Workspace(
    @PrimaryKey @Unique var id: Long = -1,
    @Column var name: String = "",
    @ForeignKey(tableClass = User::class) var userId: Long? = null
)