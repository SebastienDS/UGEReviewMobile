package fr.uge.review.dto.review

data class TestsReviewDTO(val id: Long, val succeededCount: Long, val totalCount: Long, val errors: List<String>)
