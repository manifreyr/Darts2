package `is`.hi.darts2.model

data class Game(
    val id: Long,
    val players: List<Player>,
    val rounds: List<Round>,
    val legs: List<Leg>,
    val currentRound: Int,
    val gameType: String,
    val date: String,
    val isPaused: Boolean,
    val status: GameStatus,
    val currentPlayerIndex: Int,
    val totalLegs: Long
)
