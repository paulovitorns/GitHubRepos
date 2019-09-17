package br.com.githubrepos.data.search

class ResultNotFoundException(val queryString: String) : IllegalArgumentException()
