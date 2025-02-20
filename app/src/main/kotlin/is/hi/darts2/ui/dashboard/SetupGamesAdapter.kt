package `is`.hi.darts2.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import `is`.hi.darts2.R
import `is`.hi.darts2.model.Game

class SetupGamesAdapter(
    private val onSetupClicked: (Game) -> Unit
) : ListAdapter<Game, SetupGamesAdapter.SetupGameViewHolder>(SetupGameDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SetupGameViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.setup_game_list_item, parent, false)
        return SetupGameViewHolder(view)
    }

    override fun onBindViewHolder(holder: SetupGameViewHolder, position: Int) {
        val game = getItem(position)
        holder.bind(game)
    }

    inner class SetupGameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val gameDetailsTextView: TextView = itemView.findViewById(R.id.gameDetailsTextView)
        private val setupButton: Button = itemView.findViewById(R.id.setupButton)

        fun bind(game: Game) {
            gameDetailsTextView.text = "Game ${game.id}: ${game.gameType}"
            setupButton.setOnClickListener {
                onSetupClicked(game)
            }
        }
    }
}

class SetupGameDiffCallback : DiffUtil.ItemCallback<Game>() {
    override fun areItemsTheSame(oldItem: Game, newItem: Game): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Game, newItem: Game): Boolean {
        return oldItem == newItem
    }
}
