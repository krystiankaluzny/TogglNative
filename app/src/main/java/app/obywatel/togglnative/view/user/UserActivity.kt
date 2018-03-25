package app.obywatel.togglnative.view.user

import android.app.Dialog
import android.app.FragmentManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import app.obywatel.togglnative.R
import app.obywatel.togglnative.viewmodel.user.UsersViewModel
import kotlinx.android.synthetic.main.user_activity.*
import kotlinx.android.synthetic.main.user_app_bar.*
import java.util.*

class UserActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val random = Random()
    internal val usersViewModel: UsersViewModel = UsersViewModel()

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, UserActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_activity)
        setSupportActionBar(toolbar)

        addUserButton.setOnClickListener { showAddUserDialog() }

        initHamburgerButton()
        initUserList()

        navigationView.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun initHamburgerButton() {
        val hamburgerToggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0)
        drawerLayout.addDrawerListener(hamburgerToggle)
        hamburgerToggle.syncState()
    }

    private fun initUserList() {

        val userAdapter = UserAdapter(usersViewModel)

        recycleView.layoutManager = LinearLayoutManager(this)
        recycleView.adapter = userAdapter
    }

    private fun showAddUserDialog() {
        val addUserDialog = AddUserDialog()

        addUserDialog.show(fragmentManager, "Add User")
    }
}