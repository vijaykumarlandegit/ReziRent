package com.resieasy.rezirent.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.resieasy.rezirent.Class.BothResiClass
import com.resieasy.rezirent.Repository.BothActivityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BothActivityViewModel @Inject constructor(
    private val repository: BothActivityRepository
) : ViewModel() {

    private val _allData = MutableLiveData<List<BothResiClass>>()
    val allData: LiveData<List<BothResiClass>> get() = _allData

    private val _filteredData = MutableLiveData<List<BothResiClass>>()
    val filteredData: LiveData<List<BothResiClass>> get() = _filteredData

    // Method to fetch all data from Firebase
    fun fetchAllData() {
        viewModelScope.launch {
            val data = repository.getAllData()
            _allData.postValue(data)
        }
    }

    // Method to filter data by type directly in ViewModel
    fun filterDataByType(type: String) {
        _allData.value?.let {
            val filtered = it.filter { item ->
                item.rtype == type
            }
            _filteredData.postValue(filtered)
        }
    }

}
