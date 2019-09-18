package com.geekorum.bugabstractsavedstatevmfactory

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner

/**
 * Simple Factory based on AbstractSavedStateViewModelFactory.
 *
 * On "androidx.lifecycle:lifecycle-viewmodel-savedstate:1.0.0-alpha05"
 * the application crash when creating the ViewModel.
 *
 * On alpha04 no problem
 */
class SimpleFactory(
    owner: SavedStateRegistryOwner,
    defaultArgs: Bundle?
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {

    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        return SimpleViewModel() as T
    }
}

class SimpleViewModel: ViewModel() {

    fun doSomething() {
        Log.d("SimpleViewModel", "do something")
    }
}
