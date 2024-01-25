package io.dnatechnology.dnataskandroid.domain.usecases

import io.dnatechnology.dnataskandroid.domain.repository.PaymentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

internal class InitTransactionUseCase @Inject constructor(
    private val paymentRepository: PaymentRepository,
) {
    operator fun invoke(order:Map<String, Long>) = flow {
        emit(paymentRepository.initTransaction(order))
    }.flowOn(Dispatchers.IO)
}