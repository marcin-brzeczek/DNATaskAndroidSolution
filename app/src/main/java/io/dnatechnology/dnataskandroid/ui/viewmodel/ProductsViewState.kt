package io.dnatechnology.dnataskandroid.ui.viewmodel

data class ProductsViewState(
    val cards:List,
    val prodcuts:List,
){

    companion object {

        val INITIAL = ProductsViewState(
           cards =  emptyList(),
            prodcuts = emptyList(),
        )
    }
}