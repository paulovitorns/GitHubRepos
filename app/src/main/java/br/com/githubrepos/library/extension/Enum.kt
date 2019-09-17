package br.com.githubrepos.library.extension

inline fun <reified T : Enum<T>> safeEnumValueOf(name: String?, defaultValue: T): T {
    return try {
        name?.toUpperCase()
            ?.replace('-', '_')
            ?.let { enumValueOf<T>(it) }
            ?: defaultValue
    } catch (e: Exception) {
        defaultValue
    }
}

// Force `when` statements to be exhaustive
val <T> T.exhaustive: T get() = this
