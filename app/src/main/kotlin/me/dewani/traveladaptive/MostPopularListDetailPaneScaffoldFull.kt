package me.dewani.traveladaptive

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.AnimatedPane
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.ListDetailPaneScaffoldRole

import androidx.compose.material3.adaptive.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun MostPopularListDetailPaneScaffoldFull() {
    // Currently selected item
    var selectedItem: MyItem? by rememberSaveable(stateSaver = MyItem.Saver) {
        mutableStateOf(null)
    }

    // Create the ListDetailPaneScaffoldState
    val navigator = rememberListDetailPaneScaffoldNavigator()

    ListDetailPaneScaffold(
        scaffoldState = navigator.scaffoldState,
        listPane = {
            AnimatedPane(Modifier) {
                MyList(
                    onItemClick = { id ->
                        // Set current item
                        selectedItem = id
                        // Display the detail pane
                        navigator.navigateTo(ListDetailPaneScaffoldRole.Detail)
                    },
                )
            }
        },
        detailPane = {
            AnimatedPane(Modifier) {
                // Show the detail pane content if selected item is available
                selectedItem?.let { item ->
                    MyDetails(item)
                }
            }
        },

        extraPane = {
            AnimatedPane(Modifier) {
                // Show the extra pane  content if selected item is available
                selectedItem?.let { item ->
                    MyDetails(item)
                }
            }
        }
    )
}

@Composable
fun MyList(
    onItemClick: (MyItem) -> Unit,
) {
    Card {
        LazyColumn {
            topInstagramVlogTravelDestinations.forEachIndexed { id, destinationPair ->
                item {
                    ListItem(
                        modifier = Modifier
                            .background(Color.Magenta)
                            .clickable {
                                onItemClick(MyItem(id))
                            },
                        headlineContent = {
                            Text(
                                text = destinationPair.first,
                            )
                        },
                    )
                }
            }
        }
    }
}
@Composable
fun MyDetails(item: MyItem) {
    val text = topInstagramVlogTravelDestinations[item.id]
    Card {
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = topInstagramVlogTravelDestinations[item.id].first,
                fontSize = 24.sp,
            )
            Spacer(Modifier.size(16.dp))
            Text(
                text = topInstagramVlogTravelDestinations[item.id].second,
            )
        }
    }
}
class MyItem(val id: Int) {
    companion object {
        val Saver: Saver<MyItem?, Int> = Saver(
            { it?.id },
            ::MyItem,
        )
    }
}

val topInstagramVlogTravelDestinations = arrayOf(
    Pair("Bali, Indonesia", "A tropical paradise known for its beaches, temples, and lush rice paddies."),
    Pair("Maldives", "A chain of islands in the Indian Ocean with crystal-clear waters, overwater bungalows, and pristine beaches."),
    Pair("Santorini, Greece", "A Greek island famous for its whitewashed buildings, blue-domed churches, and stunning sunsets."),
    Pair("Tulum, Mexico", "A bohemian beach town on the Yucatán Peninsula with ancient Mayan ruins, turquoise waters, and cenotes."),
    Pair("Cappadocia, Turkey", "A region in central Turkey known for its otherworldly landscapes of fairy chimneys, hot air balloon rides, and cave hotels."),
    Pair("Marrakech, Morocco", "A vibrant city in Morocco with bustling souks, ancient palaces, and colorful gardens."),
    Pair("Machu Picchu, Peru", "An ancient Inca citadel in Peru, nestled high in the Andes Mountains, offering breathtaking views and a fascinating history."),
    Pair("Iceland", "A land of fire and ice, with glaciers, volcanoes, waterfalls, and geothermal hot springs."),
    Pair("The Amalfi Coast, Italy", "A picturesque stretch of coastline in southern Italy known for its dramatic cliffs, colorful villages, and winding roads."),
    Pair("Dubai, United Arab Emirates", "A futuristic city in the United Arab Emirates with towering skyscrapers, luxurious shopping malls, and man-made islands."),
    Pair("Paris, France", "The City of Lights, known for its iconic landmarks like the Eiffel Tower, romantic ambiance, and world-class museums."),
    Pair("New York City, USA", "The Big Apple, a bustling metropolis with iconic sights like Times Square, Central Park, and the Statue of Liberty."),
    Pair("Tokyo, Japan", "A vibrant city that blends modern technology with traditional culture, famous for its neon lights, anime, and delicious cuisine."),
    Pair("Kyoto, Japan", "A city steeped in history and tradition, known for its ancient temples, serene gardens, and geisha districts."),
    Pair("Sydney, Australia", "A cosmopolitan city with iconic landmarks like the Sydney Opera House, stunning beaches, and a laid-back lifestyle."),
    Pair("London, England", "A historic city with royal palaces, iconic landmarks like Big Ben and Buckingham Palace, and a vibrant arts scene."),
    Pair("Amsterdam, Netherlands", "A charming city with canals, cobblestone streets, and a lively nightlife scene."),
    Pair("Barcelona, Spain", "A vibrant city on the Mediterranean coast known for its art, architecture, and delicious tapas."),
    Pair("Rome, Italy", "The Eternal City, home to ancient ruins, iconic landmarks like the Colosseum and Vatican City, and mouthwatering cuisine."),
    Pair("Phuket, Thailand", "Thailand's largest island, known for its pristine beaches, turquoise waters, and lively nightlife."),
    Pair("Bora Bora, French Polynesia", "An island paradise in French Polynesia with overwater bungalows, coral reefs, and crystal-clear lagoons."),
    Pair("Hawaii, USA", "A stunning archipelago with lush rainforests, volcanic landscapes, and world-class surfing beaches."),
    Pair("Banff National Park, Canada", "A breathtaking national park in the Canadian Rockies with turquoise lakes, snow-capped mountains, and glaciers."),
    Pair("Yosemite National Park, USA", "A majestic national park in California known for its towering granite cliffs, giant sequoia trees, and cascading waterfalls."),
    Pair("Grand Canyon National Park, USA", "One of the seven natural wonders of the world, a vast canyon carved by the Colorado River over millions of years."),
    Pair("Great Barrier Reef, Australia", "The world's largest coral reef system, home to a vibrant array of marine life, offering snorkeling and diving adventures."),
    Pair("Fiordland National Park, New Zealand", "A rugged national park in New Zealand with towering mountains, pristine fiords, and lush rainforests.")
)


