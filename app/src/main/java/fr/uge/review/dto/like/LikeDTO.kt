package fr.uge.review.dto.like

import fr.uge.review.ShowAble
import fr.uge.review.dto.user.UserDTO
import java.util.Date

class LikeDTO(
    override val id: Long, val content: String, override val date: Date, override val author: UserDTO,
    val reviewId :Long, val className: String) : ShowAble {
    override fun reviewId(): Long = reviewId
    override fun content(): String = content
}
