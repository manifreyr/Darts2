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

    private val _createdGame = MutableLiveData<Game>()
    val createdGame: LiveData<Game> = _createdGame

    private val _gameInvites = MutableLiveData<List<GameInvite>>()
    val gameInvites: LiveData<List<GameInvite>> = _gameInvites

    private val _setupGames = MutableLiveData<List<Game>>()
    val setupGames: LiveData<List<Game>> = _setupGames

    private val _ongoingGames = MutableLiveData<List<Game>>()
    val ongoingGames: LiveData<List<Game>> = _ongoingGames

    fun createNewGame(onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            try {
                val response = gameRepository.createNewGame()
                if (response.isSuccessful) {
                    val game = response.body()
                    if (game != null) {
                        _createdGame.value = game
                        onResult(true, "Game created successfully")
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
                    _gameInvites.value = _gameInvites.value?.filter { it.id != inviteId }
                }
            } catch (e: Exception) {
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
            } catch (e: Exception) { /* Handle exception if needed */
            }
        }
    }

    // Fetch setup games list for the current user
    fun fetchSetupGames(onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                val response = gameRepository.getSetupGames()
                Log.d("fetchSetupGames", response.body().toString())
                _setupGames.value =
                    if (response.isSuccessful) response.body() ?: emptyList() else emptyList()
            } catch (e: Exception) {
                Log.d("fetchSetupGames", "Error while getting setup games" + e.message)
                _setupGames.value = emptyList()
            } finally {
                onComplete()
            }
        }
    }

    // Fetch ongoing games list for the current user
    fun fetchOngoingGames(onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                val response = gameRepository.getOngoingGames()
                _ongoingGames.value =
                    if (response.isSuccessful) response.body() ?: emptyList() else emptyList()
            } catch (e: Exception) {
                _ongoingGames.value = emptyList()
            } finally {
                onComplete();
            }
        }
    }
}
