package diefferson.com.gitihub.githubrepositoriessearch.data.model

import com.squareup.moshi.Json

data class GenreResponse(val genres: List<Genre>)

data class Genre(val id: Long, val name: String)

data class RepositoriesResponse(
    @Json(name = "total_count") val totalCount: Int,
    @Json(name = "incomplete_results") var incompleteResults:Boolean,
    @Json(name = "items") val items: List<GitHubRepositoryModel>
)

data class GitHubRepositoryModel(
    val id: Long,
    val name: String,
    @Json(name = "full_name") val fullName: String,
    val description: String,
    val url: String,
    @Json(name = "stargazers_count") val stargazersCount :Int,
    @Json(name = "watchers_count") val watchersCount :Int,
    @Json(name = "forks_count") val forksCount :Int,
    val language :String,
    val owner: Owner
)

data class Owner(
        val id: Long,
        val login: String,
        @Json(name = "avatar_url") val avataUrl: String
)