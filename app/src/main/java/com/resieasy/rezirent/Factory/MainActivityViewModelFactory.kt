package com.resieasy.rezirent.Factory

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.resieasy.rezirent.Repository.MainActivityRepository
import com.resieasy.rezirent.ViewModel.MainActivityViewModel


class MainActivityViewModelFactory(private val repository: MainActivityRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MainActivityViewModel::class.java)) {
             Log.d("aa","AA")

            MainActivityViewModel(repository) as T
        } else {
            Log.d("cc","DD")
            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
