package `is`.hi.darts2.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import `is`.hi.darts2.R
import `is`.hi.darts2.data.LocalLoginDataStore
import `is`.hi.darts2.ui.MainActivity
import `is`.hi.darts2.ui.dashboard.DashboardFragment
import `is`.hi.darts2.ui.register.RegisterFragment
import `is`.hi.darts2.viewmodel.LoginViewModel
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val usernameEditText = view.findViewById<EditText>(R.id.usernameEditText)
        val passwordEditText = view.findViewById<EditText>(R.id.passwordEditText)
        val loginButton = view.findViewById<Button>(R.id.loginButton)
        val noAccountTextView = view.findViewById<TextView>(R.id.noAccountTextView)

        loginButton.setOnClickListener {
            val email = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT)
                    .show()
            } else {
                loginViewModel.login(email, password) { user ->
                    if (user != null) {
                        Toast.makeText(requireContext(), "Login successful", Toast.LENGTH_SHORT)
                            .show()

                        // Save credentials using SharedPreferences
                        val localDataStore = LocalLoginDataStore(requireContext())
                        lifecycleScope.launch {
                            localDataStore.saveCredentials(email, password)
                        }

                        // Optionally show the navigation bar
                        (activity as? MainActivity)?.showNavigation()

                        parentFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, DashboardFragment())
                            .commit()
                    } else {
                        Toast.makeText(requireContext(), "Login failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        noAccountTextView.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, RegisterFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}
