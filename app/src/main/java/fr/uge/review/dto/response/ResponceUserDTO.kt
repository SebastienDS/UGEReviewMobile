package fr.uge.review.dto.response

import fr.uge.review.ShowAble
import fr.uge.review.dto.user.UserDTO
import java.util.Date

data class ResponseUserDTO(override val id: Long, val content: String, override val date: Date,
    override val author: UserDTO, val reviewId: Long): ShowAble {
    override fun reviewId(): Long = reviewId

    override fun content(): String = content
}