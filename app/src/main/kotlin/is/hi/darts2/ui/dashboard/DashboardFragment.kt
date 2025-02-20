package `is`.hi.darts2.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.google.android.material.tabs.TabLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import `is`.hi.darts2.R
import `is`.hi.darts2.viewmodel.DashboardViewModel

class DashboardFragment : Fragment() {

    private val dashboardViewModel: DashboardViewModel by viewModels()
    private lateinit var startNewGameButton: Button
    private lateinit var tabLayout: TabLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        startNewGameButton = view.findViewById(R.id.startNewGameButton)
        tabLayout = view.findViewById(R.id.dashboardTabLayout)

        startNewGameButton.setOnClickListener {
            dashboardViewModel.createNewGame { success, message ->
                if (success) {
                    val game = dashboardViewModel.createdGame.value
                    if (game != null) {
                        Toast.makeText(
                            requireContext(),
                            message ?: "Created game",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        message ?: "Failed to create game",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        tabLayout.addTab(tabLayout.newTab().setText("Setup"))
        tabLayout.addTab(tabLayout.newTab().setText("Ongoing"))
        tabLayout.addTab(tabLayout.newTab().setText("Invites"))

        childFragmentManager.beginTransaction()
            .replace(R.id.listContainer, SetupGamesFragment())
            .commit()

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val fragment = when (tab.position) {
                    0 -> SetupGamesFragment()
                    1 -> OngoingGamesFragment()
                    2 -> GameInvitesFragment()
                    else -> SetupGamesFragment()
                }
                childFragmentManager.beginTransaction()
                    .replace(R.id.listContainer, fragment)
                    .commit()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }
}
