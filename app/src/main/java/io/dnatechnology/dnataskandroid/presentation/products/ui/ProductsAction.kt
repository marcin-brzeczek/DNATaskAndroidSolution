package io.dnatechnology.dnataskandroid.presentation.products.ui

import io.dnatechnology.dnataskandroid.feature.model.UiAction

internal sealed interface ProductsAction : UiAction {
    object Pay : ProductsAction
    object BackToProducts : ProductsAction
    data class AddToCart(val productId: String) : ProductsAction
}
