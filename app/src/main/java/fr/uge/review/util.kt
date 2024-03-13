package fr.uge.review

import java.text.SimpleDateFormat
import java.util.Date

fun Date.withFormat(format: String): String = SimpleDateFormat(format).format(this)
