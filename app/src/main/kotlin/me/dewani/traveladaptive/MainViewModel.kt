package me.dewani.traveladaptive

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


class MainViewModel : ViewModel() {

    private val _uiState:  MutableStateFlow<ResponseState> = MutableStateFlow(ResponseState.Initial)
    val uiState: StateFlow<ResponseState> = _uiState.asStateFlow()
    val generativeModel = GenerativeModel(
        // For text-only input, use the gemini-pro model
        modelName = "gemini-pro",
        // Access your API key as a Build Configuration variable (see "Set up your API key" above)
        apiKey = BuildConfig.apiKey
    )
    // MutableState to handle our UI state
    fun onSuggestClick(prompt: String) {
        viewModelScope.launch ( Dispatchers.IO ) {
//          //  state = state.copy(itineraryProvided = 0)
            try {
                val response = generativeModel.generateContent(prompt)
                response.text?.let { outputContent ->
                    _uiState.value = ResponseState.Success(outputContent)
                    Log.d(MainViewModel::class.toString(), outputContent)
                }
            } catch (e: Exception) {
                _uiState.value = ResponseState.Error(e.localizedMessage ?: "")
            }
        }
    }
}

class MainViewModelFactory :ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return super.create(modelClass) as T
    }
}