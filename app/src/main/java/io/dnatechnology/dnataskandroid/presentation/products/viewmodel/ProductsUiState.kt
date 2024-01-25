package io.dnatechnology.dnataskandroid.presentation.products.viewmodel

import io.dnatechnology.dnataskandroid.domain.model.Product
import io.dnatechnology.dnataskandroid.feature.model.UiState

internal sealed interface ProductsUiState : UiState {
    object Loading : ProductsUiState
    data class Data(
        val products: List<Product>,
    ) : ProductsUiState {
        data class PaymentSuccess(val amountWithUnit:String) : ProductsUiState
        data class PaymentError(val errorMessage: String) : ProductsUiState
        data class PaymentInitilization(val selectedProducts: List<Product>) : ProductsUiState
        object PaymentProcessing : ProductsUiState
        object ReadingCard : ProductsUiState
    }
    object Empty: ProductsUiState
}