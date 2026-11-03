package ua.edu.chnu.labo3.domain.usecase

import ua.edu.chnu.labo3.data.NetworkResult
import ua.edu.chnu.labo3.data.Post
import ua.edu.chnu.labo3.data.PostRepository
import ua.edu.chnu.labo3.data.RawResponse

/*
 * Lab 3: domain-level use cases for the posts API. Each class is one
 * application action ("use case") with a single `invoke`, so a ViewModel
 * calls `getPost(id)` instead of talking to the repository directly.
 */

/** GET /posts/{id} as a typed [Post]. */
class GetPostUseCase(private val repository: PostRepository) {
    suspend operator fun invoke(id: Int): NetworkResult<Post> = repository.getPost(id)
}

/** POST /posts as a typed [Post]. */
class CreatePostUseCase(private val repository: PostRepository) {
    suspend operator fun invoke(title: String, body: String): NetworkResult<Post> =
        repository.createPost(title, body)
}

/** PUT /posts/{id} as a typed [Post]. */
class UpdatePostUseCase(private val repository: PostRepository) {
    suspend operator fun invoke(id: Int, title: String, body: String): NetworkResult<Post> =
        repository.updatePost(id, title, body)
}

/** DELETE /posts/{id}. */
class DeletePostUseCase(private val repository: PostRepository) {
    suspend operator fun invoke(id: Int): NetworkResult<Unit> = repository.deletePost(id)
}

/** Lab 12: GET /posts, raw body as text. */
class GetPostsTextUseCase(private val repository: PostRepository) {
    suspend operator fun invoke(): NetworkResult<String> = repository.getPostsText()
}

/** Lab 13: POST /posts, raw body as text. */
class CreatePostTextUseCase(private val repository: PostRepository) {
    suspend operator fun invoke(title: String, body: String): NetworkResult<String> =
        repository.createPostText(title, body)
}

/** Lab 14: PUT /posts/{id}, raw body as text. */
class UpdatePostTextUseCase(private val repository: PostRepository) {
    suspend operator fun invoke(id: Int, title: String, body: String): NetworkResult<String> =
        repository.updatePostText(id, title, body)
}

/** Lab 15: DELETE /posts/{id}, raw status + body. */
class DeletePostTextUseCase(private val repository: PostRepository) {
    suspend operator fun invoke(id: Int): NetworkResult<RawResponse> =
        repository.deletePostText(id)
}
