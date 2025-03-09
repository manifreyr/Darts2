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
    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> get() = _user
    private val _friends = MutableLiveData<List<User>>()
    val friends: LiveData<List<User>> get() = _friends

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> get() = _toastMessage
    fun fetchUser() {
        viewModelScope.launch {
            try {
                val response = repository.getCurrentUser()
                if (response.isSuccessful) {
                    _user.value = response.body()
                } else {
                    Log.d("Profile", "got current user")
                }
            } catch (e: Exception) {
                Log.d("Profile", "failed to get current user")
            }
        }
    }

    fun fetchFriends() {
        viewModelScope.launch {
            try {
                val response = repository.getFriends()
                if (response.isSuccessful) {
                    _friends.value = response.body() ?: emptyList()
                } else {
                    Log.d("Profile", "failed to get current user ")
                }
            } catch (e: Exception) {
                Log.d("Profile", "Error ")
            }
        }
    }

    fun unfriend(friendId: Long) {
        viewModelScope.launch {
            try {
                val response =
                    repository.removeFriend(friendId)
                if (response.isSuccessful) {
                    Log.d("Profile", "removed friend")
                    fetchFriends()
                } else {
                    Log.d("Profile", "failed to remove friend")
                }
            } catch (e: Exception) {
                Log.d("Profile", "failed to remove friend" + e.message)
            }
        }
    }
}
