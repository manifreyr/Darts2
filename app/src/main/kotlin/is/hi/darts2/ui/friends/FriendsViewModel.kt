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

    /**
     * Fetches incoming friend requests and updates the LiveData.
     * @param onComplete Callback function to be executed after fetching is complete.
     */
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

    /**
     * Sends a friend request to a user.
     * @param identifier The identifier of the user to send a request to.
     * @param onResult Callback function with success status and message.
     */
    fun sendFriendRequest(identifier: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            val response = repository.addFriend(identifier)
            val success = response.isSuccessful
            val message = response.body()?.message ?: ""
            Log.d("Send Friend Request", message)
            onResult(success, message)
        }
    }

    /**
     * Accepts a friend request.
     * @param requestId The ID of the friend request to accept.
     */
    fun acceptFriendRequest(requestId: Long) {
        viewModelScope.launch {
            val success = repository.acceptFriendRequest(requestId).isSuccessful
            if (success) {
                _friendRequests.value = _friendRequests.value?.filter { it.id != requestId }
            }
        }
    }

    /**
     * Declines a friend request.
     * @param requestId The ID of the friend request to decline.
     */
    fun declineFriendRequest(requestId: Long) {
        viewModelScope.launch {
            val success = repository.declineFriendRequest(requestId).isSuccessful
            if (success) {
                _friendRequests.value = _friendRequests.value?.filter { it.id != requestId }
            }
        }
    }
}
