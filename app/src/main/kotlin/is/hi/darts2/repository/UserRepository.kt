package `is`.hi.darts2.repository

import `is`.hi.darts2.model.FriendRequest
import `is`.hi.darts2.model.MessageResponse
import `is`.hi.darts2.model.User
import `is`.hi.darts2.network.ApiService
import `is`.hi.darts2.network.Network
import retrofit2.Response

class UserRepository(private val apiService: ApiService = Network.apiService) {

    suspend fun login(email: String, password: String): Response<User> {
        return apiService.login(email, password)
    }

    suspend fun register(email: String, username: String, password: String): Response<User> {
        return apiService.register(email, username, password)
    }

    suspend fun addFriend(identifier: String): Response<MessageResponse> {
        return apiService.addFriend(identifier)
    }

    suspend fun getIncomingFriendRequests(): Response<List<FriendRequest>> {
        return apiService.getIncomingFriendRequests()
    }

    suspend fun acceptFriendRequest(requestId: Long): Response<MessageResponse> {
        return apiService.acceptFriendRequest(requestId)
    }

    suspend fun declineFriendRequest(requestId: Long): Response<MessageResponse> {
        return apiService.declineFriendRequest(requestId)
    }
}
