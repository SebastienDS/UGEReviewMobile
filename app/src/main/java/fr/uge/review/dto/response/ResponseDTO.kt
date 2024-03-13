package fr.uge.review.dto.response

import fr.uge.review.dto.LikeStateDTO
import fr.uge.review.dto.user.UserDTO
import java.util.Date

data class ResponseDTO(val id: Long, val content: String, val date: Date, val likes: Int, val author: UserDTO,
    val likeState: LikeStateDTO)
