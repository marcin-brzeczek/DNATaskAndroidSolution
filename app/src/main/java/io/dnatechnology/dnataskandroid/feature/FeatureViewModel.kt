package io.dnatechnology.dnataskandroid.feature

import androidx.lifecycle.ViewModel
import io.dnatechnology.dnataskandroid.feature.model.UiAction
import io.dnatechnology.dnataskandroid.feature.model.UiState
import io.dnatechnology.dnataskandroid.navigation.NavigationTarget
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow

abstract class FeatureViewModel<S : UiState, A : UiAction, N : NavigationTarget> : ViewModel() {
    /**
     * Provides the entire View's data. All operations on the data need to be done through here
     * to ensure we are Reactive.
     */
    abstract val state: StateFlow<S>

    /**
     * Processes a [UiAction] sent from the View.
     */
    abstract fun onAction(action: A)

    private val _navigation = Channel<N>(Channel.BUFFERED)
    val navigation = _navigation.receiveAsFlow()

    /**
     * Sets the [NavigationTarget] which indicates where the user should be navigated to.
     */
    suspend fun navigateTo(target: N) {
        _navigation.send(target)
    }
}
