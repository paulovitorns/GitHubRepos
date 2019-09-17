package br.com.githubrepos.library.retrofit.endpoint

import br.com.githubrepos.data.model.SearchRepositoriesResult
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SearchRepositoriesEndpoint {

    @GET("search/repositories")
    fun searchRepositories(
        @Path("q") query: String,
        @Query("sort") sort: String,
        @Query("order") order: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Single<Response<SearchRepositoriesResult>>
}
