package `is`.hi.darts2.model

import java.time.LocalDateTime

data class GameInvite(
    val id: Long,
    val gameId: Long,
    val userId: Long,
    val inviterId: Long,
    val invitationTime: LocalDateTime
)
