package `is`.hi.darts2.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import `is`.hi.darts2.model.User
import `is`.hi.darts2.repository.UserRepository
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val repository: UserRepository = UserRepository()
) : ViewModel() {

    // LiveData for the current user
    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> get() = _user

    // LiveData for the friends list
    private val _friends = MutableLiveData<List<User>>()
    val friends: LiveData<List<User>> get() = _friends

    // LiveData for toast messages
    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> get() = _toastMessage

    fun fetchUser() {
        viewModelScope.launch {
            try {
                val response = repository.getCurrentUser()  // Implement in your repository.
                if (response.isSuccessful) {
                    _user.value = response.body()
                } else {
                    Log.d("Profile", "got current user")
                    _toastMessage.value = "Failed to fetch user: ${response.code()}"
                }
            } catch (e: Exception) {
                _toastMessage.value = "Error: ${e.message}"
                Log.d("Profile", "failed to get current user")
            }
        }
    }

    fun fetchFriends() {
        viewModelScope.launch {
            try {
                val response = repository.getFriends()  // GET /friends endpoint.
                if (response.isSuccessful) {
                    _friends.value = response.body() ?: emptyList()
                } else {
                    _toastMessage.value = "Failed to fetch friends: ${response.code()}"
                }
            } catch (e: Exception) {
                _toastMessage.value = "Error: ${e.message}"
            }
        }
    }

    fun unfriend(friendId: Long) {
        viewModelScope.launch {
            try {
                val response =
                    repository.removeFriend(friendId)  // Implement removal in your repository.
                if (response.isSuccessful) {
                    _toastMessage.value = response.body()?.message ?: "Friend removed"
                    // Refresh the friends list after removal.
                    fetchFriends()
                } else {
                    _toastMessage.value = "Failed to remove friend: ${response.code()}"
                }
            } catch (e: Exception) {
                _toastMessage.value = "Error: ${e.message}"
            }
        }
    }
}
