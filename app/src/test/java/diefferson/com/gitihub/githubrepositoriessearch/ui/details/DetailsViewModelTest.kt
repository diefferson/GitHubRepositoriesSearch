package diefferson.com.gitihub.githubrepositoriessearch.ui.details

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import com.nhaarman.mockitokotlin2.*
import diefferson.com.gitihub.githubrepositoriessearch.data.model.GitHubRepositoryModel
import diefferson.com.gitihub.githubrepositoriessearch.data.repository.GitHubRepository
import diefferson.com.gitihub.githubrepositoriessearch.ui.base.ViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

@ExperimentalCoroutinesApi
class DetailsViewModelTest{

    @get:Rule
     val rule: TestRule = InstantTaskExecutorRule()

    private lateinit var repoMock :GitHubRepositoryModel
    private lateinit var gitHubRepositoryMock: GitHubRepository
    private lateinit var observer:Observer<GitHubRepositoryModel>
    private lateinit var observerState:Observer<ViewState>

    lateinit var detailsViewModel: DetailsViewModel

    @Before
    fun setup(){
        //Dispatchers.setMain(Dispatchers.Unconfined)
        repoMock  = mock()
        gitHubRepositoryMock =mock()
        observer = mock()
        observerState = mock()
        detailsViewModel = DetailsViewModel(gitHubRepositoryMock)
    }

    @After
    fun tearDown() {
        //Dispatchers.resetMain()
    }

    @Test
    fun testGetRepoSuccess(){
        detailsViewModel.repository.observeForever(observer)
        runBlocking {
            whenever(gitHubRepositoryMock.getRepository(OWNER, REPO)).thenReturn(repoMock)
            detailsViewModel.getRepository(OWNER, REPO)
        }

        verify(observer).onChanged(repoMock)
    }

    @Test
    fun testShouldNotReloadOnRotateScreen(){
        detailsViewModel.repository.postValue(repoMock)
        runBlocking {
            whenever(gitHubRepositoryMock.getRepository(OWNER, REPO)).thenReturn(repoMock)
            detailsViewModel.getRepository(OWNER, REPO)
            verifyZeroInteractions(gitHubRepositoryMock)
        }
    }

    @Test
    fun testShouldShowExpectedError(){
        detailsViewModel.repository.observeForever(observer)
        detailsViewModel.getViewStateObservable().observeForever(observerState)
        runBlocking {
            whenever(gitHubRepositoryMock.getRepository(OWNER, REPO)).thenReturn(null)
            detailsViewModel.getRepository(OWNER, REPO)
            delay(200)
        }
        verifyZeroInteractions(observer)

        verify(observerState, times(3))
    }

    companion object {
        const val OWNER = "OWNER"
        const val REPO  = "REPO"
    }

}