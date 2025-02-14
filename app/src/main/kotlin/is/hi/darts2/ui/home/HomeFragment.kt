package `is`.hi.darts2.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import `is`.hi.darts2.R

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the home fragment layout
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Retrieve the username passed in via arguments
        val username = arguments?.getString(ARG_USERNAME) ?: "Unknown User"
        // Find the TextView and set the text
        val usernameTextView = view.findViewById<TextView>(R.id.homeUsernameTextView)
        usernameTextView.text = username
    }

    companion object {
        private const val ARG_USERNAME = "username"

        // Helper method to create a new instance of HomeFragment with the username as an argument.
        fun newInstance(username: String): HomeFragment {
            val fragment = HomeFragment()
            val args = Bundle()
            args.putString(ARG_USERNAME, username)
            fragment.arguments = args
            return fragment
        }
    }
}
