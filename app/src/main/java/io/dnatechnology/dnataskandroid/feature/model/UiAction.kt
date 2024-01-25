package io.dnatechnology.dnataskandroid.feature.model

interface UiAction

/**
 * An empty action for screens where the View does not produce any actions for the ViewModel.
 */
object EmptyAction : UiAction

private sealed interface SampleAction : UiAction {
    object BackClicked : SampleAction
}
