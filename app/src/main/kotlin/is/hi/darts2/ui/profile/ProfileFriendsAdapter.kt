package `is`.hi.darts2.ui.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import `is`.hi.darts2.model.User
import `is`.hi.darts2.R

class ProfileFriendsAdapter(
    private val onUnfriendClick: (User) -> Unit
) : ListAdapter<User, ProfileFriendsAdapter.FriendViewHolder>(UserDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.profile_friend_list_item, parent, false)
        return FriendViewHolder(view, onUnfriendClick)
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class FriendViewHolder(
        itemView: View,
        private val onUnfriendClick: (User) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val friendNameTextView: TextView = itemView.findViewById(R.id.friendNameTextView)
        private val unfriendButton: Button = itemView.findViewById(R.id.unfriendButton)

        fun bind(user: User) {
            friendNameTextView.text = user.username
            unfriendButton.setOnClickListener {
                onUnfriendClick(user)
            }
        }
    }
}

class UserDiffCallback : DiffUtil.ItemCallback<User>() {

    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean =
        oldItem == newItem
}