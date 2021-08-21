package org.devlee.example.mvvm.notes.ui.main

import android.text.format.DateFormat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import org.devlee.example.mvvm.notes.locateLazy
import org.devlee.example.mvvm.notes.repository.Repository
import org.devlee.example.mvvm.notes.repository.room.Note
import java.util.*

class MainViewModel : ViewModel() {

    private val repository: Repository by locateLazy()

    val notes = repository.getAll().asLiveDataFlow()

    fun save(note: String) {
        viewModelScope.launch { repository.save(createNote(note)) }
    }

    fun delete(note: Note) {
        viewModelScope.launch { repository.delete(note) }
    }

    private fun createNote(noteText: String) = Note(
        caption = createCaption(),
        text = noteText
    )

    private fun createCaption(): String =
        DateFormat.format("hh:mm:ss, MMM dd, yyyy", Date()).toString()

    fun <T> Flow<T>.asLiveDataFlow() = shareIn(viewModelScope, SharingStarted.Eagerly, replay = 1)
}