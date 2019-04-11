package diefferson.com.gitihub.githubrepositoriessearch.repository

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import diefferson.com.gitihub.githubrepositoriessearch.data.api.GitHubApi
import diefferson.com.gitihub.githubrepositoriessearch.data.model.GitHubRepositoryModel
import diefferson.com.gitihub.githubrepositoriessearch.data.model.RepositoriesResponse
import diefferson.com.gitihub.githubrepositoriessearch.data.repository.GitHubRepository
import io.coroutines.cache.core.CoroutinesCache
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class GitHubRepositoryTest{

    private lateinit var gitHubRepository: GitHubRepository
    private lateinit var apiMock:GitHubApi
    private lateinit var cacheMock :CoroutinesCache
    private lateinit var reposResponseMock: RepositoriesResponse
    private lateinit var repoMock : GitHubRepositoryModel

    @Before
    fun setup(){
        //Dispatchers.setMain(Dispatchers.Unconfined)
        cacheMock= CoroutinesCache(mock(), true)
        gitHubRepository= mock()
        apiMock=mock()
        apiMock=mock()
        repoMock  = mock()
        apiMock = mock{
            on { search(QUERY, PAGE) }.thenReturn(CompletableDeferred(reposResponseMock))
            on { getRepository(OWNER, REPO) }.thenReturn(CompletableDeferred(repoMock))
        }
        gitHubRepository = GitHubRepository(apiMock,cacheMock)
    }

    @After
    fun tearDown() {
       // Dispatchers.resetMain()
    }

    @Test
    fun testSearchReposShouldReturnCorrectValue(){
        val result = runBlocking{
            whenever(  apiMock.search(QUERY, PAGE)).thenReturn(CompletableDeferred(reposResponseMock))
            gitHubRepository.searchRepositories(QUERY, PAGE)
        }
        verify(apiMock).search(QUERY, PAGE)
        assertEquals(result, reposResponseMock)
    }


    @Test
    fun testGetRepoShouldReturnCorrectValue(){
        val result = runBlocking{
            whenever(  apiMock.getRepository(OWNER, REPO)).thenReturn(CompletableDeferred(repoMock))
            gitHubRepository.getRepository(OWNER, REPO)
        }
        verify(apiMock).getRepository(OWNER, REPO)
        assertEquals(result, repoMock)
    }

    companion object {
        const val QUERY = "myQuery"
        const val PAGE = 3
        const val OWNER = "OWNER"
        const val REPO  = "REPO"
    }
}