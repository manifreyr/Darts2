package `is`.hi.darts2.ui.game.stats

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import `is`.hi.darts2.R
import `is`.hi.darts2.ui.game.GameViewModel
import java.util.Locale

class GameStatsFragment : Fragment() {

    private val gameViewModel: GameViewModel by activityViewModels()

    private lateinit var textviewPlayer1Name: TextView
    private lateinit var textviewPlayer2Name: TextView
    private lateinit var textviewPlayer1Score: TextView
    private lateinit var textviewPlayer2Score: TextView
    private lateinit var textviewPlayer1ThreeDartAvg: TextView
    private lateinit var textviewPlayer2ThreeDartAvg: TextView
    private lateinit var textviewPlayer1First9Avg: TextView
    private lateinit var textviewPlayer2First9Avg: TextView
    private lateinit var textviewPlayer1BestLeg: TextView
    private lateinit var textviewPlayer2BestLeg: TextView
    private lateinit var textviewPlayer1WorstLeg: TextView
    private lateinit var textviewPlayer2WorstLeg: TextView
    private lateinit var textviewPlayer1HighScore: TextView
    private lateinit var textviewPlayer2HighScore: TextView
    private lateinit var textviewPlayer1TableName: TextView
    private lateinit var textviewPlayer2TableName: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_game_stats, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initializeTextView(view);

        gameViewModel.currentGame.observe(viewLifecycleOwner) { game ->
            if (game != null && game.players.size == 2) {
                val player1 = game.players[0]
                val player2 = game.players[1]

                textviewPlayer1Name.text = "${player1.name} : "
                textviewPlayer2Name.text = "${player2.name} : "


                textviewPlayer1Score.text = String.format(Locale.getDefault(), "%d", player1.legsWon)
                textviewPlayer2Score.text = String.format(Locale.getDefault(), "%d", player2.legsWon)

                textviewPlayer1ThreeDartAvg.text = String.format(Locale.getDefault(), "%.2f", gameViewModel.getGameThreeDartAverage(player1.id))
                textviewPlayer2ThreeDartAvg.text = String.format(Locale.getDefault(), "%.2f", gameViewModel.getGameThreeDartAverage(player2.id))

                textviewPlayer1First9Avg.text = String.format(Locale.getDefault(), "%.2f", gameViewModel.getGameFirst9Average(player1.id))
                textviewPlayer2First9Avg.text = String.format(Locale.getDefault(), "%.2f", gameViewModel.getGameFirst9Average(player2.id))

                textviewPlayer1BestLeg.text = String.format(Locale.getDefault(), "%s", gameViewModel.getBestLegForPlayer(player1.id))
                textviewPlayer2BestLeg.text = String.format(Locale.getDefault(), "%s", gameViewModel.getBestLegForPlayer(player2.id))

                textviewPlayer1WorstLeg.text = String.format(Locale.getDefault(), "%s", gameViewModel.getWorstLegForPlayer(player1.id))
                textviewPlayer2WorstLeg.text = String.format(Locale.getDefault(), "%s", gameViewModel.getWorstLegForPlayer(player2.id))

                textviewPlayer1HighScore.text = String.format(Locale.getDefault(), "%d", gameViewModel.getHighestScoreForPlayer(player1.id))
                textviewPlayer2HighScore.text = String.format(Locale.getDefault(), "%d", gameViewModel.getHighestScoreForPlayer(player2.id))

                textviewPlayer1TableName.text = player1.name
                textviewPlayer2TableName.text = player2.name
            }
        }
    }

    private fun initializeTextView(view: View){
        textviewPlayer1Name = view.findViewById(R.id.player1Name)
        textviewPlayer2Name = view.findViewById(R.id.player2Name)
        textviewPlayer1Score = view.findViewById(R.id.player1Score)
        textviewPlayer2Score = view.findViewById(R.id.player2Score)
        textviewPlayer1ThreeDartAvg = view.findViewById(R.id.player1_3dartAvg)
        textviewPlayer2ThreeDartAvg = view.findViewById(R.id.player2_3dartAvg)
        textviewPlayer1First9Avg = view.findViewById(R.id.player1_first9Avg)
        textviewPlayer2First9Avg = view.findViewById(R.id.player2_first9Avg)
        textviewPlayer1BestLeg = view.findViewById(R.id.player1_bestLeg)
        textviewPlayer2BestLeg = view.findViewById(R.id.player2_bestLeg)
        textviewPlayer1WorstLeg = view.findViewById(R.id.player1_worstLeg)
        textviewPlayer2WorstLeg = view.findViewById(R.id.player2_worstLeg)
        textviewPlayer1HighScore = view.findViewById(R.id.player1_highScore)
        textviewPlayer2HighScore = view.findViewById(R.id.player2_highScore)
        textviewPlayer1TableName = view.findViewById(R.id.player1TableName)
        textviewPlayer2TableName = view.findViewById(R.id.player2TableName)
    }
}


