package `is`.hi.darts2.ui.friends

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import `is`.hi.darts2.viewmodel.FriendsViewModel
import `is`.hi.darts2.R

class FriendsFragment : Fragment() {

    private val friendsViewModel: FriendsViewModel by viewModels()
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var friendRequestAdapter: FriendRequestAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_friends, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val identifierEditText = view.findViewById<EditText>(R.id.identifierEditText)
        val sendButton = view.findViewById<Button>(R.id.sendFriendRequestButton)
        swipeRefreshLayout = view.findViewById(R.id.friendRequestsContainer)
        val recyclerView = view.findViewById<RecyclerView>(R.id.friendRequestsRecyclerView)

        friendRequestAdapter = FriendRequestAdapter(
            onAcceptClicked = { requestId ->
                friendsViewModel.acceptFriendRequest(requestId)
            },
            onDeclineClicked = { requestId ->
                friendsViewModel.declineFriendRequest(requestId)
            }
        )
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = friendRequestAdapter

        friendsViewModel.friendRequests.observe(viewLifecycleOwner) { requests ->
            friendRequestAdapter.submitList(requests)
        }

        sendButton.setOnClickListener {
            val identifier = identifierEditText.text.toString().trim()
            if (identifier.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter an identifier", Toast.LENGTH_SHORT)
                    .show()
            } else {
                friendsViewModel.sendFriendRequest(identifier) { success, message ->
                    if (success) {
                        Toast.makeText(requireContext(), "Friend request sent", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        swipeRefreshLayout.setOnRefreshListener {
            friendsViewModel.fetchFriendRequests {
                swipeRefreshLayout.isRefreshing = false
            }
        }

        friendsViewModel.fetchFriendRequests()
    }
}
