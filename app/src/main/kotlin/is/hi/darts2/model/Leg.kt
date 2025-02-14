package `is`.hi.darts2.model

data class Leg(
    val startIndex: Int,
    val endIndex: Int?,          // May be null if not yet set
    val winnerPlayerId: Long?    // May be null if no winner is set yet
)