package diefferson.com.gitihub.githubrepositoriessearch.data.api

import diefferson.com.gitihub.githubrepositoriessearch.data.model.GitHubRepositoryModel
import diefferson.com.gitihub.githubrepositoriessearch.data.model.RepositoriesResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubApi {

    @GET("search/repositories")
    fun search(
        @Query("q") query:String,
        @Query("page") page:Int,
        @Query("per_page") perPage:Int = 20
    ): Deferred<RepositoriesResponse>


    @GET("repos/{owner}/{repo}")
    fun getRepository(
            @Path("owner") owner:String,
            @Path("repo") repo:String)
    : Deferred<GitHubRepositoryModel>

}
