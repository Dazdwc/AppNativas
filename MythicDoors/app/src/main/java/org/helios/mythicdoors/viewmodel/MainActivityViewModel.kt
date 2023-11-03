package org.helios.mythicdoors.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.helios.mythicdoors.model.DataController
import org.helios.mythicdoors.utils.Connection

class MainActivityViewModel(
    private val dbHelper: Connection,
    private val dataController: DataController
    ): ViewModel() {

    init { viewModelScope.launch{ loadDefaultData() }}

    private suspend fun loadDefaultData() {
        dataController.initDataLoader()
    }
}