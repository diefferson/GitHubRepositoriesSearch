package diefferson.com.gitihub.githubrepositoriessearch.data.repository

import diefferson.com.gitihub.githubrepositoriessearch.data.api.GitHubApi
import diefferson.com.gitihub.githubrepositoriessearch.data.model.GitHubRepositoryModel
import diefferson.com.gitihub.githubrepositoriessearch.data.model.RepositoriesResponse
import io.coroutines.cache.core.CachePolicy
import io.coroutines.cache.core.CoroutinesCache
import java.util.concurrent.TimeUnit

class GitHubRepository(private val api: GitHubApi, private val cache:CoroutinesCache){

    suspend fun searchRepositories(query:String, page:Int) : RepositoriesResponse {
        return cache.asyncCache({ api.search(query, page) },
                SEARCH_KEY+query+page,
                CachePolicy.LifeCache(1, TimeUnit.DAYS)).await()
    }

    suspend fun getRepository(owner:String, repo:String) : GitHubRepositoryModel{
        return cache.asyncCache({api.getRepository(owner, repo)},
                REPO_KEY+owner+repo,
                CachePolicy.LifeCache(1, TimeUnit.DAYS)).await()
    }

    companion object {
        const val SEARCH_KEY = "SEARCH_KEY"
        const val REPO_KEY = "REPO_KEY"
    }
}