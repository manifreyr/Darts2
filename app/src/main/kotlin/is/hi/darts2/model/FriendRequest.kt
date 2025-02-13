package `is`.hi.darts2.model

data class FriendRequest(
    val id: Long,
    val requester: User,
    val requestedUser: User
)
