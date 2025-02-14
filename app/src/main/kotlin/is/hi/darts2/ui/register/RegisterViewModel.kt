package `is`.hi.darts2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import `is`.hi.darts2.model.User
import `is`.hi.darts2.repository.UserRepository
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val repository: UserRepository = UserRepository()
) : ViewModel() {

    /**
     * Calls the register endpoint using the provided email, username, and password.
     * Executes [onResult] with the resulting [User] if registration is successful,
     * or null if it fails.
     */
    fun register(email: String, username: String, password: String, onResult: (User?) -> Unit) {
        viewModelScope.launch {
            try {
                val response = repository.register(email, username, password)
                if (response.isSuccessful) {
                    onResult(response.body())
                } else {
                    onResult(null)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                onResult(null)
            }
        }
    }
}
