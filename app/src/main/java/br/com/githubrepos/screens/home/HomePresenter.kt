package br.com.githubrepos.screens.home

import br.com.githubrepos.data.model.Repository
import br.com.githubrepos.data.search.ResultNotFoundException
import br.com.githubrepos.domain.search.GetGitHubRepositoriesByLanguage
import br.com.githubrepos.domain.search.GetPaginatedGitHubRepositories
import br.com.githubrepos.library.reactivex.SchedulerProvider
import br.com.githubrepos.library.reactivex.addDisposableTo
import br.com.githubrepos.screens.BasePresenter
import br.com.githubrepos.screens.BaseUi
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class HomePresenter @Inject constructor(
    private val getGitHubRepositoriesByLanguage: GetGitHubRepositoriesByLanguage,
    private val getPaginatedGitHubRepositories: GetPaginatedGitHubRepositories,
    private val schedulerProvider: SchedulerProvider
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
        loadDefaultQuery()
        bindIntents()
    }

    private fun loadDefaultQuery() {
        getGitHubRepositoriesByLanguage(
            query = searchViewState.lastQueryString,
            sort = searchViewState.sort,
            order = searchViewState.order
        ).subscribe(
            { handleFirstPageResult(it) },
            { handleError(it) }
        ).addDisposableTo(disposeBag)
    }

    private fun bindIntents() {
        homeUi?.search()!!
            .debounce(1, TimeUnit.SECONDS, schedulerProvider.postWorkerThread())
            .filter { it.trim().isNotEmpty() }
            .filter { typed -> typed != searchViewState.lastQueryString }
            .flatMap { queryString ->
                homeUi?.showProgress()

                searchViewState = SearchViewState.Builder(searchViewState)
                    .setLastQueryString(queryString)
                    .build()

                getGitHubRepositoriesByLanguage(
                    query = searchViewState.lastQueryString,
                    sort = searchViewState.sort,
                    order = searchViewState.order
                )
            }
            .subscribe({ handleFirstPageResult(it) }, { handleError(it) })
            .addDisposableTo(disposeBag)

        homeUi?.loadNextPage()!!
            .filter { !searchViewState.hasLoadedAllPages }
            .flatMap {
                homeUi?.showProgress()

                searchViewState = SearchViewState.Builder(searchViewState)
                    .increaseCurrentPage()
                    .build()

                getPaginatedGitHubRepositories(
                    query = searchViewState.lastQueryString,
                    sort = searchViewState.sort,
                    order = searchViewState.order,
                    page = searchViewState.currentPage
                )
            }
            .subscribe({ handlePaginatedResult(it) }, { handleError(it) })
            .addDisposableTo(disposeBag)
    }

    private fun handleFirstPageResult(repositories: List<Repository>) {
        if (repositories.isEmpty()) {
            homeUi?.showSearchError(searchViewState.lastQueryString)
        } else {
            searchViewState = SearchViewState.Builder(searchViewState)
                .setSearchResult(repositories)
                .setStateError(null)
                .build()

            homeUi?.showSearchResult(searchViewState.searchResult)
        }
        homeUi?.hideProgress()
    }

    private fun handlePaginatedResult(repositories: List<Repository>) {
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