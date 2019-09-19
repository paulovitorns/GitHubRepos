package br.com.githubrepos.screens.home

import android.util.Log
import br.com.githubrepos.data.model.Repository
import br.com.githubrepos.data.search.ResultNotFoundException
import br.com.githubrepos.domain.search.GetGitHubRepositoriesByLanguage
import br.com.githubrepos.domain.search.GetPaginatedGitHubRepositories
import br.com.githubrepos.library.reactivex.SchedulerProvider
import br.com.githubrepos.library.reactivex.addDisposableTo
import br.com.githubrepos.library.state.StateStore
import br.com.githubrepos.screens.BasePresenter
import br.com.githubrepos.screens.BaseUi
import io.reactivex.Observable
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class HomePresenter @Inject constructor(
    private val getGitHubRepositoriesByLanguage: GetGitHubRepositoriesByLanguage,
    private val getPaginatedGitHubRepositories: GetPaginatedGitHubRepositories,
    private val schedulerProvider: SchedulerProvider,
    private val stateStore: StateStore
) : BasePresenter<BaseUi>() {

    private val homeUi: HomeUi? get() = baseUi()

    private var searchViewState = SearchViewState
        .Builder(SearchViewState())
        .setLastQueryString("kotlin")
        .increaseCurrentPage()
        .setSort("stars")
        .setOrder("desc")
        .build()

    override fun onCreate() {
        super.onCreate()
        homeUi?.showDefaultQueryString(searchViewState.lastQueryString)
        restoreStateOrLoadDefaultQuery()
        bindIntents()
    }

    override fun onSaveState() {
        super.onSaveState()
        stateStore.save(HomeUi::class, searchViewState)
    }

    private fun restoreStateOrLoadDefaultQuery() {
        val savedState = stateStore.load<SearchViewState>(HomeUi::class)
        savedState?.let { restoreLastState(it) } ?: loadDefaultQuery()
    }

    private fun restoreLastState(lastState: SearchViewState) {
        searchViewState = lastState
        if (lastState.stateError != null) {
            handleError(lastState.stateError)
        } else {
            handleFirstPageResult(lastState.searchResult)
        }
    }

    private fun loadDefaultQuery() {
        fetchRepositories().subscribe(
            { handleFirstPageResult(it) },
            { handleError(it) }
        ).addDisposableTo(disposeBag)
    }

    private fun bindIntents() {
        homeUi?.search()!!
            .debounce(1, TimeUnit.SECONDS, schedulerProvider.postWorkerThread())
            .filter { it.trim().isNotEmpty() }
            .filter { typed -> typed != searchViewState.lastQueryString }
            .subscribe({ queryString ->
                homeUi?.showProgress()
                searchViewState = SearchViewState.Builder(searchViewState)
                    .setLastQueryString(queryString)
                    .build()

                fetchRepositories().subscribe({
                    handleFirstPageResult(it)
                }, {
                    handleError(it)
                }).addDisposableTo(disposeBag)
            }, { Log.e("Search", it.message) }).addDisposableTo(disposeBag)

        homeUi?.loadNextPage()!!
            .filter { !searchViewState.hasLoadedAllPages }
            .subscribe({
                homeUi?.showProgress()

                searchViewState = SearchViewState.Builder(searchViewState)
                    .increaseCurrentPage()
                    .build()

                paginateSearchResult()
                    .subscribe({
                        handlePaginatedResult(it)
                    }, {
                        handleError(it)
                    }).addDisposableTo(disposeBag)
            }, { Log.e("Search", it.message) }).addDisposableTo(disposeBag)

        homeUi?.retryButton()!!
            .filter { searchViewState.stateError is UnknownHostException }
            .subscribe({
                homeUi?.showProgress()
                when {
                    searchViewState.currentPage == 1 -> fetchRepositories()
                    else -> paginateSearchResult()
                }.subscribe({
                    when {
                        searchViewState.currentPage == 1 -> handleFirstPageResult(it)
                        else -> handlePaginatedResult(it)
                    }
                }, {
                    handleError(it)
                }).addDisposableTo(disposeBag)
            }, { Log.e("Search", it.message) }).addDisposableTo(disposeBag)

        homeUi?.repositorySelected()!!
            .subscribe({ repository ->
                homeUi?.openRepositoryWebPage(repository.htmlUrl)
            }, { Log.e("Search", it.message) }).addDisposableTo(disposeBag)
    }

    private fun fetchRepositories(): Observable<List<Repository>> {
        return getGitHubRepositoriesByLanguage(
            query = searchViewState.lastQueryString,
            sort = searchViewState.sort,
            order = searchViewState.order
        )
    }

    private fun paginateSearchResult(): Observable<List<Repository>> {
        return getPaginatedGitHubRepositories(
            query = searchViewState.lastQueryString,
            sort = searchViewState.sort,
            order = searchViewState.order,
            page = searchViewState.currentPage
        )
    }

    private fun handleFirstPageResult(repositories: List<Repository>) {
        homeUi?.hideAllErrorState()

        if (repositories.isEmpty()) {
            homeUi?.showSearchError(searchViewState.lastQueryString)
            searchViewState = SearchViewState.Builder(searchViewState)
                .setLoadedAllPages(true)
                .build()
        } else {
            searchViewState = SearchViewState.Builder(searchViewState)
                .setSearchResult(repositories)
                .setLoadedAllPages(false)
                .setStateError(null)
                .build()

            homeUi?.showSearchResult(searchViewState.searchResult)
        }
        homeUi?.hideProgress()
    }

    private fun handlePaginatedResult(repositories: List<Repository>) {
        homeUi?.hideAllErrorState()

        if (repositories.isEmpty()) {
            homeUi?.showAllItemsLoaded()

            searchViewState = SearchViewState.Builder(searchViewState)
                .setLoadedAllPages(true)
                .build()
        } else {
            val newData = searchViewState.searchResult.toMutableList()
            newData.addAll(repositories)

            searchViewState = SearchViewState.Builder(searchViewState)
                .setSearchResult(newData)
                .setLoadedAllPages(false)
                .setStateError(null)
                .build()

            homeUi?.showNextPage(searchViewState.searchResult)
        }
        homeUi?.hideProgress()
    }

    private fun handleError(error: Throwable) {
        when (error) {
            is ResultNotFoundException -> homeUi?.showSearchError(error.queryString)
            is UnknownHostException -> homeUi?.showOfflineState()
            else -> homeUi?.showDefaultError()
        }

        searchViewState = SearchViewState.Builder(searchViewState)
            .setStateError(error)
            .build()

        homeUi?.hideProgress()
    }
}