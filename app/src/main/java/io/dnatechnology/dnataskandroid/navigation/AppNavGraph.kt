package io.dnatechnology.dnataskandroid.navigation

import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.spec.NavGraphSpec
import com.ramcosta.composedestinations.spec.Route
import io.dnatechnology.dnataskandroid.navigation.destinations.ProductsPageDestination

/**
 * Nav graph containing all destinations in the app.
 */
internal object AppNavGraph : NavGraphSpec {

    val firstPageRoute: Route = ProductsPageDestination

    override val route: String = "root"

    override val startRoute: Route = firstPageRoute

    override val destinationsByRoute = listOf<DestinationSpec<*>>(
        ProductsPageDestination,
    ).associateBy {
        it.route
    }
}