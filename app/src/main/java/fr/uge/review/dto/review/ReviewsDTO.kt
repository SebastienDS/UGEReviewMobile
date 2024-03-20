package fr.uge.review.dto.review

import fr.uge.review.ShowAble
import fr.uge.review.dto.user.UserDTO
import java.util.Date

data class ReviewsDTO (override val id : Long, val title: String, override val date: Date,
                       override val author: UserDTO) :ShowAble {
    override fun reviewId(): Long = id
    override fun content(): String = title
}