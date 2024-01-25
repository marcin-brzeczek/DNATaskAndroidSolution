package io.dnatechnology.dnataskandroid.data.network.requests

internal data class PaymentRequest(
    val transactionID: String,
    val amount: Double,
    val currency: String,
    val cardToken: String
)

internal data class PaymentResponse(val transactionID: String, val status: PaymentStatus)

internal enum class PaymentStatus {
    SUCCESS,
    FAILED
}
