package fr.uge.review.dto.review

import fr.uge.review.dto.LikeStateDTO
import fr.uge.review.dto.comment.CommentDTO
import fr.uge.review.dto.user.UserDTO
import java.util.Date

data class ReviewOneReviewDTO(val id: Long, val title: String, val commentary: String, val code: String,
                              val test: String, val likes: Int, val author: UserDTO, val date: Date, val comments: List<CommentDTO>,
                              val unitTests: TestsReviewDTO?, val likeState: LikeStateDTO
)
