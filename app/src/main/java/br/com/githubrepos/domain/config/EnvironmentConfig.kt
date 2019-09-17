package br.com.githubrepos.domain.config

import br.com.githubrepos.BuildConfig
import br.com.githubrepos.library.extension.safeEnumValueOf
import javax.inject.Inject

enum class ConfigName {
    STAGING, PRODUCTION
}

class EnvironmentConfig @Inject constructor() {

    private val configName: ConfigName = safeEnumValueOf(BuildConfig.FLAVOR, ConfigName.PRODUCTION)

    val baseUrl: String

    init {
        baseUrl = when (configName) {
            ConfigName.STAGING -> {
                "https://api.github.com/"
            }
            ConfigName.PRODUCTION -> {
                "https://api.github.com/"
            }
        }
    }
}
