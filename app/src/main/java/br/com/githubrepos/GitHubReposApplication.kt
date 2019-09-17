package br.com.githubrepos

import br.com.githubrepos.library.di.DaggerGitHubReposComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class GitHubReposApplication : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerGitHubReposComponent.builder()
            .application(this)
            .build()
    }
}
