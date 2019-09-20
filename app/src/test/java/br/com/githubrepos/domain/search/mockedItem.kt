package br.com.githubrepos.domain.search

import br.com.githubrepos.data.model.Owner
import br.com.githubrepos.data.model.Repository

fun mockedItem(): Repository = Repository(owner = Owner())
