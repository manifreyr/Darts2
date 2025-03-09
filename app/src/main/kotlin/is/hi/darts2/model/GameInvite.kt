package `is`.hi.darts2.model


data class GameInvite(
    val id: Long,
    val gameId: Long,
    val userId: Long,
    val inviterId: Long,
    val invitationTime: String
)
