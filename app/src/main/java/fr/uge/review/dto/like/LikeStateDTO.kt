package fr.uge.review.dto.like

class LikeStateDTO (val likes: Int, val likeState: LikeState)


enum class LikeState {
    LIKE, DISLIKE, NONE
}