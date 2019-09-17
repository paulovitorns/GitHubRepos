package br.com.githubrepos.data

import br.com.githubrepos.data.model.Repository
import br.com.githubrepos.data.model.SearchRepositoriesResult
import br.com.githubrepos.data.search.ResultNotFoundException
import br.com.githubrepos.data.search.SearchGitHubRepositoriesRepository
import br.com.githubrepos.domain.search.mockedItem
import br.com.githubrepos.library.retrofit.endpoint.SearchRepositoriesEndpoint
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.willReturn
import io.reactivex.Observable
import okhttp3.ResponseBody
import org.junit.Test
import retrofit2.Response

class SearchGitHubRepositoriesRepositoryTest {

    private val searchEndpoint = mock<SearchRepositoriesEndpoint>()
    private val searchGitHubRepositories = SearchGitHubRepositoriesRepository(searchEndpoint)

    @Test
    fun `success on fetch a GitHub repositories search`() {
        val mockedItem = mockedItem()
        val itemList = listOf(mockedItem, mockedItem)
        val expected = SearchRepositoriesResult(repositories = itemList)
        val result = SearchRepositoriesResult(repositories = itemList)

        val query = "language:koltin"
        val sort = "stars"
        val order = "asc"
        val page = 1
        val perPage = 10

        given {
            searchEndpoint.searchRepositories(query, sort, order, page, perPage)
        }.willReturn { Observable.just(Response.success(200, result)) }

        searchGitHubRepositories.fetchRepositories(query, sort, order, page, perPage)
            .test()
            .assertNoErrors()
            .assertComplete()
            .assertValue(expected)
            .awaitTerminalEvent()

        verify(searchEndpoint).searchRepositories(query, sort, order, page, perPage)
    }

    @Test
    fun `get an empty result after load all pages of a GitHub repositories search pagination`() {
        val itemList = emptyList<Repository>()
        val result = SearchRepositoriesResult(repositories = itemList)

        val query = "language:koltin"
        val sort = "stars"
        val order = "asc"
        val page = 2
        val perPage = 10

        given {
            searchEndpoint.searchRepositories(query, sort, order, page, perPage)
        }.willReturn { Observable.just(Response.success(200, result)) }

        searchGitHubRepositories.fetchRepositories(query, sort, order, page, perPage)
            .test()
            .assertNoErrors()
            .assertComplete()
            .assertValue { it.repositories.isEmpty() }
            .awaitTerminalEvent()

        verify(searchEndpoint).searchRepositories(query, sort, order, page, perPage)
    }

    @Test
    fun `throw an error when the API fails trying to search a GitHub repositories search`() {
        val query = "language:koltin"
        val sort = "stars"
        val order = "asc"
        val page = 1
        val perPage = 10

        given {
            searchEndpoint.searchRepositories(query, sort, order, page, perPage)
        }.willReturn {
            Observable.just(
                Response.error(
                    500,
                    ResponseBody.create(null, "")
                )
            )
        }

        searchGitHubRepositories.fetchRepositories(query, sort, order, page, perPage)
            .test()
            .assertError(IllegalArgumentException::class.java)
            .assertNotComplete()
            .assertNoValues()
            .awaitTerminalEvent()
    }

    @Test
    fun `throw an error when receive an null response body trying to search a GitHub repositories search`() {
        val query = "language:koltin"
        val sort = "stars"
        val order = "asc"
        val page = 2
        val perPage = 10

        given {
            searchEndpoint.searchRepositories(query, sort, order, page, perPage)
        }.willReturn {
            Observable.just(Response.success(null as SearchRepositoriesResult?))
        }

        searchGitHubRepositories.fetchRepositories(query, sort, order, page, perPage)
            .test()
            .assertError(ResultNotFoundException::class.java)
            .assertNotComplete()
            .assertNoValues()
            .awaitTerminalEvent()
    }
}
