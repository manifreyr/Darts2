package `is`.hi.darts2.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import `is`.hi.darts2.model.FriendRequest
import `is`.hi.darts2.repository.UserRepository
import kotlinx.coroutines.launch

class FriendsViewModel(
    private val repository: UserRepository = UserRepository()
) : ViewModel() {

    private val _friendRequests = MutableLiveData<List<FriendRequest>>()
    val friendRequests: LiveData<List<FriendRequest>> = _friendRequests

    fun fetchFriendRequests(onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            val response = repository.getIncomingFriendRequests()
            if (response.isSuccessful) {
                _friendRequests.value = response.body() ?: emptyList()
            } else {
                Log.d("Friend Requests", "Failed to get friend requests")
                _friendRequests.value = emptyList()
            }
            onComplete();
        }
    }

    fun sendFriendRequest(identifier: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            val response = repository.addFriend(identifier)
            val success = response.isSuccessful
            val message = response.body()?.message ?: ""
            Log.d("Send Friend Request", message)
            onResult(success, message)
        }
    }

    fun acceptFriendRequest(requestId: Long) {
        viewModelScope.launch {
            val success = repository.acceptFriendRequest(requestId).isSuccessful
            if (success) {
                _friendRequests.value = _friendRequests.value?.filter { it.id != requestId }
            }
        }
    }

    fun declineFriendRequest(requestId: Long) {
        viewModelScope.launch {
            val success = repository.declineFriendRequest(requestId).isSuccessful
            if (success) {
                _friendRequests.value = _friendRequests.value?.filter { it.id != requestId }
            }
        }
    }
}
