package br.com.githubrepos.screens.home

import br.com.githubrepos.data.model.Repository

sealed class PartialStateChanged {
    object Loading : PartialStateChanged()
    data class SearchRepositoryLoaded(val repositories: List<Repository>) : PartialStateChanged()
    data class NextPageLoaded(val repositories: List<Repository>) : PartialStateChanged()
    data class LastViewStateRestored(val lastViewState: SearchViewState) : PartialStateChanged()
    data class StateError(val error: Throwable, val queryString: String? = null) :
        PartialStateChanged()
}
