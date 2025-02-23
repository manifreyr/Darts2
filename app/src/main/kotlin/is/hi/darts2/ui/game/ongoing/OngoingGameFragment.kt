package `is`.hi.darts2.ui.game.ongoing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import `is`.hi.darts2.R
import `is`.hi.darts2.viewmodel.GameViewModel

class OngoingGameFragment : Fragment() {

    private val gameViewModel: GameViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_game_setup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Observe the current game object for any changes.
        gameViewModel.currentGame.observe(viewLifecycleOwner) { game ->
            if (game != null) {
                // Update UI elements accordingly. For example:
                // textViewGameType.text = game.gameType
                // If the game status changes, you might want to notify the parent fragment to perform a fragment transaction.
            }
        }
    }
}
