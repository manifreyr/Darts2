package `is`.hi.darts2.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import `is`.hi.darts2.model.Game
import `is`.hi.darts2.R

class OngoingGamesAdapter(
    private val onContinueClicked: (Game) -> Unit
) : ListAdapter<Game, OngoingGamesAdapter.OngoingGameViewHolder>(OngoingGameDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OngoingGameViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.ongoing_game_list_item, parent, false)
        return OngoingGameViewHolder(view)
    }

    override fun onBindViewHolder(holder: OngoingGameViewHolder, position: Int) {
        val game = getItem(position)
        holder.bind(game)
    }

    inner class OngoingGameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val gameDetailsTextView: TextView = itemView.findViewById(R.id.gameDetailsTextView)
        private val continueButton: Button = itemView.findViewById(R.id.continueButton)

        fun bind(game: Game) {
            gameDetailsTextView.text = "Game ${game.id}: ${game.gameType} - Ongoing"
            continueButton.setOnClickListener {
                onContinueClicked(game)
            }
        }
    }
}

class OngoingGameDiffCallback : DiffUtil.ItemCallback<Game>() {
    override fun areItemsTheSame(oldItem: Game, newItem: Game): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Game, newItem: Game): Boolean {
        return oldItem == newItem
    }
}
