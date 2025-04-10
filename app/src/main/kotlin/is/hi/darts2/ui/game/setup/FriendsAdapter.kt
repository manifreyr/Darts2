package `is`.hi.darts2.ui.game.setup

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import `is`.hi.darts2.R
import `is`.hi.darts2.model.User

class FriendsAdapter(
    private val onInviteClick: (User) -> Unit
) : ListAdapter<User, FriendsAdapter.FriendViewHolder>(UserDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.game_friend_list_item, parent, false)
        return FriendViewHolder(view, onInviteClick)
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class FriendViewHolder(itemView: View, private val onInviteClick: (User) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        private val friendNameTextView: TextView = itemView.findViewById(R.id.friendNameTextView)
        private val inviteButton: Button = itemView.findViewById(R.id.inviteButton)

        fun bind(user: User) {
            friendNameTextView.text = user.displayName
            inviteButton.setOnClickListener {
                inviteButton.text = "Invited"
                onInviteClick(user)
            }
        }
    }
}

class UserDiffCallback : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem == newItem
    }
}

