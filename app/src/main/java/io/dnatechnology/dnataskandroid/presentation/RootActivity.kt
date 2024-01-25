package io.dnatechnology.dnataskandroid.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.scope.DestinationScopeWithNoDependencies
import dagger.hilt.android.AndroidEntryPoint
import io.dnatechnology.dnataskandroid.navigation.AppNavigator
import io.dnatechnology.dnataskandroid.navigation.NavGraphs
import io.dnatechnology.dnataskandroid.presentation.theme.DNATaskAndroidTheme

@AndroidEntryPoint
class RootComposeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DNATaskAndroidTheme {
                val navController = rememberNavController()

                Scaffold(
                    contentWindowInsets = WindowInsets(0, 0, 0, 0)
                ) { padding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                            .consumeWindowInsets(padding)
                            .windowInsetsPadding(
                                WindowInsets.safeDrawing.only(
                                    WindowInsetsSides.Horizontal
                                )
                            )
                    ) {
                        DestinationsNavHost(
                            navGraph = NavGraphs.root,
                            navController = navController,
                            dependenciesContainerBuilder = {
                                dependency(createNavigator())
                            },
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun DestinationScopeWithNoDependencies<*>.createNavigator(): AppNavigator {
        return AppNavigator(
            navController = navController,
        )
    }
}
