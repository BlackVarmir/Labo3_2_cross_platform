package ua.edu.chnu.labo3.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType

/**
 * Low-level networking layer: the actual API calls.
 *
 * [ApiServiceImpl] performs the four HTTP verbs against the test API using the
 * shared [HttpClient]. The repository sits on top of this and isolates the
 * ViewModel from Ktor entirely.
 */
interface ApiService {
    suspend fun getPost(id: Int): Post
    suspend fun createPost(post: Post): Post
    suspend fun updatePost(post: Post): Post
    suspend fun deletePost(id: Int)

    /**
     * Lab 12: a plain GET call that returns the response body verbatim,
     * without deserializing it, so the screen can show the raw text.
     */
    suspend fun getPostsRawText(): String

    /**
     * Lab 13: a plain POST call. Sends a new post as JSON and returns the
     * response body verbatim, without deserializing it, so the screen can show
     * the raw text the server sends back.
     */
    suspend fun createPostRawText(title: String, body: String): String

    /**
     * Lab 14: a plain PUT call. Updates an existing post as JSON and returns
     * the response body verbatim, without deserializing it, so the screen can
     * show the raw text the server sends back.
     */
    suspend fun updatePostRawText(id: Int, title: String, body: String): String

    /**
     * Lab 15: a plain DELETE call. Deletes an existing post and returns the
     * HTTP status line plus the response body verbatim, without deserializing
     * it, so the screen can show the raw text the server sends back
     * (jsonplaceholder answers a DELETE with an empty JSON object `{}`).
     */
    suspend fun deletePostRawText(id: Int): String
}

class ApiServiceImpl(
    private val client: HttpClient,
) : ApiService {

    private val baseUrl = "https://jsonplaceholder.typicode.com"

    override suspend fun getPost(id: Int): Post =
        client.get("$baseUrl/posts/$id").body()

    override suspend fun createPost(post: Post): Post =
        client.post("$baseUrl/posts") {
            contentType(ContentType.Application.Json)
            setBody(post)
        }.body()

    override suspend fun updatePost(post: Post): Post =
        client.put("$baseUrl/posts/${post.id}") {
            contentType(ContentType.Application.Json)
            setBody(post)
        }.body()

    override suspend fun deletePost(id: Int) {
        client.delete("$baseUrl/posts/$id")
    }

    override suspend fun getPostsRawText(): String =
        client.get("$baseUrl/posts").bodyAsText()

    override suspend fun createPostRawText(title: String, body: String): String =
        client.post("$baseUrl/posts") {
            contentType(ContentType.Application.Json)
            setBody(Post(title = title, body = body))
        }.bodyAsText()

    override suspend fun updatePostRawText(id: Int, title: String, body: String): String =
        client.put("$baseUrl/posts/$id") {
            contentType(ContentType.Application.Json)
            setBody(Post(id = id, title = title, body = body))
        }.bodyAsText()

    override suspend fun deletePostRawText(id: Int): String {
        val response = client.delete("$baseUrl/posts/$id")
        return "HTTP ${response.status}\n\n${response.bodyAsText()}"
    }
}
