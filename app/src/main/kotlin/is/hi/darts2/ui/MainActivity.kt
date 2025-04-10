package `is`.hi.darts2.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import `is`.hi.darts2.R
import `is`.hi.darts2.network.Network
import `is`.hi.darts2.ui.start.StartFragment
import `is`.hi.darts2.ui.friends.FriendsFragment
import `is`.hi.darts2.ui.navigation.NavigationFragment
import android.util.Log
import android.view.View
import `is`.hi.darts2.ui.leaderboard.LeaderboardFragment
import `is`.hi.darts2.ui.dashboard.DashboardFragment
import `is`.hi.darts2.ui.profile.ProfileFragment

class MainActivity : AppCompatActivity(), NavigationFragment.NavigationListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Network.init(applicationContext)
        setContentView(R.layout.activity_main)

        // Initially hide the navigation container
        findViewById<View>(R.id.navigation_container).visibility = View.GONE

        if (savedInstanceState == null) {
            // Load the initial fragment (e.g., StartFragment or LoginFragment)
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, StartFragment())
                .commit()
        }
    }

    fun showNavigation() {
        val navContainer = findViewById<View>(R.id.navigation_container)
        navContainer.visibility = View.VISIBLE

        val navigationFragment = NavigationFragment.newInstance().apply {
            listener = this@MainActivity
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.navigation_container, navigationFragment)
            .commit()
    }

    fun hideNavigation() {
        findViewById<View>(R.id.navigation_container).visibility = View.GONE
    }

    override fun onNavigationItemSelected(item: Int) {
        Log.d("NAVIGATION", "navigating")
        when (item) {
            1 -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, DashboardFragment())
                    .commit()
            }

            2 -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, LeaderboardFragment())
                    .commit()
            }

            3 -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, FriendsFragment())
                    .commit()
            }

            4 -> {
                 supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, ProfileFragment())
                    .commit()
            }
        }
    }
}
