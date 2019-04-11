package diefferson.com.gitihub.githubrepositoriessearch.ui.home

import android.arch.lifecycle.MutableLiveData
import diefferson.com.gitihub.githubrepositoriessearch.R
import diefferson.com.gitihub.githubrepositoriessearch.data.model.GitHubRepositoryModel
import diefferson.com.gitihub.githubrepositoriessearch.data.model.RepositoriesResponse
import diefferson.com.gitihub.githubrepositoriessearch.data.repository.GitHubRepository
import diefferson.com.gitihub.githubrepositoriessearch.ui.base.BaseViewModel
import diefferson.com.gitihub.githubrepositoriessearch.ui.base.SingleLiveEvent
import diefferson.com.gitihub.githubrepositoriessearch.util.asyncCatching
import diefferson.com.gitihub.githubrepositoriessearch.util.onFailure
import diefferson.com.gitihub.githubrepositoriessearch.util.onSuccess

class HomeViewModel(private val gitHubRepository: GitHubRepository) : BaseViewModel(){

    var currentQuery = ""
    var currentPage = 1
    var totalPages = 0
    val repos = ArrayList<GitHubRepositoryModel>()
    val enableLoadMore = MutableLiveData<Boolean>()
    val updatedRepos = SingleLiveEvent<Boolean>()
    var searching = false

    fun refreshRepos() {
        loadRepos()
    }

    fun loadMoreRepos(){
        if(currentPage< totalPages){
            currentPage++
            if(currentQuery.isEmpty()){
                loadRepos(page = currentPage)
            }else{
                searchRepos(currentQuery, currentPage)
            }
        }else{
            enableLoadMore.value = false
        }
    }

    fun loadRepos(query:String= "termo", page:Int = 1) {
        asyncCatching {
            gitHubRepository.searchRepositories(query, page)
        }.onSuccess {
            handleRepoResult(it)
        }.onFailure{
            showError(R.string.error_get_repos)
        }
    }

    fun searchRepos(query:String, page: Int) {
        if(query.isNotEmpty()){
            currentPage = page
            currentQuery = query
            if(page == 1){
                repos.clear()
                searching = true
                updatedRepos.postValue(true)
            }
            asyncCatching {
                gitHubRepository.searchRepositories(query, page)
            }.onSuccess {
                searching = false
                handleRepoResult(it)
            }.onFailure {
                showError(R.string.error_get_repos)
            }
        }
    }

    private fun handleRepoResult(result: RepositoriesResponse) {

        totalPages = result.totalCount/20
        enableLoadMore.postValue(!result.incompleteResults)

        val reposResult = result.items

        if (currentPage == 1) {
            repos.clear()
            repos.addAll(reposResult)
        } else {
            repos.addAll(reposResult)
        }

        if(!result.incompleteResults){
            currentPage++
        }

        updatedRepos.postValue(true)
    }
}