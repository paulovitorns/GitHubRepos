package br.com.githubrepos.screens.home

import br.com.githubrepos.data.model.Repository

data class SearchViewState(
    // stored the first query string to be used in case of lost connection before load the first page
    val lastQueryString: String = "",
    // stored the current page we're showing/loading
    val currentPage: Int = 0,
    // stored the current page we're showing/loading
    val sort: String = "",
    // stored the current page we're showing/loading
    val order: String = "",
    // indicates that's has loaded all the pages
    val hasLoadedAllPages: Boolean = false,
    // used to store data result from search repositories or loaded a next repositories page
    val searchResult: List<Repository> = emptyList(),
    // indicates that's occurs some error while loading the next page
    val stateError: Throwable? = null
) {

    fun builder(): Builder {
        return Builder(this)
    }

    class Builder(searchViewState: SearchViewState) {
        private var lastQueryString: String = searchViewState.lastQueryString
        private var currentPage: Int = searchViewState.currentPage
        private var sort: String = searchViewState.sort
        private var order: String = searchViewState.order
        private var hasLoadedAllPages = searchViewState.hasLoadedAllPages
        private var searchResult: List<Repository> = searchViewState.searchResult
        private var stateError: Throwable? = searchViewState.stateError

        fun setLastQueryString(queryString: String): Builder {
            this.lastQueryString = queryString
            return this
        }

        fun increaseCurrentPage(): Builder {
            this.currentPage++
            return this
        }

        fun setSort(sort: String): Builder {
            this.sort = sort
            return this
        }

        fun setOrder(order: String): Builder {
            this.order = order
            return this
        }

        fun setLoadedAllPages(flag: Boolean): Builder {
            this.hasLoadedAllPages = flag
            return this
        }

        fun setSearchResult(searchResult: List<Repository>): Builder {
            this.searchResult = searchResult
            return this
        }

        fun setStateError(error: Throwable?): Builder {
            this.stateError = error
            return this
        }

        fun build(): SearchViewState {
            return SearchViewState(
                lastQueryString,
                currentPage,
                sort,
                order,
                hasLoadedAllPages,
                searchResult,
                stateError
            )
        }
    }
}
