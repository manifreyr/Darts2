package `is`.hi.darts2.network

import android.os.Message
import `is`.hi.darts2.model.FriendRequest
import `is`.hi.darts2.model.Game
import `is`.hi.darts2.model.GameInvite
import `is`.hi.darts2.model.MessageResponse
import `is`.hi.darts2.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<User>

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("email") email: String,
        @Field("username") username: String,
        @Field("password") password: String
    ): Response<User>

    @FormUrlEncoded
    @POST("player/friends/add")
    suspend fun addFriend(
        @Field("identifier") identifier: String
    ): Response<MessageResponse>

    @POST("player/friends/incoming")
    suspend fun getIncomingFriendRequests(): Response<List<FriendRequest>>

    @FormUrlEncoded
    @POST("player/friends/requests/accept")
    suspend fun acceptFriendRequest(
        @Field("requestId") requestId: Long
    ): Response<MessageResponse>

    @FormUrlEncoded
    @POST("player/friends/requests/decline")
    suspend fun declineFriendRequest(
        @Field("requestId") requestId: Long
    ): Response<MessageResponse>

    @POST("games/") // Adjust the endpoint path as needed.
    suspend fun createNewGame(): Response<Game>


    @POST("games/setup")
    suspend fun getSetupGames(): Response<List<Game>>

    @POST("games/ongoing")
    suspend fun getOngoingGames(): Response<List<Game>>

    @POST("games/invites")
    suspend fun getGameInvites(): Response<List<GameInvite>>

    @FormUrlEncoded
    @POST("games/invites/accept")
    suspend fun acceptGameInvite(
        @Field("inviteId") inviteId: Long
    ): Response<Game>

    @FormUrlEncoded
    @POST("games/invites/decline")
    suspend fun declineGameInvite(
        @Field("inviteId") inviteId: Long
    ): Response<MessageResponse>

    @FormUrlEncoded
    @POST("games/get")
    suspend fun getGameById(
        @Field("gameId") gameId: Long
    ): Response<Game>

    @GET("player/friends")
    suspend fun getFriends(): Response<List<User>>

    @POST("games/{gameId}/invite")
    suspend fun inviteFriendToGame(
        @Path("gameId") gameId: Long,
        @Body friendId: Long
    ): Response<MessageResponse>


    @GET("player/current")
    suspend fun getCurrentUser(): Response<User>

    @DELETE("player/friends/{friendId}/remove")
    suspend fun removeFriend(
        @Path("friendId") friendId: Long
    ): Response<MessageResponse>

    @PUT("games/{gameId}/totalLegs")
    suspend fun updateTotalLegs(
        @Path("gameId") gameId: Long,
        @Body totalLegs: Long
    ): Response<Game>

    @PUT("games/{gameId}/gameType")
    suspend fun updateGameType(
        @Path("gameId") gameId: Long,
        @Body gameTypeValue: Long
    ): Response<Game>

    @POST("games/{gameId}/start")
    suspend fun startGame(
        @Path("gameId") gameId: Long
    ): Response<MessageResponse>

    @POST("games/{gameId}/stats")
    suspend fun displayStats(
        @Path("gameId") gameId: Long
    ): Response<MessageResponse>


    @POST("games/{gameId}/throws")
    suspend fun submitThrow(
        @Path("gameId") gameId: Long,
        @Body score: Long
    ): Response<MessageResponse>
}
