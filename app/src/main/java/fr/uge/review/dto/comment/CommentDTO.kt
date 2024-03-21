package fr.uge.review.dto.comment

import fr.uge.review.dto.like.LikeState
import fr.uge.review.dto.response.ResponseDTO
import fr.uge.review.dto.user.UserDTO
import java.util.Date

data class CommentDTO(val id: Long, val content: String, val date: Date, val likes: Int, val author: UserDTO,
                      val responses: List<ResponseDTO>, val likeState: LikeState)

