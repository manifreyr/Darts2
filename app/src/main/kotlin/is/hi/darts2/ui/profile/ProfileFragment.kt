package `is`.hi.darts2.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import `is`.hi.darts2.R
import `is`.hi.darts2.viewmodel.ProfileViewModel

class ProfileFragment : Fragment() {

    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val usernameTextView = view.findViewById<TextView>(R.id.usernameTextView)
        val emailTextView = view.findViewById<TextView>(R.id.emailTextView)
        val idTextView = view.findViewById<TextView>(R.id.idTextView)
        val friendsRecyclerView = view.findViewById<RecyclerView>(R.id.friendsRecyclerView)

        // Set up the RecyclerView
        friendsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        val adapter = ProfileFriendsAdapter { friend ->
            // Unfriend button click handler.
            viewModel.unfriend(friend.id)
        }
        friendsRecyclerView.adapter = adapter

        // Observe the current user info
        viewModel.user.observe(viewLifecycleOwner) { user ->
            user?.let {
                usernameTextView.text = it.displayName  // or it.displayName if available
                emailTextView.text = it.username
                idTextView.text = "User id: " + it.id.toString()
            }
        }

        // Observe the friends list
        viewModel.friends.observe(viewLifecycleOwner) { friends ->
            adapter.submitList(friends)
        }

        // Observe toast messages for feedback
        viewModel.toastMessage.observe(viewLifecycleOwner) { message ->
            message?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }

        // Fetch user info and friends on fragment start.
        viewModel.fetchUser()
        viewModel.fetchFriends()
    }
}
