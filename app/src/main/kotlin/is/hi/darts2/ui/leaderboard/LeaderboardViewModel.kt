package `is`.hi.darts2.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import `is`.hi.darts2.repository.UserRepository
import kotlinx.coroutines.launch

class LeaderboardViewModel(
    private val repository: UserRepository = UserRepository()
) : ViewModel() {

    // Using a list of maps since your API returns List<Map<String, Any>>
    private val _leaderboardEntries = MutableLiveData<List<Map<String, Any>>>()
    val leaderboardEntries: LiveData<List<Map<String, Any>>> get() = _leaderboardEntries

    fun fetchLeaderboardStats(onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                val response = repository.getLeaderboardStats()
                if (response.isSuccessful) {
                    _leaderboardEntries.value = response.body() ?: emptyList()
                } else {
                    Log.d("LeaderboardViewModel", "Failed to get leaderboard data")
                    _leaderboardEntries.value = emptyList()
                }
            } catch (e: Exception) {
                Log.e("LeaderboardViewModel", "Exception fetching leaderboard", e)
                _leaderboardEntries.value = emptyList()
            }
            onComplete()
        }
    }
}
