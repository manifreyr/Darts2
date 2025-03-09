package `is`.hi.darts2.ui.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import `is`.hi.darts2.viewmodel.DashboardViewModel
import `is`.hi.darts2.R
import `is`.hi.darts2.model.Game

class SetupGamesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SetupGamesAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private val dashboardViewModel: DashboardViewModel by viewModels({ requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_setup_games, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.setupGamesRecyclerView)
        swipeRefreshLayout = view.findViewById(R.id.setupGamesContainer)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = SetupGamesAdapter { game: Game ->
            dashboardViewModel.onSetupGameClicked(game)
        }
        recyclerView.adapter = adapter

        dashboardViewModel.setupGames.observe(viewLifecycleOwner) { games ->
            adapter.submitList(games)
            swipeRefreshLayout.isRefreshing = false
        }

        swipeRefreshLayout.setOnRefreshListener {
            dashboardViewModel.fetchSetupGames {
                swipeRefreshLayout.isRefreshing = false
                Log.d("SetupGamesFragment", dashboardViewModel.setupGames.value.toString())
            }
        }

        dashboardViewModel.fetchSetupGames()
    }
}
