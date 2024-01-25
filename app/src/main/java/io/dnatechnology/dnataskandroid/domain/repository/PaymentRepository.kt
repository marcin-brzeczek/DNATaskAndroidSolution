package io.dnatechnology.dnataskandroid.domain.repository

import io.dnatechnology.dnataskandroid.data.network.requests.PaymentResponse
import io.dnatechnology.dnataskandroid.data.network.requests.PurchaseResponse
import io.dnatechnology.dnataskandroid.domain.model.Product

internal interface PaymentRepository {
    suspend fun getProducts(): List<Product>
    suspend fun initTransaction(order: Map<String, Long>): PurchaseResponse
    suspend fun pay(
        transactionId: String,
        cardToken: String,
        currency: String,
        total: Double
    ): PaymentResponse
}