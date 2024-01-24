package io.dnatechnology.dnataskandroid.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dnatechnology.learning.data.network.api.PurchaseApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ProductsViewModel @Inject constructor(
    private val getProductsUseCase:  GetProductsUseCase,
): ViewModel() {

    val purchaseApiClient: com.dnatechnology.learning.data.network.api.PurchaseApiClient =
        com.dnatechnology.learning.data.network.api.PurchaseApiClient()

    private val state = MutableStateFlow(ProductsViewState.INITIAL)

    fun getProducts() {
        viewModelScope.launch {
            getProductsUseCase()
                .collect {
                    mutableProducts.value = it
                }
        }
    }

    fun addToCart(productID: String) {
        val newMap = mutableCart.value.toMutableMap()
        newMap[productID] = (mutableCart.value[productID] ?: 0L) + 1L

        mutableCart.value = newMap.toMap()
    }
}