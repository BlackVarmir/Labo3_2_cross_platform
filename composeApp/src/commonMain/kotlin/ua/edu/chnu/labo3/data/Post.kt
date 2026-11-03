package ua.edu.chnu.labo3.data

import kotlinx.serialization.Serializable

/**
 * Data model for the test API (https://jsonplaceholder.typicode.com/posts).
 *
 * The same shape is used for every verb: GET returns one, POST/PUT send one and
 * echo it back, DELETE just needs the [id]. [id] is nullable so a brand-new post
 * can be created without one.
 */
@Serializable
data class Post(
    val id: Int? = null,
    val userId: Int = 1,
    val title: String = "",
    val body: String = "",
)
