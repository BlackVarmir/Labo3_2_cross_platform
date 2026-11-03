package ua.edu.chnu.labo3.data

/**
 * Wrapper over [ApiService] that isolates the ViewModel from the service.
 *
 * Every method returns a [NetworkResult] instead of throwing, so the
 * presentation layer never deals with Ktor types or exceptions directly.
 */
interface PostRepository {
    suspend fun getPost(id: Int): NetworkResult<Post>
    suspend fun createPost(title: String, body: String): NetworkResult<Post>
    suspend fun updatePost(id: Int, title: String, body: String): NetworkResult<Post>
    suspend fun deletePost(id: Int): NetworkResult<Unit>

    /** Lab 12: GET the posts list and return its raw body as plain text. */
    suspend fun getPostsText(): NetworkResult<String>

    /** Lab 13: POST a new post and return the raw response body as plain text. */
    suspend fun createPostText(title: String, body: String): NetworkResult<String>

    /** Lab 14: PUT (update) a post and return the raw response body as plain text. */
    suspend fun updatePostText(id: Int, title: String, body: String): NetworkResult<String>

    /** Lab 15: DELETE a post and return the raw response (status + body) as plain text. */
    suspend fun deletePostText(id: Int): NetworkResult<String>
}

class PostRepositoryImpl(
    private val apiService: ApiService,
) : PostRepository {

    override suspend fun getPost(id: Int): NetworkResult<Post> =
        networkResultOf { apiService.getPost(id) }

    override suspend fun createPost(title: String, body: String): NetworkResult<Post> =
        networkResultOf { apiService.createPost(Post(title = title, body = body)) }

    override suspend fun updatePost(id: Int, title: String, body: String): NetworkResult<Post> =
        networkResultOf { apiService.updatePost(Post(id = id, title = title, body = body)) }

    override suspend fun deletePost(id: Int): NetworkResult<Unit> =
        networkResultOf { apiService.deletePost(id) }

    override suspend fun getPostsText(): NetworkResult<String> =
        networkResultOf { apiService.getPostsRawText() }

    override suspend fun createPostText(title: String, body: String): NetworkResult<String> =
        networkResultOf { apiService.createPostRawText(title, body) }

    override suspend fun updatePostText(id: Int, title: String, body: String): NetworkResult<String> =
        networkResultOf { apiService.updatePostRawText(id, title, body) }

    override suspend fun deletePostText(id: Int): NetworkResult<String> =
        networkResultOf { apiService.deletePostRawText(id) }
}
