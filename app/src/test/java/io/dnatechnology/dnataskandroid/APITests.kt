package io.dnatechnology.dnataskandroid

import com.dnatechnology.learning.data.network.api.PaymentApiClient
import com.dnatechnology.learning.data.network.api.PurchaseApiClient
import com.dnatechnology.learning.data.network.requests.PaymentRequest
import com.dnatechnology.learning.data.network.requests.PaymentStatus
import com.dnatechnology.learning.data.network.requests.PurchaseConfirmRequest
import com.dnatechnology.learning.data.network.requests.PurchaseRequest
import com.dnatechnology.learning.data.network.requests.TransactionStatus
import io.dnatechnology.dnataskandroid.ui.api.data.*
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*

class PaymentAPITests {
    private val paymentAPI = com.dnatechnology.learning.data.network.api.PaymentApiClient()

    @Test
    fun whenCorrectDataThenSuccess() = runBlocking {
        // given
        val paymentRequest = com.dnatechnology.learning.data.network.requests.PaymentRequest(
            transactionID = "Tr1",
            amount = 33.66,
            currency = "EUR",
            cardToken = "Token"
        )

        // when
        val paymentResponse =  paymentAPI.pay(paymentRequest)

        // then
        assertEquals(paymentResponse.status, com.dnatechnology.learning.data.network.requests.PaymentStatus.SUCCESS)
    }

    @Test
    fun whenIncorrectAmountThenFail() = runBlocking {
        // given
        val paymentRequest = com.dnatechnology.learning.data.network.requests.PaymentRequest(
            transactionID = "Tr1",
            amount = 19.66,
            currency = "EUR",
            cardToken = "Token"
        )

        // when
        val paymentResponse =  paymentAPI.pay(paymentRequest)

        // then
        assertEquals(paymentResponse.status, com.dnatechnology.learning.data.network.requests.PaymentStatus.FAILED)
    }

    @Test
    fun whenIncorrectCurrencyThenFail() = runBlocking {
        // given
        val paymentRequest = com.dnatechnology.learning.data.network.requests.PaymentRequest(
            transactionID = "Tr1",
            amount = 22.66,
            currency = "PLN",
            cardToken = "Token"
        )

        // when
        val paymentResponse =  paymentAPI.pay(paymentRequest)

        // then
        assertEquals(paymentResponse.status, com.dnatechnology.learning.data.network.requests.PaymentStatus.FAILED)
    }

    @Test
    fun whenRevertingCorrectAmountThenSuccess() = runBlocking {
        // given
        val paymentRequest = com.dnatechnology.learning.data.network.requests.PaymentRequest(
            transactionID = "Tr1",
            amount = 12.66,
            currency = "EUR",
            cardToken = "Token"
        )

        // when
        val paymentResponse =  paymentAPI.revert(paymentRequest)

        // then
        assertEquals(paymentResponse.status, com.dnatechnology.learning.data.network.requests.PaymentStatus.SUCCESS)
    }

    @Test
    fun whenRevertingIncorrectAmountThenFail() = runBlocking {
        // given
        val paymentRequest = com.dnatechnology.learning.data.network.requests.PaymentRequest(
            transactionID = "Tr1",
            amount = 0.66,
            currency = "EUR",
            cardToken = "Token"
        )

        // when
        val paymentResponse =  paymentAPI.revert(paymentRequest)

        // then
        assertEquals(paymentResponse.status, com.dnatechnology.learning.data.network.requests.PaymentStatus.FAILED)
    }
}

class PurchaseAPITests {
    private val purchaseApiClient = com.dnatechnology.learning.data.network.api.PurchaseApiClient()

    @Test
    fun whenGetProductsThenSuccess() = runBlocking {
        // when
        val products =  purchaseApiClient.getProducts()

        // then
        assertEquals(products.size, 5)
    }

    @Test
    fun whenInitiateEmptyOrderThenFail() = runBlocking {
        // given
       val purchaseRequest = com.dnatechnology.learning.data.network.requests.PurchaseRequest(
           mapOf()
       )

        // when
        val purchaseResponse =  purchaseApiClient.initiatePurchaseTransaction(purchaseRequest)

        // then
        assertEquals(purchaseResponse.transactionStatus, com.dnatechnology.learning.data.network.requests.TransactionStatus.FAILED)
    }

    @Test
    fun whenInitiateOrderWithZeroItemsThenFail() = runBlocking {
        // given
        val purchaseRequest = com.dnatechnology.learning.data.network.requests.PurchaseRequest(
            mapOf("12345" to 0)
        )

        // when
        val purchaseResponse =  purchaseApiClient.initiatePurchaseTransaction(purchaseRequest)

        // then
        assertEquals(purchaseResponse.transactionStatus, com.dnatechnology.learning.data.network.requests.TransactionStatus.FAILED)
    }

    @Test
    fun whenInitiateOrderWithToManyItemsThenFail() = runBlocking {
        // given
        val purchaseRequest = com.dnatechnology.learning.data.network.requests.PurchaseRequest(
            mapOf("12348" to 2001)
        )

        // when
        val purchaseResponse =  purchaseApiClient.initiatePurchaseTransaction(purchaseRequest)

        // then
        assertEquals(purchaseResponse.transactionStatus, com.dnatechnology.learning.data.network.requests.TransactionStatus.FAILED)
    }

    @Test
    fun whenConfirmrderWithToManyItemsThenFail() = runBlocking {
        // given
        val purchaseRequest = com.dnatechnology.learning.data.network.requests.PurchaseConfirmRequest(
            mapOf("12348" to 2001),
            "Tr2"
        )

        // when
        val purchaseResponse =  purchaseApiClient.confirm(purchaseRequest)

        // then
        assertEquals(purchaseResponse.status, com.dnatechnology.learning.data.network.requests.TransactionStatus.FAILED)
    }
}