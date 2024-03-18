package fr.uge.review

import fr.uge.review.dto.user.UserDTO
import java.util.Date

interface ShowAble {
    fun reviewId(): Long
    val id: Long
    fun content(): String
    val date: Date
    val author: UserDTO
}