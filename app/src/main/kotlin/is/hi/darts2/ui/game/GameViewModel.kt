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
}
