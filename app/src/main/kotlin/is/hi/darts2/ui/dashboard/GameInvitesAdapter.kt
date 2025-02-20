package `is`.hi.darts2.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import `is`.hi.darts2.model.GameInvite
import `is`.hi.darts2.R

class GameInvitesAdapter(
    private val onAcceptClicked: (GameInvite) -> Unit,
    private val onDeclineClicked: (GameInvite) -> Unit
) : ListAdapter<GameInvite, GameInvitesAdapter.GameInviteViewHolder>(GameInviteDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameInviteViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.game_invite_list_item, parent, false)
        return GameInviteViewHolder(view)
    }

    override fun onBindViewHolder(holder: GameInviteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class GameInviteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val inviteDetailsTextView: TextView =
            itemView.findViewById(R.id.inviteDetailsTextView)
        private val acceptButton: Button = itemView.findViewById(R.id.acceptButton)
        private val declineButton: Button = itemView.findViewById(R.id.declineButton)

        fun bind(invite: GameInvite) {
            inviteDetailsTextView.text = "Invite from ${invite.userId}"
            acceptButton.setOnClickListener {
                onAcceptClicked(invite)
            }
            declineButton.setOnClickListener {
                onDeclineClicked(invite)
            }
        }
    }
}

class GameInviteDiffCallback : DiffUtil.ItemCallback<GameInvite>() {
    override fun areItemsTheSame(oldItem: GameInvite, newItem: GameInvite): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: GameInvite, newItem: GameInvite): Boolean {
        return oldItem == newItem
    }
}
