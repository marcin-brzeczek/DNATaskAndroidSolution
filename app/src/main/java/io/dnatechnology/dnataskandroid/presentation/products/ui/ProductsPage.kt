package io.dnatechnology.dnataskandroid.presentation.products.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import io.dnatechnology.dnataskandroid.R
import io.dnatechnology.dnataskandroid.domain.model.Product
import io.dnatechnology.dnataskandroid.presentation.products.navigation.ProductsPageNavigator
import io.dnatechnology.dnataskandroid.presentation.products.viewmodel.ProductsUiState
import io.dnatechnology.dnataskandroid.presentation.products.viewmodel.ProductsViewModel
import io.dnatechnology.dnataskandroid.presentation.theme.Black
import io.dnatechnology.dnataskandroid.presentation.theme.DNATaskAndroidTheme
import io.dnatechnology.dnataskandroid.presentation.theme.White

@RootNavGraph(start = true)
@Destination
@Composable
internal fun ProductsPage(
    navigator: ProductsPageNavigator,
    viewModel: ProductsViewModel = hiltViewModel()
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    ProductsPage(
        uiState = uiState,
        onAction = viewModel::onAction,
    )

    LaunchedEffect(Unit) {
        viewModel.notice.collect { noticeData ->
            Toast.makeText(context, noticeData, Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.navigation.collect {
            navigator.navigate(it)
        }
    }
}


@Composable
private fun ProductsPage(
    uiState: ProductsUiState,
    onAction: (ProductsAction) -> Unit,
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (uiState) {
            ProductsUiState.Loading -> InformationBox("LOADING")
            ProductsUiState.Data.ReadingCard -> InformationBox("READING CARD...")
            is ProductsUiState.Data.PaymentInitilization -> InformationBox("PAYMENT INITIALIZATION...")
            is ProductsUiState.Data.PaymentProcessing -> PaymentProcessingContent()
            is ProductsUiState.Data -> ProductsContent(
                uiState = uiState,
                onAction = onAction,
            )

            is ProductsUiState.Data.PaymentError -> ResultBox(
                isSuccess = false,
                message = uiState.errorMessage,
                onAction = onAction,
            )

            is ProductsUiState.Data.PaymentSuccess -> ResultBox(
                isSuccess = true,
                "PAYMENT SUCCESS! \n ${uiState.amountWithUnit}",
                onAction = onAction,
            )

            ProductsUiState.Empty -> InformationBox("Sorry we don't have products for you :(")
        }
    }
}

@Composable
private fun ProductsContent(
    uiState: ProductsUiState.Data,
    onAction: (ProductsAction) -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(top = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        for (product in uiState.products) {
            val productBackroundColor = if (product.isSelected) Color.Green else Color.White
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 8.dp)
                    .background(productBackroundColor)
                    .border(1.dp, Black)
                    .clickable {
                        onAction(ProductsAction.AddToCart(product.productID))
                    }
            ) {

                Text(
                    text = product.toString(),
                    color = Black,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .padding(vertical = 8.dp)
                )
            }
        }
    }
    Button(
        text = stringResource(R.string.pay),
        onClick = {
            onAction(ProductsAction.Pay)
        }
    )
}

@Composable
private fun Button(
    text: String,
    onClick: () -> Unit,
) {
    Row(
        Modifier
            .background(White)
            .fillMaxWidth()
            .padding(16.dp)
            .height(50.dp)
            .border(1.dp, Black)
            .clickable {
                onClick()
            },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = text, color = Black)
    }
}

@Composable
private fun ResultBox(
    isSuccess: Boolean,
    message: String,
    onAction: (ProductsAction) -> Unit,
) {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            if (isSuccess) {
                Icon(
                    modifier = Modifier.size(48.dp),
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    tint = Color.Green,
                )
            } else {
                Icon(
                    modifier = Modifier.size(48.dp),
                    imageVector = Icons.Filled.Close,
                    contentDescription = null,
                    tint = Color.Red,
                )
            }
            Spacer(Modifier.height(16.dp))
            Text(
                textAlign = TextAlign.Center,
                text = message
            )
            Spacer(Modifier.height(16.dp))
            Button(text = "Back to Product list",
                onClick = {
                    onAction(ProductsAction.BackToProducts)
                })
        }
    }
}

@Composable
private fun InformationBox(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = message
        )
    }
}

@Composable
private fun PaymentProcessingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "PAYMENT PROCESSING"
            )
            Spacer(Modifier.height(8.dp))
            CircularProgressIndicator()
        }
    }
}


//previews
internal class ProductsPagePreviewParameterProvider : PreviewParameterProvider<ProductsUiState> {
    override val values: Sequence<ProductsUiState> = sequenceOf(
        ProductsUiState.Loading,
        ProductsUiState.Empty,
        ProductsUiState.Data(
            products = mockedProducts(),
        ),
        ProductsUiState.Data(
            products = mockedProducts()
                .map {
                    if (it.productID == "2") it.copy(isSelected = true) else it
                },
        ),
        ProductsUiState.Data.PaymentInitilization(
            selectedProducts = mockedProducts().filter { it.productID.toInt() > 1 }
        ),
        ProductsUiState.Data.PaymentSuccess("100 EUR"),
        ProductsUiState.Data.PaymentError("PAYMENT FAILED")
    )

    private fun mockedProducts() = (1..3).map { index ->
        Product(
            index.toString(),
            "Test_$index",
            10L + index,
            30.0 + index,
            "PLN",
            3.0 + index,
            false
        )
    }

}

@Preview(showBackground = true)
@Composable
internal fun ProductsPagePreview(
    @PreviewParameter(ProductsPagePreviewParameterProvider::class) state: ProductsUiState
) {
    DNATaskAndroidTheme {
        ProductsPage(
            uiState = state,
            onAction = {},
        )
    }
}


