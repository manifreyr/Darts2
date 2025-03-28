package `is`.hi.darts2.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import `is`.hi.darts2.model.Game
import `is`.hi.darts2.model.GameStatus
import `is`.hi.darts2.model.User
import `is`.hi.darts2.repository.GameRepository
import `is`.hi.darts2.repository.UserRepository
import kotlinx.coroutines.launch

class GameViewModel : ViewModel() {
    // Holds the current game.
    private val gameRepository: GameRepository = GameRepository()
    private val userRepository: UserRepository = UserRepository()
    private val _currentGame = MutableLiveData<Game>()
    val currentGame: LiveData<Game> = _currentGame
    private var _gameId: Long? = null

    private val _currentUser = MutableLiveData<User>()
    val currentUser: LiveData<User> get() = _currentUser

    val friendsList = MutableLiveData<List<User>>()

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
                // Handle the error.
            }
        }
    }

    fun setGameId(id: Long) {
        _gameId = id
    }

    fun fetchGame(gameId: Long) {
        // Use your preferred async method. For example, using Coroutines:
        viewModelScope.launch {
            try {
                val game = gameRepository.getGameById(gameId)
                _currentGame.value = game.body()
            } catch (e: Exception) {
                // Handle error, e.g., post a null value or update an error LiveData
            }
        }
    }

    fun updateGame(game: Game) {
        _currentGame.value = game
    }

    val gameStatus: LiveData<GameStatus> = MutableLiveData<GameStatus>().apply {
        _currentGame.observeForever { game ->
            value = game.status
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
                    fetchGame(gameId)
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
                    //response.body()?.let { updatedGame ->
                    //    _currentGame.value = updatedGame
                    //}
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
                // Handle error.
            }
        }
    }

    fun displayStats() {
        val gameId = _currentGame.value?.id ?: return
        viewModelScope.launch {
            try {
                val response = gameRepository.displayStats(gameId)
                if (response.isSuccessful) {

                    //_gameStats.value = response.body()
                } else {
                  //  _gameStats.value = null
                }
            } catch (e: Exception) {
                // error
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
}
