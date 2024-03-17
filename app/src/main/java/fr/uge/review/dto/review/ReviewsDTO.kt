package fr.uge.review.dto.review

import fr.uge.review.dto.user.UserDTO
import java.util.Date

data class ReviewsDTO (val id : Long, val title: String, val date: Date, val author: UserDTO){
}