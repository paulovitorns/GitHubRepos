package br.com.githubrepos.domain.search

import br.com.githubrepos.data.model.Item
import br.com.githubrepos.library.reactivex.SchedulerProvider
import br.com.githubrepos.library.reactivex.applySingleSchedulers
import io.reactivex.Single
import javax.inject.Inject

class GetGitHubRepositoriesByLanguage @Inject constructor(
    private val searchRepository: SearchRepository,
    private val schedulerProvider: SchedulerProvider
) {

    operator fun invoke(
        query: String = "kotlin",
        sort: String = "stars",
        order: String = "asc"
    ): Single<List<Item>> {

        // Following the GitHub API rules.
        // This function will compound a query string for a specific
        // language: https://help.github.com/en/articles/searching-for-repositories#search-by-language
        fun compoundQueryString(value: String): String {
            return "language:$value"
        }

        return searchRepository.fetchRepositories(
            query = compoundQueryString(query),
            sort = sort,
            order = order,
            page = 1,
            perPage = 10).map {
            it.items
        }.compose(applySingleSchedulers(schedulerProvider))
    }
}
