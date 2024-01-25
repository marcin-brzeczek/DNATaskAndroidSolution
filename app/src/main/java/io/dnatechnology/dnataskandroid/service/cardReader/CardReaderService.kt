package io.dnatechnology.dnataskandroid.service.cardReader

import kotlinx.coroutines.delay
import java.util.*
import java.util.Calendar.SECOND
import javax.inject.Inject

class CardReaderService @Inject constructor(){

    suspend fun readCard(): CardData {
        val second = Calendar.getInstance().get(SECOND)

        if (second <= 5) {
            // User will need some time to use the card
            delay(4000)
            return CardData(UUID.randomUUID().toString())
        }

        throw CardReaderException()
    }

}

class CardReaderException : Throwable("Could not read card data")

data class CardData(val token: String)
