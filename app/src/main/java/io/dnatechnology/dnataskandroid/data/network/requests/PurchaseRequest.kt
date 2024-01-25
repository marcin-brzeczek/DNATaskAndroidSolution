package io.dnatechnology.dnataskandroid.data.network.requests

internal data class PurchaseRequest(val order: Map<String, Long>)

internal data class PurchaseResponse(
    val order: Map<String, Long>,
    val transactionID: String,
    val transactionStatus: TransactionStatus
)

internal data class PurchaseConfirmRequest(val order: Map<String, Long>, val transactionID: String)

internal data class PurchaseCancelRequest(val transactionID: String)

internal data class PurchaseStatusResponse(val transactionID: String, val status: TransactionStatus)

internal enum class TransactionStatus {
    INITIATED,
    CONFIRMED,
    CANCELLED,
    FAILED
}
