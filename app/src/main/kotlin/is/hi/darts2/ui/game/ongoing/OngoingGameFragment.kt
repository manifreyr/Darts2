package `is`.hi.darts2.ui.game.ongoing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import `is`.hi.darts2.R
import `is`.hi.darts2.ui.game.GameViewModel
import kotlinx.coroutines.launch

class OngoingGameFragment : Fragment() {

    private val gameViewModel: GameViewModel by activityViewModels()

    private lateinit var playerDistance: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_game_ongoing, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Get references to UI components.
        val textViewCurrentUserName = view.findViewById<TextView>(R.id.textViewCurrentUserName)
        val textViewCurrentUserScore = view.findViewById<TextView>(R.id.textViewCurrentUserScore)
        val textViewOpponentName = view.findViewById<TextView>(R.id.textViewOpponentName)
        val textViewOpponentScore = view.findViewById<TextView>(R.id.textViewOpponentScore)
        val scoreInputEditText = view.findViewById<EditText>(R.id.scoreInputEditText)
        val submitScoreButton = view.findViewById<Button>(R.id.submitScoreButton)
        playerDistance = view.findViewById<TextView>(R.id.playerDistance)

        gameViewModel.currentGame.observe(viewLifecycleOwner) { game ->
            val currentUser = gameViewModel.currentUser.value
            if (game != null && currentUser != null) {
                val players = game.players
                val currentIndex = players.indexOfFirst { it.id == currentUser.id }
                if (currentIndex != -1) {
                    val currentPlayer = players[currentIndex]
                    val opponent =
                        if (players.size > 1) players[(currentIndex + 1) % players.size] else null

                    textViewCurrentUserName.text = currentPlayer.name
                    textViewCurrentUserScore.text =
                        "Score: ${currentPlayer.score}"  // Adjust field names as needed

                    opponent?.let {
                        textViewOpponentName.text = it.name
                        textViewOpponentScore.text = "Score: ${it.score}"
                    }
                    lifecycleScope.launch {
                        initializeLocationText() // Calling suspend function in a coroutine
                    }

                }
            }
        }

        submitScoreButton.setOnClickListener {
            // Get the score input as a string, trim any extra spaces.
            val scoreText = scoreInputEditText.text.toString().trim()
            if (scoreText.isNotEmpty()) {
                val score = scoreText.toLongOrNull()
                if (score != null) {
                    gameViewModel.submitThrow(score)
                } else {
                    // Handle invalid input (e.g. show a toast for non-numeric input)
                }
            }
        }
    }

    private suspend fun initializeLocationText() {
        playerDistance.text =
            "Distance between you and your opponent is: ${gameViewModel.fetchPlayerDistance()}"
    }
}
