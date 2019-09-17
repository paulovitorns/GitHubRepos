package br.com.githubrepos.data.model

import br.com.githubrepos.library.serializer.DateSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class SearchRepositoriesResult(
    @SerialName("total_count")
    val totalCount: Int = 0,
    @SerialName("incomplete_results")
    val incompleteResults: Boolean = false,
    val items: List<Item> = emptyList()
)

@Serializable
data class Item(
    val archived: Boolean = false,
    @Serializable(with = DateSerializer::class)
    @SerialName("created_at")
    val createdAt: Date,
    @SerialName("default_branch")
    val defaultBranch: String,
    val description: String = "",
    val disabled: Boolean = false,
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
