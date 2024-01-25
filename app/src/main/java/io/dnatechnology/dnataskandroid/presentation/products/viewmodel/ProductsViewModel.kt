package io.dnatechnology.dnataskandroid.presentation.products.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.dnatechnology.dnataskandroid.data.network.requests.TransactionStatus
import io.dnatechnology.dnataskandroid.domain.model.Product
import io.dnatechnology.dnataskandroid.domain.usecases.GetProductsUseCase
import io.dnatechnology.dnataskandroid.domain.usecases.InitTransactionUseCase
import io.dnatechnology.dnataskandroid.domain.usecases.PayUseCase
import io.dnatechnology.dnataskandroid.domain.usecases.ReadCardUseCase
import io.dnatechnology.dnataskandroid.feature.FeatureViewModel
import io.dnatechnology.dnataskandroid.presentation.products.navigation.ProductsPageNavigatonTarget
import io.dnatechnology.dnataskandroid.presentation.products.ui.ProductsAction
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ProductsViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val initTransactionUseCase: InitTransactionUseCase,
    private val readCardUseCase: ReadCardUseCase,
    private val payUseCase: PayUseCase,
) : FeatureViewModel<ProductsUiState, ProductsAction, ProductsPageNavigatonTarget>() {

    private val mutableState: MutableStateFlow<ProductsUiState> = MutableStateFlow(ProductsUiState.Loading)

    override val state = mutableState.asStateFlow()

    private val _notice = MutableSharedFlow<String>()
    val notice = _notice.asSharedFlow()

    init {
        fetchProducts()
    }

    private fun fetchProducts() = viewModelScope.launch {
        getProductsUseCase()
            .collect { products ->
                mutableState.update {
                    ProductsUiState.Data(
                        products = products
                    )
                }
            }
    }


    override fun onAction(action: ProductsAction) {
        when (action) {
            ProductsAction.Pay -> startPaymentProcess()
            is ProductsAction.AddToCart -> addToCart(
                productID = action.productId,
            )

            ProductsAction.BackToProducts -> {
                mutableState.update {  ProductsUiState.Loading}
                fetchProducts()
            }
        }
    }

    private fun startPaymentProcess() {
        val selectedProducts = (state.value as? ProductsUiState.Data)
            ?.products
            ?.filter { it.isSelected }
            .orEmpty()

        if (selectedProducts.isEmpty()) {
            viewModelScope.launch {
                _notice.emit("Please select product")
            }
        } else {
            mutableState.update { ProductsUiState.Data.PaymentInitilization(selectedProducts) }
            viewModelScope.launch {
                val orderMap = selectedProducts.map { it.productID to it.maxAmount }.toMap()
                initTransactionUseCase(order = orderMap)
                    .catch {
                        mutableState.update {
                            ProductsUiState.Data.PaymentError(
                                "INIT TRANSACTION ERROR"
                            )
                        }
                    }
                    .collect {
                        when (it.transactionStatus) {
                            TransactionStatus.INITIATED -> {
                                mutableState.update { ProductsUiState.Data.ReadingCard }
                                readCard(
                                    transactionId = it.transactionID,
                                    selectedProducts = selectedProducts,
                                )
                            }

                            TransactionStatus.CANCELLED ->
                                mutableState.update {
                                    ProductsUiState.Data.PaymentError(
                                        "TRANSACTION CANCELLED"
                                    )
                                }

                            TransactionStatus.FAILED -> {
                                mutableState.update {
                                    ProductsUiState.Data.PaymentError(
                                        "TRANSACTION FAILED"
                                    )
                                }
                            }

                            TransactionStatus.CONFIRMED -> Unit
                        }
                    }
            }
        }
    }

    private fun readCard(transactionId: String, selectedProducts: List<Product>) {
        val totalAmount = selectedProducts.sumOf { it.maxAmount }.toDouble()

        //todo there should be a way to pass currency as initial value to avoid having mixed currency for each product
        val currency = selectedProducts.firstOrNull()?.unitValueCurrency.orEmpty()

        viewModelScope.launch {
            readCardUseCase()
                .catch {
                    mutableState.update { ProductsUiState.Data.PaymentError("READING CARD ERROR") }
                }
                .collect { cardData ->
                    pay(
                        transactionId = transactionId,
                        cardToken = cardData.token,
                        total = totalAmount,
                        currency = currency,
                    )
                }

        }
    }

    private fun pay(transactionId: String, cardToken: String, total: Double, currency: String) {
        mutableState.update { ProductsUiState.Data.PaymentProcessing }
        viewModelScope.launch {
            payUseCase(
                transactionId = transactionId,
                cardToken = cardToken,
                currency = currency,
                total = total
            )
                .catch {
                    mutableState.update { ProductsUiState.Data.PaymentError("PAYMENT FAILED") }
                }
                .collect {
                    mutableState.update { ProductsUiState.Data.PaymentSuccess(
                        amountWithUnit = "$total $currency"
                    ) }
                }
        }
    }

    private fun addToCart(productID: String) {
        val existingProductList = (state.value as? ProductsUiState.Data)?.products.orEmpty()
        mutableState.update {
            ProductsUiState.Data(
                products = existingProductList.map {
                    when {
                        it.isSelected && it.productID != productID -> it.copy(isSelected = true)
                        it.isSelected && it.productID == productID -> it.copy(isSelected = false)
                        else -> it.copy(isSelected = it.productID == productID)
                    }
                })
        }

    }
}