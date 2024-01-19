package me.dewani.traveladaptive

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.MaterialTheme

import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigation.suite.ExperimentalMaterial3AdaptiveNavigationSuiteApi
import androidx.compose.material3.adaptive.navigation.suite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigation.suite.NavigationSuiteScaffoldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import me.dewani.traveladaptive.ui.theme.ComposeMpTheme

class MainActivity : ComponentActivity() {

    @SuppressLint("StateFlowValueCalledInComposition", "UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeMpTheme {
                JetTravelAdaptiveApp()
            }
        }
    }
}


@OptIn(ExperimentalMaterial3AdaptiveNavigationSuiteApi::class,
    ExperimentalMaterial3AdaptiveApi::class
)
@Composable
fun JetTravelAdaptiveApp() {
    val navItems =  listOf("Most Popular", "Travel Planner", "Ask JetTravel")
    val icons = listOf(Icons.Filled.Place, Icons.Filled.Info, Icons.Filled.Star)
    var selectedItem by remember { mutableIntStateOf(0) }
    val route = listOf("main_screen", "second_screen", "third_screen")

    val navController = rememberNavController()

    //CAMAL -> bottom bar on phones, navrail on larger layouts
    NavigationSuiteScaffold(navigationSuiteItems = {
        navItems.forEachIndexed { index, navItem ->
            item(icon = { Icon(icons[index], contentDescription = navItem) },
                label = { Text(navItem) },
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    navController.navigate(route[index])
                }
            )
        }
    }) {
        // Screen content.
        Box(Modifier.safeDrawingPadding()) {
            Column(
                verticalArrangement = Arrangement.Center,
            ) {
                contentBody(navController)
            }
        }

    }
}



@Composable
fun contentBody(navController: NavHostController) {
    val viewModelStoreOwner: ViewModelStoreOwner = LocalContext.current as ViewModelStoreOwner
    val mainViewModel = ViewModelProvider(viewModelStoreOwner).get(MainViewModel::class.java)
    val mainViewModelState by mainViewModel.uiState.collectAsState()

    NavHost(navController, startDestination = "main_screen") {
        composable("main_screen") {
            //CAMAL based list and detail screen with vacation locations and description
            MostPopularListDetailPaneScaffoldFull()
        }
        composable("second_screen") {
            //Nothing useful, just image input
            ImageInputScreen()
        }
        composable("third_screen") {
            //Gemini API test
            ItinerarySuggestionScreen(mainViewModel, mainViewModelState)
        }
    }
}

@Composable
fun bottomBar(navController: NavHostController) {
    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf("Most Popular", "Travel Planner", "Ask JetTravel")
    val route = listOf("main_screen", "second_screen", "third_screen")
    val icons = listOf(Icons.Filled.Place, Icons.Filled.Info, Icons.Filled.Star)
    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(icon = {
                Icon(
                    icons[index], contentDescription = item
                )
            }, label = { Text(item) }, selected = selectedItem == index, onClick = {
                selectedItem = index
                navController.navigate(route[index])
            })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun AppAndroidPreview() {
    val navController = rememberNavController()
    Scaffold(
        topBar = {
            TopAppBar(colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary,
            ), title = {
                Text("JetTravel")
            })
        },
        content = {
            Column(
                modifier = Modifier.padding(it.calculateTopPadding()),
                verticalArrangement = Arrangement.Center,
            ) {
                contentBody(navController)
            }
        },
        bottomBar = { bottomBar(navController) },
    )
}