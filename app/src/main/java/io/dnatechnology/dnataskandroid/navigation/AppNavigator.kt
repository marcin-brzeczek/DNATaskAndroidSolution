package io.dnatechnology.dnataskandroid.navigation

import androidx.navigation.NavController
import io.dnatechnology.dnataskandroid.presentation.products.navigation.ProductsPageNavigatonTarget
import io.dnatechnology.dnataskandroid.presentation.products.navigation.ProductsPageNavigator

/**
 * Hosts navigation logic that coordinates across all feature modules.
 *
 * &nbsp;
 *
 * To use, first define a Navigator in your feature module. Your Navigator should be a SAM interface.
 * ```kotlin
 * // make public so `:app` can access it
 * interface OnboardingNavigator {
 *     fun navigate(target: OnboardingNavigationTarget)
 * }
 *
 * sealed interface OnboardingNavigationTarget : NavigationTarget
 * ```
 *
 * Then, implement your Navigator in AppNavigator.
 * ```kotlin
 * class AppNavigator(
 *     // [...]
 * ) : OnboardingNavigator {
 *     override fun navigate(target: OnboardingNavigationTarget) {
 *         // [...]
 *     }
 * }
 * ```
 *
 * @property navController The nav controller for performing navigation.
 * @property onAppNavigate Special navigation events delegated to the hosting activity.
 */
class AppNavigator(
    private val navController: NavController,
) : ProductsPageNavigator
{ // add navigator classes here

    override fun navigate(target: ProductsPageNavigatonTarget) {
        when (target) {
            ProductsPageNavigatonTarget.Next -> TODO()
        }
    }

    // endregion
}
