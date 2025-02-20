package `is`.hi.darts2.ui.friends

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import `is`.hi.darts2.R
import `is`.hi.darts2.model.FriendRequest

class FriendRequestDiffCallback : DiffUtil.ItemCallback<FriendRequest>() {
    override fun areItemsTheSame(oldItem: FriendRequest, newItem: FriendRequest): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: FriendRequest, newItem: FriendRequest): Boolean {
        return oldItem == newItem
    }
}

class FriendRequestAdapter(
    private val onAcceptClicked: (Long) -> Unit,
    private val onDeclineClicked: (Long) -> Unit
) : ListAdapter<FriendRequest, FriendRequestAdapter.FriendRequestViewHolder>(
    FriendRequestDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendRequestViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.friend_request_list_item, parent, false)
        return FriendRequestViewHolder(view)
    }

    override fun onBindViewHolder(holder: FriendRequestViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class FriendRequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val senderTextView: TextView = itemView.findViewById(R.id.senderTextView)
        private val acceptButton: Button = itemView.findViewById(R.id.acceptButton)
        private val declineButton: Button = itemView.findViewById(R.id.declineButton)

        fun bind(request: FriendRequest) {
            senderTextView.text = "${request.requester.username}"
            acceptButton.setOnClickListener { onAcceptClicked(request.id) }
            declineButton.setOnClickListener { onDeclineClicked(request.id) }
        }
    }
}
