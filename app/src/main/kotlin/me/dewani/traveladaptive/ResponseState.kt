package me.dewani.traveladaptive

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue


sealed interface ResponseState {

    /**
     * Empty state when the screen is first shown
     */
    data object Initial: ResponseState

    /**
     * Still loading
     */
    data object Loading: ResponseState

    /**
     * Text has been generated
     */
    data class Success(
        val outputText: String
    ): ResponseState

    /**
     * There was an error generating text
     */
    data class Error(
        val errorMessage: String
    ): ResponseState
}
