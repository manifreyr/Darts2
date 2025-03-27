package `is`.hi.darts2.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import `is`.hi.darts2.R
import `is`.hi.darts2.model.Game
import `is`.hi.darts2.viewmodel.DashboardViewModel

class OngoingGamesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: OngoingGamesAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private val dashboardViewModel: DashboardViewModel by viewModels({ requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_ongoing_games, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.ongoingGamesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        swipeRefreshLayout = view.findViewById(R.id.ongoingGamesContainer)
        adapter = OngoingGamesAdapter(
            // Placeholder
            onContinueClicked = { game: Game ->
                dashboardViewModel.navigateToGameScreen(game.id)
            }
        )
        recyclerView.adapter = adapter

        dashboardViewModel.ongoingGames.observe(viewLifecycleOwner) { games ->
            adapter.submitList(games)
        }

        swipeRefreshLayout.setOnRefreshListener {
            dashboardViewModel.fetchOngoingGames {
                swipeRefreshLayout.isRefreshing = false
            }
        }
        dashboardViewModel.fetchOngoingGames()
    }
}
