package `is`.hi.darts2.model

data class User(
    val id: Long,
    val email: String,
    val password: String,
    val username: String,
    val threeDartAverage: Double = 0.0,
    val totalRounds: Long = 0,
    val previousGames: List<String> = emptyList()
)