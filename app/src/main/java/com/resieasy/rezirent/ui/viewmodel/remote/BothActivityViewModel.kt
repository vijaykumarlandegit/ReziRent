package com.resieasy.rezirent.ui.viewmodel.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.resieasy.rezirent.data.remote.firebase.BothResiClass
import com.resieasy.rezirent.data.remote.repository.BothActivityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BothActivityViewModel @Inject constructor(
    private val repository: BothActivityRepository
) : ViewModel() {

    private val _allData = MutableLiveData<List<BothResiClass>>()//only accessible by viewmodel
    val allData: LiveData<List<BothResiClass>> get() = _allData //accessible by activity, only read -> get()

    private val _filteredData = MutableLiveData<List<BothResiClass>>()
    val filteredData: LiveData<List<BothResiClass>> get() = _filteredData

    // Method to fetch all data from Firebase
    fun fetchAllData() {
        viewModelScope.launch {
            val data = repository.getAllData()
            _allData.value = data//Use .value => main thread OR Use .postValue() => background thread
        }
    }

    // Method to filter data by type directly in ViewModel
    fun filterDataByType(type: String) {
        _allData.value?.let {
            val filtered = it.filter { item ->
                item.rtype == type
            }
            _filteredData.value = filtered
        }
    }

}
