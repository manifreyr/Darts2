package `is`.hi.darts2.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import `is`.hi.darts2.viewmodel.DashboardViewModel
import `is`.hi.darts2.R

class GameInvitesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: GameInvitesAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private val dashboardViewModel: DashboardViewModel by viewModels({ requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_game_invites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.gameInvitesRecyclerView)
        swipeRefreshLayout = view.findViewById(R.id.gameInvitesContainer)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = GameInvitesAdapter(
            onAcceptClicked = { invite ->
                dashboardViewModel.acceptGameInvite(invite.id)
            },
            onDeclineClicked = { invite ->
                dashboardViewModel.declineGameInvite(invite.id)
            }
        )
        recyclerView.adapter = adapter

        dashboardViewModel.gameInvites.observe(viewLifecycleOwner) { invites ->
            adapter.submitList(invites)
            swipeRefreshLayout.isRefreshing = false
        }

        swipeRefreshLayout.setOnRefreshListener {
            dashboardViewModel.fetchGameInvites {
                swipeRefreshLayout.isRefreshing = false
            }
        }

        dashboardViewModel.fetchGameInvites()
    }
}
