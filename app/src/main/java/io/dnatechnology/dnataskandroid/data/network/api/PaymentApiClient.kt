package io.dnatechnology.dnataskandroid.data.network.api

import io.dnatechnology.dnataskandroid.data.network.requests.PaymentRequest
import io.dnatechnology.dnataskandroid.data.network.requests.PaymentResponse
import io.dnatechnology.dnataskandroid.data.network.requests.PaymentStatus
import kotlinx.coroutines.delay
import javax.inject.Inject

internal class PaymentApiClient @Inject constructor() {
    /**
     * Call this method to execute payment on the account connected with provided card token
     */
    suspend fun pay(paymentRequest: PaymentRequest): PaymentResponse {
        delay(2500)

        return if (paymentRequest.currency == "EUR" && paymentRequest.amount >= 20.00 ) {
            PaymentResponse(
                paymentRequest.transactionID,
                PaymentStatus.SUCCESS
            )
        } else {
            PaymentResponse(
                paymentRequest.transactionID,
                PaymentStatus.FAILED
            )
        }
    }

    /**
     * Method meant for reverting payment when transaction could not be completed by the merchant.
      */
    suspend fun revert(paymentRequest: PaymentRequest): PaymentResponse {
        delay(500)
        return if (paymentRequest.amount >= 1.00 ) {
            PaymentResponse(
                paymentRequest.transactionID,
                PaymentStatus.SUCCESS
            )
        } else {
            PaymentResponse(
                paymentRequest.transactionID,
                PaymentStatus.FAILED
            )
        }
    }
}