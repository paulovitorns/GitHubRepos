package br.com.githubrepos.library.state

import br.com.githubrepos.screens.BaseUi
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.reflect.KClass

typealias CurrentState = MutableMap<KClass<out BaseUi>, Any>

interface StateSaver {
    fun save(view: KClass<out BaseUi>, viewState: Any)
}

interface StateLoader {
    infix fun <STATE> load(view: KClass<out BaseUi>): STATE? where STATE : Any
}

@Singleton
class StateStore @Inject constructor() : StateSaver, StateLoader {

    private val currentState: CurrentState = HashMap()

    override fun save(view: KClass<out BaseUi>, viewState: Any) {
        currentState[view] = viewState
    }

    override fun <STATE : Any> load(view: KClass<out BaseUi>): STATE? {
        @Suppress("UNCHECKED_CAST")
        return currentState.remove(view) as? STATE
    }
}
