package io.dnatechnology.dnataskandroid.domain.usecases

import io.dnatechnology.dnataskandroid.domain.repository.PaymentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

internal class GetProductsUseCase @Inject constructor(
    private val paymentRepository: PaymentRepository,
) {
    operator fun invoke() = flow {
        emit(paymentRepository.getProducts())
    }.flowOn(Dispatchers.IO)
}