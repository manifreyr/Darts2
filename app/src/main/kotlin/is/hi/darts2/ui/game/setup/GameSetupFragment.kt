package `is`.hi.darts2.ui.game.setup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import `is`.hi.darts2.ui.game.GameViewModel
import `is`.hi.darts2.R

class GameSetupFragment : Fragment() {

    private val gameViewModel: GameViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_game_setup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // UI references.
        val spinnerGameType = view.findViewById<Spinner>(R.id.spinnerGameType)
        val spinnerLegs = view.findViewById<Spinner>(R.id.spinnerLegs)
        val playersRecyclerView = view.findViewById<RecyclerView>(R.id.playersRecyclerView)
        val friendsRecyclerView = view.findViewById<RecyclerView>(R.id.friendsRecyclerView)
        val friendsSwipeRefresh = view.findViewById<SwipeRefreshLayout>(R.id.friendsSwipeRefresh)
        val gameTypeTextView = view.findViewById<TextView>(R.id.gameTypeLabel)
        val startGameButton = view.findViewById<Button>(R.id.startGameButton)


        // Setup LayoutManagers.
        playersRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        friendsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Set Adapters if not already set.
        if (playersRecyclerView.adapter == null) {
            playersRecyclerView.adapter = PlayersAdapter()
        }
        if (friendsRecyclerView.adapter == null) {
            friendsRecyclerView.adapter = FriendsAdapter { user ->
                gameViewModel.inviteFriend(user.id)
            }
        }

        spinnerLegs.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedLegsStr = parent.getItemAtPosition(position).toString()
                val selectedLegs = selectedLegsStr.toLongOrNull()
                if (2 == gameViewModel.fetchingGame) {
                    selectedLegs?.let {
                        gameViewModel.updateTotalLegs(it)
                    }
                    gameViewModel.fetchingGame = 0
                } else {
                    gameViewModel.fetchingGame++
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
        spinnerGameType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedGameTypeStr = parent.getItemAtPosition(position).toString()
                val selectedGameType = selectedGameTypeStr.toLongOrNull()
                if (2 == gameViewModel.fetchingGame) {
                    selectedGameType?.let {
                        gameViewModel.updateGameType(it)
                    }
                    gameViewModel.fetchingGame = 0
                } else {
                    gameViewModel.fetchingGame++
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        startGameButton.setOnClickListener {
            gameViewModel.startGame()
        }

        // Observe game info (players, game type, etc.)
        gameViewModel.currentGame.observe(viewLifecycleOwner) { game ->
            if (game != null) {
                // Update spinners and game type text.
                val gameType = game.gameType
                spinnerGameType.adapter?.let { adapter ->
                    for (i in 0 until adapter.count) {
                        if (adapter.getItem(i).toString() == gameType) {
                            spinnerGameType.setSelection(i)
                            break
                        }
                    }
                }
                val totalLegs = game.totalLegs.toInt() // Assuming totalLegs is a Long
                spinnerLegs.adapter?.let { adapter ->
                    for (i in 0 until adapter.count) {
                        if (adapter.getItem(i).toString() == totalLegs.toString()) {
                            spinnerLegs.setSelection(i)
                            break
                        }
                    }
                }
                gameTypeTextView.text = "Game Type: $gameType"
                (playersRecyclerView.adapter as? PlayersAdapter)?.submitList(game.players)
            }
        }

        // Observe the friends list.
        gameViewModel.friendsList.observe(viewLifecycleOwner) { friends ->
            (friendsRecyclerView.adapter as? FriendsAdapter)?.submitList(friends)
        }

        // Optionally, set up swipe-to-refresh.
        friendsSwipeRefresh.setOnRefreshListener {
            gameViewModel.fetchFriends()
            friendsSwipeRefresh.isRefreshing = false
        }

        // Fetch the friends when the fragment is created.
        gameViewModel.fetchFriends()
    }
}
