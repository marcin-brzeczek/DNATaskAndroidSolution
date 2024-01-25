package io.dnatechnology.dnataskandroid.data.repository

import io.dnatechnology.dnataskandroid.data.network.api.PaymentApiClient
import io.dnatechnology.dnataskandroid.data.network.api.PurchaseApiClient
import io.dnatechnology.dnataskandroid.data.network.requests.PaymentRequest
import io.dnatechnology.dnataskandroid.data.network.requests.PaymentResponse
import io.dnatechnology.dnataskandroid.data.network.requests.PurchaseRequest
import io.dnatechnology.dnataskandroid.data.network.requests.PurchaseResponse
import io.dnatechnology.dnataskandroid.domain.repository.PaymentRepository
import javax.inject.Inject

internal class PaymentRepositoryImpl @Inject constructor(
    private val purchaseApiClient: PurchaseApiClient,
    private val paymentApiClient: PaymentApiClient,
) : PaymentRepository {

    override suspend fun getProducts(): List<io.dnatechnology.dnataskandroid.domain.model.Product> {
        return purchaseApiClient.getProducts()
    }

    override suspend fun initTransaction(order: Map<String, Long>): PurchaseResponse {
        return purchaseApiClient.initiatePurchaseTransaction(
            purchaseRequest = PurchaseRequest(
                order = order,
            )
        )
    }

    override suspend fun pay(
        transactionId: String,
        cardToken: String,
        currency: String,
        total: Double,
    ): PaymentResponse {
        return paymentApiClient.pay(
            paymentRequest = PaymentRequest(
                transactionID = transactionId,
                amount = total,
                currency = currency,
                cardToken = cardToken
            )
        )
    }
}