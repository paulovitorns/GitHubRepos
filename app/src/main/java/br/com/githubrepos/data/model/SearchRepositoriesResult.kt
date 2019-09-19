package br.com.githubrepos.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchRepositoriesResult(
    @SerialName("total_count")
    val totalCount: Int = 0,
    @SerialName("incomplete_results")
    val incompleteResults: Boolean = false,
    @SerialName("items")
    val repositories: List<Repository> = emptyList()
)

@Serializable
data class Repository(
    val id: Long = 0,
    val archived: Boolean = false,
    @SerialName("forks_count")
    val forksCount: Long = 0,
    @SerialName("full_name")
    val fullName: String = "",
    val language: String = "",
    val name: String = "",
    val private: Boolean = false,
    @SerialName("stargazers_count")
    val stargazersCount: Long = 0,
    @SerialName("watchers_count")
    val watchersCount: Long = 0,
    @SerialName("html_url")
    val htmlUrl: String = "",
    val owner: Owner
)

@Serializable
data class Owner(
    @SerialName("avatar_url")
    val avatarUrl: String = "",
    val login: String = "",
    @SerialName("html_url")
    val htmlUrl: String = ""
)
