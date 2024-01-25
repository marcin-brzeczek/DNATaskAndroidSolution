package io.dnatechnology.dnataskandroid.domain.usecases

import io.dnatechnology.dnataskandroid.service.cardReader.CardData
import io.dnatechnology.dnataskandroid.service.cardReader.CardReaderService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


internal class ReadCardUseCase @Inject constructor(
    private val cardReaderService: CardReaderService,
) {
    operator fun invoke(): Flow<CardData> = flow {
        emit(cardReaderService.readCard())
    }.flowOn(Dispatchers.IO)
}