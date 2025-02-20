package `is`.hi.darts2.repository

import `is`.hi.darts2.model.Game
import `is`.hi.darts2.model.GameInvite
import `is`.hi.darts2.model.MessageResponse
import `is`.hi.darts2.network.ApiService
import `is`.hi.darts2.network.Network
import retrofit2.Response

class GameRepository(private val apiService: ApiService = Network.apiService) {

    suspend fun createNewGame(): Response<Game> {
        return apiService.createNewGame()
    }

    suspend fun getSetupGames(): Response<List<Game>> {
        return apiService.getSetupGames()
    }

    suspend fun getOngoingGames(): Response<List<Game>> {
        return apiService.getOngoingGames()
    }

    suspend fun getGameInvites(): Response<List<GameInvite>> {
        return apiService.getGameInvites()
    }
    
    suspend fun acceptGameInvite(inviteId: Long): Response<Game> {
        return apiService.acceptGameInvite(inviteId)
    }

    suspend fun declineGameInvite(inviteId: Long): Response<MessageResponse> {
        return apiService.declineGameInvite(inviteId)
    }
}
