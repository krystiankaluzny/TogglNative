package app.obywatel.togglnative.viewmodel.user

import android.util.Log
import app.obywatel.togglnative.model.repository.UserItem

class UsersViewModel {

    private val userItems: MutableList<UserItem>

    init {
        val userView1 = UserItem(1, "Dupa")
        val userView2 = UserItem(24, "Blada")
        userItems = mutableListOf(userView1, userView2)
    }

    fun userCount() = userItems.size
    fun singleUserViewModel(position: Int) = SingleUserViewModel(userItems[position])

    fun addUserByApiToken(apiToken: String) {

        Log.d("UsersViewModel", apiToken)
    }
}