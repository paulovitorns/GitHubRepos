package br.com.githubrepos.screens.home

import br.com.githubrepos.data.search.ResultNotFoundException
import br.com.githubrepos.domain.search.GetGitHubRepositoriesByLanguage
import br.com.githubrepos.domain.search.GetPaginatedGitHubRepositories
import br.com.githubrepos.domain.search.mockedItem
import br.com.githubrepos.library.reactivex.DisposeBag
import br.com.githubrepos.library.reactivex.TestSchedulerProvider
import br.com.githubrepos.library.state.StateStore
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.willReturn
import io.reactivex.Observable
import io.reactivex.Single
import org.junit.Test
import java.net.UnknownHostException

class HomePresenterTest {

    private val schedulerProvider = TestSchedulerProvider()
    private val gitHubRepositoriesByLanguage = mock<GetGitHubRepositoriesByLanguage>()
    private val paginatedGitHubRepositories = mock<GetPaginatedGitHubRepositories>()
    private val stateStore = mock<StateStore>()
    private val homeView = mock<HomeUi>()
    private val presenter = HomePresenter(
        gitHubRepositoriesByLanguage,
        paginatedGitHubRepositories,
        schedulerProvider,
        stateStore
    ).apply {
        disposeBag = DisposeBag()
    }

    private val mockedItem = mockedItem()

    init {
        presenter.setUi(homeView)
    }

    @Test
    fun `success on load default language with no previous state stored`() {
        val expectedItems = listOf(mockedItem, mockedItem)
        val expectedViewState = SearchViewState.Builder(presenter.searchViewState)
            .setSearchResult(expectedItems)
            .setLoadedAllPages(false)
            .setStateError(null)
            .build()

        given { stateStore.load<SearchViewState>(HomeUi::class) }.willReturn { null }
        given { homeView.search() }.willReturn { Observable.just("") }
        given {
            gitHubRepositoriesByLanguage(
                query = presenter.searchViewState.lastQueryString,
                sort = presenter.searchViewState.sort,
                order = presenter.searchViewState.order
            )
        }.willReturn { Single.just(expectedItems) }

        presenter.onCreate()

        assert(presenter.searchViewState == expectedViewState)

        verify(homeView).showDefaultQueryString(presenter.searchViewState.lastQueryString)

        verify(homeView).showProgress()
        verify(homeView).hideAllErrorState()
        verify(homeView).showSearchResult(expectedItems)
        verify(homeView).hideProgress()
        verify(stateStore).load<SearchViewState>(HomeUi::class)
        verify(gitHubRepositoriesByLanguage).invoke(
            query = presenter.searchViewState.lastQueryString,
            sort = presenter.searchViewState.sort,
            order = presenter.searchViewState.order
        )
    }

    @Test
    fun `success on search repositories with a custom language on GitHub`() {
        val expectedItems = listOf(mockedItem, mockedItem)
        val typedQuery = "Java"
        val expectedViewState = SearchViewState.Builder(presenter.searchViewState)
            .setLastQueryString(typedQuery)
            .setSearchResult(expectedItems)
            .setLoadedAllPages(false)
            .setStateError(null)
            .build()

        given { stateStore.load<SearchViewState>(HomeUi::class) }.willReturn { null }
        given { homeView.search() }.willReturn { Observable.just(typedQuery) }
        given {
            gitHubRepositoriesByLanguage(
                query = presenter.searchViewState.lastQueryString,
                sort = presenter.searchViewState.sort,
                order = presenter.searchViewState.order
            )
        }.willReturn { Single.just(expectedItems) }

        presenter.onCreate()

        assert(presenter.searchViewState == expectedViewState)

        verify(homeView, times(2)).showProgress()
        verify(homeView).hideAllErrorState()
        verify(homeView).showSearchResult(expectedItems)
        verify(homeView).hideProgress()
        verify(stateStore).load<SearchViewState>(HomeUi::class)
        verify(gitHubRepositoriesByLanguage).invoke(
            query = presenter.searchViewState.lastQueryString,
            sort = presenter.searchViewState.sort,
            order = presenter.searchViewState.order
        )
    }

    @Test
    fun `success on load next page of a GitHub search result`() {
        val expectedItems = listOf(mockedItem, mockedItem)
        val paginatedExpectedItems = listOf(mockedItem, mockedItem, mockedItem, mockedItem)
        val expectedViewState = SearchViewState.Builder(presenter.searchViewState)
            .setSearchResult(paginatedExpectedItems)
            .setLoadedAllPages(false)
            .increaseCurrentPage()
            .setStateError(null)
            .build()

        given {
            gitHubRepositoriesByLanguage(
                query = presenter.searchViewState.lastQueryString,
                sort = presenter.searchViewState.sort,
                order = presenter.searchViewState.order
            )
        }.willReturn { Single.just(expectedItems) }
        given {
            paginatedGitHubRepositories(
                query = presenter.searchViewState.lastQueryString,
                sort = presenter.searchViewState.sort,
                order = presenter.searchViewState.order,
                page = presenter.searchViewState.currentPage + 1
            )
        }.willReturn { Single.just(expectedItems) }
        given { stateStore.load<SearchViewState>(HomeUi::class) }.willReturn { null }
        given { homeView.search() }.willReturn { Observable.just("") }

        presenter.onCreate()
        presenter.loadNextPage()

        assert(presenter.searchViewState == expectedViewState)

        verify(homeView).showDefaultQueryString(presenter.searchViewState.lastQueryString)
        verify(homeView, times(2)).showProgress()
        verify(homeView, times(2)).hideAllErrorState()
        verify(homeView).showNextPage(paginatedExpectedItems)
        verify(homeView, times(2)).hideProgress()
        verify(paginatedGitHubRepositories).invoke(
            query = presenter.searchViewState.lastQueryString,
            sort = presenter.searchViewState.sort,
            order = presenter.searchViewState.order,
            page = presenter.searchViewState.currentPage
        )
    }

    @Test
    fun `show result not found state to a unknown language query`() {
        val typedQuery = "qwerty"
        val error = ResultNotFoundException(typedQuery)
        val expectedViewState = SearchViewState.Builder(presenter.searchViewState)
            .setLoadedAllPages(false)
            .setStateError(error)
            .build()

        given { stateStore.load<SearchViewState>(HomeUi::class) }.willReturn { null }
        given { homeView.search() }.willReturn { Observable.just(typedQuery) }
        given {
            gitHubRepositoriesByLanguage(
                query = presenter.searchViewState.lastQueryString,
                sort = presenter.searchViewState.sort,
                order = presenter.searchViewState.order
            )
        }.willReturn { Single.error(error) }

        presenter.onCreate()

        assert(presenter.searchViewState.stateError == expectedViewState.stateError)

        verify(homeView, times(2)).showProgress()
        verify(homeView).showSearchError(typedQuery)
        verify(homeView).hideProgress()
    }

    @Test
    fun `show offline state while trying to do a search`() {
        val error = UnknownHostException()
        val expectedViewState = SearchViewState.Builder(presenter.searchViewState)
            .setLoadedAllPages(false)
            .setStateError(error)
            .build()

        given { stateStore.load<SearchViewState>(HomeUi::class) }.willReturn { null }
        given { homeView.search() }.willReturn { Observable.just("") }
        given {
            gitHubRepositoriesByLanguage(
                query = presenter.searchViewState.lastQueryString,
                sort = presenter.searchViewState.sort,
                order = presenter.searchViewState.order
            )
        }.willReturn { Single.error(error) }

        presenter.onCreate()

        assert(presenter.searchViewState == expectedViewState)

        verify(homeView).showProgress()
        verify(homeView).showOfflineState()
        verify(homeView).hideProgress()
    }

    @Test
    fun `show a default error state while trying to do a search`() {
        val error = IllegalArgumentException("Any kind of error")
        val expectedViewState = SearchViewState.Builder(presenter.searchViewState)
            .setLoadedAllPages(false)
            .setStateError(error)
            .build()

        given { stateStore.load<SearchViewState>(HomeUi::class) }.willReturn { null }
        given { homeView.search() }.willReturn { Observable.just("") }
        given {
            gitHubRepositoriesByLanguage(
                query = presenter.searchViewState.lastQueryString,
                sort = presenter.searchViewState.sort,
                order = presenter.searchViewState.order
            )
        }.willReturn { Single.error(error) }

        presenter.onCreate()

        assert(presenter.searchViewState == expectedViewState)

        verify(homeView).showProgress()
        verify(homeView).showDefaultError()
        verify(homeView).hideProgress()
    }

    @Test
    fun `success on retry a failed action`() {
        val expectedItems = listOf(mockedItem, mockedItem)
        val expectedViewState = SearchViewState.Builder(presenter.searchViewState)
            .setSearchResult(expectedItems)
            .setLoadedAllPages(false)
            .setStateError(null)
            .build()

        given { stateStore.load<SearchViewState>(HomeUi::class) }.willReturn { null }
        given { homeView.search() }.willReturn { Observable.just("") }
        given {
            gitHubRepositoriesByLanguage(
                query = presenter.searchViewState.lastQueryString,
                sort = presenter.searchViewState.sort,
                order = presenter.searchViewState.order
            )
        }.willReturn { Single.error(UnknownHostException()) }

        presenter.onCreate()

        given {
            gitHubRepositoriesByLanguage(
                query = presenter.searchViewState.lastQueryString,
                sort = presenter.searchViewState.sort,
                order = presenter.searchViewState.order
            )
        }.willReturn { Single.just(expectedItems) }

        presenter.retryAction()

        assert(presenter.searchViewState == expectedViewState)

        verify(homeView, times(2)).showProgress()
        verify(homeView).hideAllErrorState()
        verify(homeView).showSearchResult(expectedItems)
        verify(homeView, times(2)).hideProgress()
        verify(stateStore).load<SearchViewState>(HomeUi::class)
        verify(gitHubRepositoriesByLanguage, times(2)).invoke(
            query = presenter.searchViewState.lastQueryString,
            sort = presenter.searchViewState.sort,
            order = presenter.searchViewState.order
        )
    }

    @Test
    fun `success on restore the last state`() {
        val expectedItems = listOf(mockedItem, mockedItem)
        val expectedViewState = SearchViewState.Builder(presenter.searchViewState)
            .setSearchResult(expectedItems)
            .setLoadedAllPages(false)
            .setStateError(null)
            .build()

        given { stateStore.load<SearchViewState>(HomeUi::class) }.willReturn { null }
        given { homeView.search() }.willReturn { Observable.just("") }
        given {
            gitHubRepositoriesByLanguage(
                query = presenter.searchViewState.lastQueryString,
                sort = presenter.searchViewState.sort,
                order = presenter.searchViewState.order
            )
        }.willReturn { Single.just(expectedItems) }

        presenter.onCreate()
        presenter.onSaveState()

        given { stateStore.load<SearchViewState>(HomeUi::class) }.willReturn { expectedViewState }

        presenter.onCreate()

        assert(presenter.searchViewState == expectedViewState)

        verify(homeView).showProgress()
        verify(homeView, times(2)).hideAllErrorState()
        verify(homeView, times(2)).showSearchResult(expectedItems)
        verify(homeView, times(2)).hideProgress()
        verify(stateStore, times(2)).load<SearchViewState>(HomeUi::class)
        verify(gitHubRepositoriesByLanguage).invoke(
            query = presenter.searchViewState.lastQueryString,
            sort = presenter.searchViewState.sort,
            order = presenter.searchViewState.order
        )
    }
}
