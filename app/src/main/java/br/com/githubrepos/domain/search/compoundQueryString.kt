package br.com.githubrepos.domain.search

// Following the GitHub API rules.
// This function will compound a query string for a specific
// language: https://help.github.com/en/articles/searching-for-repositories#search-by-language
fun String.compoundQueryString(): String {
    return "language:$this"
}
