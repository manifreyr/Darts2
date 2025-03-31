package `is`.hi.darts2.ui.game

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import `is`.hi.darts2.model.Game
import `is`.hi.darts2.model.User
import `is`.hi.darts2.repository.GameRepository
import `is`.hi.darts2.repository.UserRepository
import kotlinx.coroutines.launch
import okhttp3.WebSocket
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent
import ua.naiksoftware.stomp.dto.StompMessage


class GameViewModel : ViewModel() {
    // Holds the current game.
    private val gameRepository: GameRepository = GameRepository()
    private val userRepository: UserRepository = UserRepository()
    private val _currentGame = MutableLiveData<Game>()
    val currentGame: LiveData<Game> = _currentGame
    private var _gameId: Long? = null

    var fetchingGame = 0

    private val _currentUser = MutableLiveData<User>()
    val currentUser: LiveData<User> get() = _currentUser

    val friendsList = MutableLiveData<List<User>>()

    private lateinit var stompClient: StompClient

    init {
        _currentUser.observeForever { user ->
            if (user != null) {
                connectStomp(user.id.toString())
            }
        }
    }

    fun initializeLocationService(context: Context) {
        locationService = LocationService(context)
    }

    /**
     * Uses LocationService to fetch the current location and then calls the repository
     * to update the player's location.
     */
    fun updatePlayerLocation() {
        if (!::locationService.isInitialized) {
            Log.e("GameViewModel", "LocationService is not initialized!")
            return
        }
        locationService.getLocation { location ->
            if (location != null) {
                viewModelScope.launch {
                    try {
                        val gameId = _currentGame.value?.id ?: return@launch
                        val playerId = currentUser.value?.id ?: return@launch
                        val response = gameRepository.setPlayerLocation(
                            gameId,
                            playerId,
                            location.latitude,
                            location.longitude
                        )
                        if (response.isSuccessful) {
                            Log.d("GameViewModel", "Player location updated successfully.")
                        } else {
                            Log.e(
                                "GameViewModel",
                                "Failed to update location: ${response.errorBody()?.string()}"
                            )
                        }
                    } catch (e: Exception) {
                        Log.e("GameViewModel", "Exception updating player location", e)
                    }
                }
            } else {
                Log.e("GameViewModel", "Location is unavailable.")
            }
        }
    }


    /**
     * Fetches the list of friends and updates the friendsList LiveData.
     */
    fun fetchFriends() {
        viewModelScope.launch {
            try {
                val response = userRepository.getFriends()
                friendsList.value =
                    if (response.isSuccessful) response.body() ?: emptyList() else emptyList()
            } catch (e: Exception) {
                friendsList.value = emptyList()
            }
        }
    }

    /**
     * Invites a friend to the current game.
     * @param friendId The ID of the friend to invite.
     */
    fun inviteFriend(friendId: Long) {
        viewModelScope.launch {
            try {
                val response =
                    currentGame.value?.let { gameRepository.inviteFriendToGame(it.id, friendId) }
                if (response != null) {
                    if (response.isSuccessful) {

                    }
                }
            } catch (e: Exception) {
            }
        }
    }

    /**
     * Sets the game ID.
     * @param id The game ID.
     */
    fun setGameId(id: Long) {
        _gameId = id
    }

    /**
     * Fetches the game details by ID.
     * @param gameId The ID of the game to fetch.
     */
    fun fetchGame(gameId: Long) {
        viewModelScope.launch {
            try {
                val game = gameRepository.getGameById(gameId)
                fetchingGame = 0
                _currentGame.value = game.body()
            } catch (e: Exception) {
            }
        }
    }

    /**
     * Updates the total number of legs in the game.
     * @param totalLegs The total legs to update.
     */
    fun updateTotalLegs(totalLegs: Long) {
        val gameId = _currentGame.value?.id ?: return
        viewModelScope.launch {
            try {
                val response = gameRepository.updateTotalLegs(gameId, totalLegs)
            } catch (e: Exception) {
            }
        }
    }

    /**
     * Submits a dart throw.
     * @param score The score of the throw.
     */
    fun submitThrow(score: Long) {
        val gameId = _currentGame.value?.id ?: return
        viewModelScope.launch {
            try {
                val response = gameRepository.submitThrow(gameId, score)
                //TODO: display error message
            } catch (e: Exception) {
            }
        }
    }

    /**
     * Starts the game.
     */
    fun startGame() {
        val gameId = _currentGame.value?.id ?: return
        viewModelScope.launch {
            try {
                val response = gameRepository.startGame(gameId)
                if (response.isSuccessful) {
                }
            } catch (e: Exception) {
            }
        }
    }

    /**
     * Updates the game type.
     * @param gameTypeValue The new game type value.
     */
    fun updateGameType(gameTypeValue: Long) {
        val gameId = _currentGame.value?.id ?: return
        viewModelScope.launch {
            try {
                val response = gameRepository.updateGameType(gameId, gameTypeValue)
                if (response.isSuccessful) {
                }
            } catch (e: Exception) {
            }
        }
    }

    /**
     * Fetches the current user data.
     */
    fun fetchCurrentUser() {
        viewModelScope.launch {
            try {
                val response = userRepository.getCurrentUser()
                if (response.isSuccessful) {
                    response.body()?.let { user ->
                        _currentUser.value = user
                    }
                }
            } catch (e: Exception) {
            }
        }
    }

    /**
     * Gets the best leg (fewest darts) for a player.
     * @param playerId The ID of the player.
     * @return The best leg as a string.
     */
    fun getBestLegForPlayer(playerId: Long): String {
        val game = _currentGame.value ?: return "-"
        val bestLeg = game.legs
            .filter { it.winnerPlayerId == playerId }
            .minOfOrNull { leg ->
                val startIndex = leg.startIndex
                val endIndex = leg.endIndex ?: return@minOfOrNull Long.MAX_VALUE
                game.rounds
                    .subList(startIndex, endIndex + 1)
                    .count { it.playerId == playerId } * 3L
            }
        return bestLeg?.toString() ?: "-"
    }

    /**
     * Gets the worst leg (most darts) for a player.
     * @param playerId The ID of the player.
     * @return The worst leg as a string.
     */
    fun getWorstLegForPlayer(playerId: Long): String {
        val game = _currentGame.value ?: return "-"
        val worstLeg = game.legs
            .filter { it.winnerPlayerId == playerId }
            .maxOfOrNull { leg ->
                val startIndex = leg.startIndex
                val endIndex = leg.endIndex ?: return@maxOfOrNull Long.MIN_VALUE
                game.rounds
                    .subList(startIndex, endIndex + 1)
                    .count { it.playerId == playerId } * 3L
            }
        return worstLeg?.toString() ?: "-"
    }

    /**
     * Gets the highest score for a player.
     * @param playerId The ID of the player.
     * @return The highest score.
     */
    fun getHighestScoreForPlayer(playerId: Long): Int {
        val game = _currentGame.value ?: return 0
        return game.rounds.filter { it.playerId == playerId }
            .maxOfOrNull { it.playerScore } ?: 0
    }

    /**
     * Gets the first 9-dart average score for a player.
     * @param playerId The ID of the player.
     * @return The first 9 dart average score.
     */
    fun getGameFirst9Average(playerId: Long): Double {
        val game = _currentGame.value ?: return 0.0
        val first3Rounds = game.rounds.filter { it.playerId == playerId }.take(3)
        val totalScore = first3Rounds.sumOf { it.playerScore.toDouble() }
        return if (first3Rounds.isNotEmpty()) totalScore / first3Rounds.size else 0.0
    }

    /**
     * Gets the three-dart average score for a player.
     * @param playerId The ID of the player.
     * @return The 3 dart average score.
     */
    fun getGameThreeDartAverage(playerId: Long): Double {
        val game = _currentGame.value ?: return 0.0
        val playerRounds = game.rounds.filter { it.playerId == playerId }
        val totalScore = playerRounds.sumOf { it.playerScore.toDouble() }
        val totalRounds = playerRounds.size
        return if (totalRounds > 0) totalScore / totalRounds else 0.0
    }


    /**
     * Connects to the WebSocket using Stomp.
     * @param userId The user ID for subscription.
     */
    fun connectStomp(userId: String) {
        val url = "ws://10.0.2.2:8081/game-websocket"
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, url)

        val subscribe = stompClient.lifecycle().subscribe({ lifecycleEvent ->
            when (lifecycleEvent.type) {
                LifecycleEvent.Type.OPENED -> {
                    Log.d("Stomp", "Stomp connection opened")
                    subscribeToUserTopic(userId)
                }

                LifecycleEvent.Type.ERROR -> {
                    Log.e("Stomp", "Stomp connection error", lifecycleEvent.exception)
                }

                LifecycleEvent.Type.CLOSED -> {
                    Log.d("Stomp", "Stomp connection closed")
                }

                else -> {}
            }
        }, { error ->
            Log.e("Stomp", "Lifecycle subscription error", error)
        })

        stompClient.connect()
    }

    /**
     * Subscribes to the user's game updates topic.
     * @param userId The user ID for subscription.
     */
    private fun subscribeToUserTopic(userId: String) {
        val subscribe = stompClient.topic("/topic/game-updates/$userId")
            .subscribe({ stompMessage: StompMessage ->
                Log.d("Stomp", "Received message: ${stompMessage.payload}")
                if (stompMessage.payload == "GAME_UPDATED" && _gameId != null) {
                    fetchGame(_gameId!!)
                }
            }, { error ->
                Log.e("Stomp", "Error in topic subscription", error)
            })
    }

    /**
     * Cleans up WebSocket connection.
     */
    override fun onCleared() {
        stompClient.disconnect()
        super.onCleared()
    }

}
