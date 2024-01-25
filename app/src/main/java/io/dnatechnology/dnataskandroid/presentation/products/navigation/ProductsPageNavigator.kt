package io.dnatechnology.dnataskandroid.presentation.products.navigation

import io.dnatechnology.dnataskandroid.navigation.NavigationTarget

sealed interface ProductsPageNavigatonTarget : NavigationTarget {
    object Next : ProductsPageNavigatonTarget

}

interface ProductsPageNavigator {
    fun navigate(target: ProductsPageNavigatonTarget)
}