package `is`.hi.darts2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import `is`.hi.darts2.model.User
import `is`.hi.darts2.repository.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository = UserRepository()) : ViewModel() {

    fun login(email: String, password: String, onResult: (User?) -> Unit) {
        viewModelScope.launch {
            val response = repository.login(email, password)
            if (response.isSuccessful) {
                onResult(response.body())
            } else {
                onResult(null)
            }
        }
    }
}
