package br.com.githubrepos.domain.search

import br.com.githubrepos.data.model.Repository
import br.com.githubrepos.data.model.Owner
import java.util.Date

fun mockedItem(): Repository = Repository(createdAt = Date(), defaultBranch = "master", owner = Owner())
