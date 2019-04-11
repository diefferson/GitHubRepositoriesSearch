package diefferson.com.gitihub.githubrepositoriessearch.ui.home

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import com.nhaarman.mockitokotlin2.*
import diefferson.com.gitihub.githubrepositoriessearch.data.model.RepositoriesResponse
import diefferson.com.gitihub.githubrepositoriessearch.data.repository.GitHubRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.assertj.core.api.Assertions
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    lateinit var homeViewModel : HomeViewModel
    
    lateinit var repositoriesResponseMock: RepositoriesResponse
    lateinit var observer: Observer<Boolean>
    lateinit var gitHubRepositoryMock: GitHubRepository 


    @Before
    fun setup(){
       // Dispatchers.setMain(Dispatchers.Unconfined)
        repositoriesResponseMock= mock()
        observer = mock()
        gitHubRepositoryMock = mock()
        homeViewModel = HomeViewModel(gitHubRepositoryMock)
    }

    @After
    fun tearDown() {
        //Dispatchers.resetMain()
    }

    @Test
    fun testRefreshRepoShouldCallGetRepoWithValueOne(){
        val viewModelTest= spy(homeViewModel)
        viewModelTest.refreshRepos()
        verify(viewModelTest).loadRepos()
    }

    @Test
    fun testLoadMoreGetReposShouldCallGetReposIncreasingPage(){
        val viewModelTest= spy(homeViewModel)
        viewModelTest.currentPage = 1
        viewModelTest.totalPages = 5
        viewModelTest.currentQuery = ""
        viewModelTest.loadMoreRepos()
        verify(viewModelTest).loadRepos(page=2)
    }

    @Test
    fun testLoadMoreSearchReposShouldCallGetReposIncreasingPage(){
        val viewModelTest= spy(homeViewModel)
        viewModelTest.currentPage = 1
        viewModelTest.totalPages = 5
        viewModelTest.currentQuery = "query"
        viewModelTest.loadMoreRepos()
        verify(viewModelTest).searchRepos("query",2)
    }

    @Test
    fun testLoadMoreShouldNotCallGetReposWhenIsLastPage(){
        homeViewModel.enableLoadMore.observeForever(observer)
        homeViewModel.currentPage = 5
        homeViewModel.totalPages = 5
        homeViewModel.currentQuery = ""
        homeViewModel.loadMoreRepos()
        verify(observer).onChanged(false)
    }


    @Test
    fun testGetReposSuccessCase() {
        homeViewModel.updatedRepos.observeForever(observer)
        runBlocking {
            whenever(repositoriesResponseMock.totalCount).thenReturn(TOTAL)
            whenever(repositoriesResponseMock.incompleteResults).thenReturn(FINAL)
            whenever(gitHubRepositoryMock.searchRepositories(QUERY, PAGE)).thenReturn(repositoriesResponseMock)
            homeViewModel.loadRepos(QUERY, PAGE)

        }
        verify(observer).onChanged(true)
        Assertions.assertThat(homeViewModel.currentPage).isEqualTo(NEW_PAGE)
        Assertions.assertThat(homeViewModel.totalPages).isEqualTo(TOTAL /20)
    }




    companion object {
        const val TOTAL = 100
        const val FINAL = false
        const val PAGE = 2
        const val NEW_PAGE = 3
        const val QUERY = "QUERY"
    }
}