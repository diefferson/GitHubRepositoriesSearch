package diefferson.com.gitihub.githubrepositoriessearch.ui.details

import android.arch.lifecycle.MutableLiveData
import diefferson.com.gitihub.githubrepositoriessearch.R
import diefferson.com.gitihub.githubrepositoriessearch.data.model.GitHubRepositoryModel
import diefferson.com.gitihub.githubrepositoriessearch.data.repository.GitHubRepository
import diefferson.com.gitihub.githubrepositoriessearch.ui.base.BaseViewModel
import diefferson.com.gitihub.githubrepositoriessearch.util.asyncCatching
import diefferson.com.gitihub.githubrepositoriessearch.util.onFailure
import diefferson.com.gitihub.githubrepositoriessearch.util.onSuccess


class DetailsViewModel(private val gitHubRepository: GitHubRepository) : BaseViewModel(){

    val repository = MutableLiveData<GitHubRepositoryModel>()

    fun getRepository(owner:String, repo:String){
        if(repository.value  == null){
            showLoading()
            asyncCatching {
                gitHubRepository.getRepository(owner, repo)
            }.onSuccess {
                dismissLoading()
                repository.value = it
            }.onFailure {
                dismissLoading()
                showError(R.string.error_get_repo)
            }
        }
    }
}