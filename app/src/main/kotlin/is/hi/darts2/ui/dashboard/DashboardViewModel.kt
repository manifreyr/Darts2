package `is`.hi.darts2.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import `is`.hi.darts2.repository.GameRepository
import `is`.hi.darts2.model.Game
import `is`.hi.darts2.model.GameInvite
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val gameRepository: GameRepository = GameRepository()
) : ViewModel() {

    private val _gameInvites = MutableLiveData<List<GameInvite>>()
    val gameInvites: LiveData<List<GameInvite>> = _gameInvites

    private val _setupGames = MutableLiveData<List<Game>>()
    val setupGames: LiveData<List<Game>> = _setupGames

    private val _ongoingGames = MutableLiveData<List<Game>>()
    val ongoingGames: LiveData<List<Game>> = _ongoingGames

    // Navigation event to indicate we should navigate to the game screen with a specific game ID.
    private val _navigateToGameScreen = MutableLiveData<Long?>()
    val navigateToGameScreen: LiveData<Long?> get() = _navigateToGameScreen

    fun createNewGame(onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            try {
                val response = gameRepository.createNewGame()
                if (response.isSuccessful) {
                    val game = response.body()
                    if (game != null) {
                        onResult(true, "Game created successfully")
                        // Trigger navigation by posting the game ID.
                        navigateToGameScreen(game.id)
                    } else {
                        onResult(false, "Game creation failed: empty response")
                    }
                } else {
                    onResult(false, "Game creation failed: error code ${response.code()}")
                }
            } catch (e: Exception) {
                onResult(false, "Exception: ${e.message}")
            }
        }
    }

    fun navigateToGameScreen(gameId: Long) {
        _navigateToGameScreen.value = gameId
    }

    fun onSetupGameClicked(game: Game) {
        navigateToGameScreen(game.id)
    }

    fun fetchGameInvites(onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                val response = gameRepository.getGameInvites()
                _gameInvites.value =
                    if (response.isSuccessful) response.body() ?: emptyList() else emptyList()
            } catch (e: Exception) {
                _gameInvites.value = emptyList()
            } finally {
                onComplete()
            }
        }
    }

    fun acceptGameInvite(inviteId: Long) {
        viewModelScope.launch {
            try {
                val response = gameRepository.acceptGameInvite(inviteId)
                if (response.isSuccessful) {
                    // Remove the accepted invite from the list.
                    _gameInvites.value = _gameInvites.value?.filter { it.id != inviteId }
                    // Get the game from the response.
                    val game = response.body()
                    if (game != null) {
                        // Trigger navigation by posting the game ID.
                        _navigateToGameScreen.value = game.id
                    }
                }
            } catch (e: Exception) {
                // Handle exception if needed
            }
        }
    }

    fun declineGameInvite(inviteId: Long) {
        viewModelScope.launch {
            try {
                val response = gameRepository.declineGameInvite(inviteId)
                if (response.isSuccessful) {
                    _gameInvites.value = _gameInvites.value?.filter { it.id != inviteId }
                }
            } catch (e: Exception) {
                // Handle exception if needed
            }
        }
    }

    // Fetch setup games list for the current user.
    fun fetchSetupGames(onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                val response = gameRepository.getSetupGames()
                Log.d("fetchSetupGames", response.body().toString())
                _setupGames.value =
                    if (response.isSuccessful) response.body() ?: emptyList() else emptyList()
            } catch (e: Exception) {
                Log.d("fetchSetupGames", "Error while getting setup games: " + e.message)
                _setupGames.value = emptyList()
            } finally {
                onComplete()
            }
        }
    }

    // Fetch ongoing games list for the current user.
    fun fetchOngoingGames(onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                val response = gameRepository.getOngoingGames()
                _ongoingGames.value =
                    if (response.isSuccessful) response.body() ?: emptyList() else emptyList()
            } catch (e: Exception) {
                _ongoingGames.value = emptyList()
            } finally {
                onComplete()
            }
        }
    }

    /**
     * After the UI handles the navigation event, call this function to clear the event
     * so it doesn't trigger navigation again after configuration changes.
     */
    fun clearNavigationEvent() {
        _navigateToGameScreen.value = null
    }
}
