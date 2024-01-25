package io.dnatechnology.dnataskandroid.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn


const val DEFAULT_FLOW_TIMEOUT_MS = 5_000L

/**
 * Convenience [ViewModel] wrapper around [stateIn].
 */
fun <T> Flow<T>.stateIn(viewModel: ViewModel, initialValue: T) = stateIn(
    scope = viewModel.viewModelScope,
    started = SharingStarted.WhileSubscribed(DEFAULT_FLOW_TIMEOUT_MS),
    initialValue = initialValue
)