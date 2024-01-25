package com.dnatechnology.learning.data.di

import io.dnatechnology.dnataskandroid.data.network.api.PurchaseApiClient
import io.dnatechnology.dnataskandroid.data.repository.PaymentRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.dnatechnology.dnataskandroid.data.network.api.PaymentApiClient
import io.dnatechnology.dnataskandroid.domain.repository.PaymentRepository
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
internal class DataModule {

    @Provides
    @Singleton
    fun provideProductsRepository(
        purchaseApiClient: PurchaseApiClient,
        paymentApiClient: PaymentApiClient,
    ): PaymentRepository = PaymentRepositoryImpl(
        purchaseApiClient = purchaseApiClient,
        paymentApiClient = paymentApiClient
    )

}