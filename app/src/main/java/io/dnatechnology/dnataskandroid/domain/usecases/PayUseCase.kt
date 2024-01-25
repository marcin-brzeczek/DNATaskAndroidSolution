package io.dnatechnology.dnataskandroid.domain.usecases

import io.dnatechnology.dnataskandroid.domain.repository.PaymentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

internal class PayUseCase @Inject constructor(
    private val paymentRepository: PaymentRepository,
) {
    operator fun invoke(
        transactionId: String,
        cardToken: String,
        currency: String,
        total: Double,
    ) = flow {
        emit(paymentRepository.pay(transactionId, cardToken, currency, total))
    }.flowOn(Dispatchers.IO)
}