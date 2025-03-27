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

    suspend fun getGameById(gameId: Long): Response<Game> {
        return apiService.getGameById(gameId)
    }

    suspend fun inviteFriendToGame(gameId: Long, friendId: Long): Response<MessageResponse> {
        return apiService.inviteFriendToGame(gameId, friendId)
    }

    suspend fun updateTotalLegs(gameId: Long, totalLegs: Long): Response<Game> {
        return apiService.updateTotalLegs(gameId, totalLegs)
    }

    suspend fun updateGameType(gameId: Long, gameTypeValue: Long): Response<Game> {
        return apiService.updateGameType(gameId, gameTypeValue)
    }

    suspend fun startGame(gameId: Long): Response<MessageResponse> {
        return apiService.startGame(gameId)
    }
}
