package br.com.githubrepos.domain.search

import br.com.githubrepos.data.model.SearchRepositoriesResult
import br.com.githubrepos.library.reactivex.TestSchedulerProvider
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.willReturn
import io.reactivex.Single
import org.junit.Test

class GetGitHubRepositoriesByLanguageUseCaseTest {

    private val searchGitHubRepositories = mock<SearchRepository>()
    private val gitHubRepositoriesByLanguage = GetGitHubRepositoriesByLanguage(
        searchGitHubRepositories,
        TestSchedulerProvider()
    )

    @Test
    fun `success on fetch GitHub repositories by Kotlin language`() {
        val mockedItem = mockedItem()
        val expected = listOf(mockedItem, mockedItem)
        val result = listOf(mockedItem, mockedItem)

        val query = "koltin"
        val sort = "stars"
        val order = "asc"
        val page = 1
        val perPage = 10

        given {
            searchGitHubRepositories.fetchRepositories(
                query = "language:$query",
                sort = sort,
                order = order,
                page = page,
                perPage = perPage
            )
        }.willReturn { Single.just(SearchRepositoriesResult(items = result)) }

        gitHubRepositoriesByLanguage(query = query, sort = sort, order = order)
            .test()
            .assertNoErrors()
            .assertComplete()
            .assertValue(expected)
            .awaitTerminalEvent()

        verify(searchGitHubRepositories).fetchRepositories(
            query = "language:$query",
            sort = sort,
            order = order,
            page = page,
            perPage = perPage
        )
    }
}
