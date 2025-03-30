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

    fun setGameId(id: Long) {
        _gameId = id
    }

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


    fun updateTotalLegs(totalLegs: Long) {
        val gameId = _currentGame.value?.id ?: return
        viewModelScope.launch {
            try {
                val response = gameRepository.updateTotalLegs(gameId, totalLegs)
                if (response.isSuccessful) {
                    //response.body()?.let { updatedGame ->
                    //    _currentGame.value = updatedGame
                    //}
                }
            } catch (e: Exception) {
            }
        }
    }

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


    fun getBestLegForPlayer(playerId: Long): Long {
        val game = _currentGame.value ?: return 0
        return game.legs.filter { it.winnerPlayerId == playerId }
            .minOfOrNull { leg ->
                val startIndex = leg.startIndex
                val endIndex = leg.endIndex ?: return@minOfOrNull Long.MAX_VALUE
                game.rounds
                    .subList(startIndex, endIndex + 1)
                    .count { it.playerId == playerId } * 3L
            } ?: 0
    }

    fun getWorstLegForPlayer(playerId: Long): Long {
        val game = _currentGame.value ?: return 0
        return game.legs.filter { it.winnerPlayerId == playerId }
            .maxOfOrNull { leg ->
                val startIndex = leg.startIndex
                val endIndex = leg.endIndex ?: return@maxOfOrNull Long.MIN_VALUE
                game.rounds
                    .subList(startIndex, endIndex + 1)
                    .count { it.playerId == playerId } * 3L
            } ?: 0
    }

    fun getHighestScoreForPlayer(playerId: Long): Int {
        val game = _currentGame.value ?: return 0
        return game.rounds.filter { it.playerId == playerId }
            .maxOfOrNull { it.playerScore } ?: 0
    }

    fun getGameFirst9Average(playerId: Long): Double {
        val game = _currentGame.value ?: return 0.0
        val first3Rounds = game.rounds.filter { it.playerId == playerId }.take(3)
        val totalScore = first3Rounds.sumOf { it.playerScore.toDouble() }
        return if (first3Rounds.isNotEmpty()) totalScore / first3Rounds.size else 0.0
    }

    fun getGameThreeDartAverage(playerId: Long): Double {
        val game = _currentGame.value ?: return 0.0
        val playerRounds = game.rounds.filter { it.playerId == playerId }
        val totalScore = playerRounds.sumOf { it.playerScore.toDouble() }
        val totalRounds = playerRounds.size
        return if (totalRounds > 0) totalScore / totalRounds else 0.0
    }

    fun connectStomp(userId: String) {
        val url = "ws://10.0.2.2:8081/game-websocket"
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, url)

        // Observe the lifecycle events of the STOMP client
        val subscribe = stompClient.lifecycle().subscribe({ lifecycleEvent ->
            when (lifecycleEvent.type) {
                LifecycleEvent.Type.OPENED -> {
                    Log.d("Stomp", "Stomp connection opened")
                    // Now that we're connected, subscribe to the topic specific to this user.
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

    private fun subscribeToUserTopic(userId: String) {
        // Subscribe to the topic with the user ID in the destination.
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


    override fun onCleared() {
        stompClient.disconnect()
        super.onCleared()
    }
}
