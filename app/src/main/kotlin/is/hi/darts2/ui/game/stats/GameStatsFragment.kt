package `is`.hi.darts2.ui.game.stats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import `is`.hi.darts2.R
import `is`.hi.darts2.viewmodel.GameViewModel
import java.util.Locale

class GameStatsFragment : Fragment() {

    private val gameViewModel: GameViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_game_stats, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val textviewPlayer1Name = view.findViewById<TextView>(R.id.player1Name)
        val textviewPlayer2Name = view.findViewById<TextView>(R.id.player2Name)
        val textviewPlayer1Score = view.findViewById<TextView>(R.id.player1Score)
        val textviewPlayer2Score = view.findViewById<TextView>(R.id.player2Score)
        val textviewPlayer1ThreeDartAvg = view.findViewById<TextView>(R.id.player1_3dartAvg)
        val textviewPlayer2ThreeDartAvg = view.findViewById<TextView>(R.id.player2_3dartAvg)
        val textviewPlayer1First9Avg = view.findViewById<TextView>(R.id.player1_first9Avg)
        val textviewPlayer2First9Avg = view.findViewById<TextView>(R.id.player2_first9Avg)
        val textviewPlayer1BestLeg = view.findViewById<TextView>(R.id.player1_bestLeg)
        val textviewPlayer2BestLeg = view.findViewById<TextView>(R.id.player2_bestLeg)
        val textviewPlayer1WorstLeg = view.findViewById<TextView>(R.id.player1_worstLeg)
        val textviewPlayer2WorstLeg = view.findViewById<TextView>(R.id.player2_worstLeg)
        val textviewPlayer1HighScore = view.findViewById<TextView>(R.id.player1_highScore)
        val textviewPlayer2HighScore = view.findViewById<TextView>(R.id.player2_highScore)

        gameViewModel.currentGame.observe(viewLifecycleOwner) { game ->
            if (game != null && game.players.size == 2) {
                val player1 = game.players[0]
                val player2 = game.players[1]

                textviewPlayer1Name.text = player1.name
                textviewPlayer2Name.text = player2.name

                textviewPlayer1Score.text = String.format(Locale.getDefault(), "%d", player1.score)
                textviewPlayer2Score.text = String.format(Locale.getDefault(), "%d", player2.score)

                textviewPlayer1ThreeDartAvg.text = String.format(Locale.getDefault(), "%.2f", gameViewModel.getGameThreeDartAverage(player1.id))
                textviewPlayer2ThreeDartAvg.text = String.format(Locale.getDefault(), "%.2f", gameViewModel.getGameThreeDartAverage(player2.id))

                textviewPlayer1First9Avg.text = String.format(Locale.getDefault(), "%.2f", gameViewModel.getGameFirst9Average(player1.id))
                textviewPlayer2First9Avg.text = String.format(Locale.getDefault(), "%.2f", gameViewModel.getGameFirst9Average(player2.id))

                textviewPlayer1BestLeg.text = String.format(Locale.getDefault(), "%d", gameViewModel.getBestLegForPlayer(player1.id))
                textviewPlayer2BestLeg.text = String.format(Locale.getDefault(), "%d", gameViewModel.getBestLegForPlayer(player2.id))

                textviewPlayer1WorstLeg.text = String.format(Locale.getDefault(), "%d", gameViewModel.getWorstLegForPlayer(player1.id))
                textviewPlayer2WorstLeg.text = String.format(Locale.getDefault(), "%d", gameViewModel.getWorstLegForPlayer(player2.id))

                textviewPlayer1HighScore.text = String.format(Locale.getDefault(), "%d", gameViewModel.getHighestScoreForPlayer(player1.id))
                textviewPlayer2HighScore.text = String.format(Locale.getDefault(), "%d", gameViewModel.getHighestScoreForPlayer(player2.id))
            }
        }
    }
}


