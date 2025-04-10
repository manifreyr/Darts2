package `is`.hi.darts2.ui.start

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import `is`.hi.darts2.R
import `is`.hi.darts2.data.LocalLoginDataStore
import `is`.hi.darts2.ui.MainActivity
import `is`.hi.darts2.ui.login.LoginFragment
import `is`.hi.darts2.ui.dashboard.DashboardFragment
import `is`.hi.darts2.ui.register.RegisterFragment
import `is`.hi.darts2.viewmodel.LoginViewModel
import kotlinx.coroutines.launch

class StartFragment : Fragment() {

    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_start, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycleScope.launch {
            val localDataStore = LocalLoginDataStore(requireContext())
            val savedUsername = localDataStore.getUsername()
            val savedPassword = localDataStore.getPassword()

            if (!savedUsername.isNullOrEmpty() && !savedPassword.isNullOrEmpty()) {
                // Attempt login with saved credentials
                loginViewModel.login(savedUsername, savedPassword) { user ->
                    if (user != null) {
                        (activity as? MainActivity)?.showNavigation()
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, DashboardFragment())
                            .commit()
                    }
                }
            }
        }

        val loginButton = view.findViewById<Button>(R.id.loginButton)
        val registerButton = view.findViewById<Button>(R.id.registerButton)

        loginButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, LoginFragment())
                .addToBackStack(null)
                .commit()
        }

        registerButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, RegisterFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}
