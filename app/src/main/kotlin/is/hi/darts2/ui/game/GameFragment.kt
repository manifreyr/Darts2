package `is`.hi.darts2.ui.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import `is`.hi.darts2.R
import `is`.hi.darts2.model.GameStatus
import `is`.hi.darts2.ui.game.ongoing.OngoingGameFragment
import `is`.hi.darts2.ui.game.setup.GameSetupFragment
import `is`.hi.darts2.ui.game.stats.GameStatsFragment

class GameFragment : Fragment() {

    private val gameViewModel: GameViewModel by activityViewModels()


    companion object {
        private const val ARG_GAME_ID = "gameId"
        fun newInstance(gameId: Long) = GameFragment().apply {
            arguments = Bundle().apply { putLong(ARG_GAME_ID, gameId) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (it.containsKey(ARG_GAME_ID)) {
                val gameId = it.getLong(ARG_GAME_ID)
                gameViewModel.setGameId(gameId)
                gameViewModel.fetchGame(gameId)
            }
            gameViewModel.fetchCurrentUser();
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        gameViewModel.currentGame.observe(viewLifecycleOwner) { game ->
            if (game != null) {
                when (game.status) {
                    GameStatus.SETUP -> {
                        childFragmentManager.beginTransaction()
                            .replace(R.id.gameContainer, GameSetupFragment())
                            .commit()
                    }

                    GameStatus.ONGOING -> {
                        childFragmentManager.beginTransaction()
                            .replace(R.id.gameContainer, OngoingGameFragment())
                            .commit()
                    }

                    GameStatus.COMPLETED -> {
                        childFragmentManager.beginTransaction()
                            .replace(R.id.gameContainer, GameStatsFragment())
                            .commit()
                    }

                    else -> { /* Should never happen */
                    }
                }
            }
        }
        if (gameViewModel.currentGame.value != null) {
            when (gameViewModel.currentGame.value!!.status) {
                GameStatus.SETUP -> {
                    childFragmentManager.beginTransaction()
                        .replace(R.id.gameContainer, GameSetupFragment())
                        .commit()
                }

                GameStatus.ONGOING -> {
                    childFragmentManager.beginTransaction()
                        .replace(R.id.gameContainer, OngoingGameFragment())
                        .commit()
                }

                GameStatus.COMPLETED -> {
                    childFragmentManager.beginTransaction()
                        .replace(R.id.gameContainer, GameStatsFragment())
                        .commit()
                }

                else -> { /* Should never happen */
                }
            }
        }
    }
}
