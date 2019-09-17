package br.com.githubrepos.domain.search

import br.com.githubrepos.data.model.Item
import br.com.githubrepos.data.model.Owner
import java.util.Date

fun mockedItem(): Item = Item(createdAt = Date(), defaultBranch = "master", owner = Owner())
