package ua.edu.chnu.labo3.ui.mvi

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/** Marker: the single immutable object describing everything a screen shows. */
interface UiState

/** Marker: something the user (or the screen) wants to happen. */
interface UiIntent

/**
 * Lab 3: base class of the MVI presentation layer (replaces plain MVVM).
 *
 *  - **Model**  - one immutable [UiState], exposed as [state].
 *  - **View**   - the Composable only renders [state] and sends [UiIntent]s.
 *  - **Intent** - the *only* way into the ViewModel is [onIntent].
 *
 * Data flows one way:  View --Intent--> ViewModel --new State--> View.
 * Subclasses implement [handleIntent] and change the state only through
 * [setState] (a reducer: old state -> new state).
 */
abstract class MviViewModel<S : UiState, I : UiIntent>(initialState: S) : ViewModel() {

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<S> = _state.asStateFlow()

    /** Single entry point for everything the View wants to do. */
    fun onIntent(intent: I) = handleIntent(intent)

    protected abstract fun handleIntent(intent: I)

    /** Reducer: builds the next state from the current one. */
    protected fun setState(reducer: S.() -> S) = _state.update { it.reducer() }
}
