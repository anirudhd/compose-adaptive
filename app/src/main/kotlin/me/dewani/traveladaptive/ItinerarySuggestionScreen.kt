package me.dewani.traveladaptive

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.core.content.ContextCompat
import dev.jeziellago.compose.markdowntext.MarkdownText


/**
 * Builds the UI for accepting a country, a time period and the vacation type from the user
 */
@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun ItinerarySuggestionScreen(
    mainViewModel: MainViewModel,
    uiState: ResponseState = ResponseState.Loading
) {


    var expanded by remember { mutableStateOf(false) }
    var preference by remember { mutableStateOf("Adventure") }
    var weeks by remember { mutableStateOf(1) }
    var country by remember { mutableStateOf("Morocco") }
    val state = rememberListDetailPaneScaffoldNavigator()


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize(),
    ) {

        OutlinedTextField(
            value = country,
            shape = RoundedCornerShape(12.dp),
            onValueChange = { country = it },
            label = { Text("Country or City") }
        )

        OutlinedTextField(
            shape = RoundedCornerShape(12.dp),
            value = preference,
            onValueChange = { preference = it },
            label = { Text("Preference") }
        )

        Box() {
            var textfieldSize by remember {
                mutableStateOf(
                    Size(
                        0.0f,
                        0f
                    )
                )
            }
            OutlinedTextField(
                value = "" + weeks,
                onValueChange = { weeks = it.toInt() },
                enabled = false,
                modifier = Modifier.onGloballyPositioned { coordinates ->
                    //This value is used to assign to the DropDown the same width
                    textfieldSize = coordinates.size.toSize()
                }.clickable { expanded = !expanded },
                shape = RoundedCornerShape(12.dp),
                label = { Text("How many weeks?") }
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .width(with(LocalDensity.current) { textfieldSize.width.toDp() })

            ) {

                arrayOf(
                    "1 week",
                    "2 weeks",
                    "3 weeks",
                    "4 weeks",
                    "5 weeks"
                ).forEachIndexed { index, it ->
                    DropdownMenuItem(
                        text = { Text(it) },
                        onClick = {
                            weeks = (index + 1)
                            expanded = false
                        }
                    )
                }
            }
        }
        when (uiState) {

            ResponseState.Initial -> {
                suggestItineraryButton(country, preference, weeks, mainViewModel, uiState)
            }

            ResponseState.Loading -> {
                suggestItineraryButton(country, preference, weeks, mainViewModel, uiState)
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .padding(all = 8.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    CircularProgressIndicator()
                }
            }

            is ResponseState.Success -> {
                MarkdownText(
                    markdown = uiState.outputText,
                    color = MaterialTheme.colorScheme.onSurface
                )
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.google.com/search?q=google flights $weeks flights from SFO to $country")
                )
                val context = LocalContext.current
                Button(onClick = { ContextCompat.startActivity(context, intent, null) }) {
                    Text("Show Flights")
                }

            }

            is ResponseState.Error -> {
                MarkdownText(
                    markdown = uiState.errorMessage,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

/**
 * Composable for a button and the loading indicator. The button onclick triggers Gemini API call.
 */
@Composable
fun suggestItineraryButton(
    country: String,
    preference: String,
    weeks: Int,
    mainViewModel: MainViewModel,
    mainViewModelState: ResponseState,
) {
    var prompt by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }

    Button(onClick = {
        loading = true
        prompt =
            "Share an ideal $weeks week itinerary for  $country travel that involves $preference"
        mainViewModel.onSuggestClick(prompt)
    }, enabled = !loading, modifier = Modifier.clip(MaterialTheme.shapes.large)) {
        Text("Suggest Itinerary")
    }

    if (!loading) return

    CircularProgressIndicator(
        modifier = Modifier.width(64.dp),
        color = MaterialTheme.colorScheme.secondary,
        trackColor = MaterialTheme.colorScheme.surfaceVariant,
    )
}
