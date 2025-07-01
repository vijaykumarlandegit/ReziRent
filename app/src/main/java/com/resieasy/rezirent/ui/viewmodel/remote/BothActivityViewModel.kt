package com.resieasy.rezirent.ui.viewmodel.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.resieasy.rezirent.data.remote.firebase.BothResiClass
import com.resieasy.rezirent.data.remote.firebase.UnifiedResidencyClass
import com.resieasy.rezirent.data.remote.repository.BothActivityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BothActivityViewModel @Inject constructor(
    private val repository: BothActivityRepository
) : ViewModel() {

    private val _allData = MutableLiveData<List<UnifiedResidencyClass>>()
    val allData: LiveData<List<UnifiedResidencyClass>> get() = _allData

    private val fullList = mutableListOf<UnifiedResidencyClass>()
    private var isLoading = false

    fun fetchNextPage() {
        if (isLoading) return
        isLoading = true

        viewModelScope.launch {
            val newPage = repository.getNextPage()
            fullList.addAll(newPage)
            _allData.postValue(fullList)
            isLoading = false
        }
    }

    fun resetAll() {
        fullList.clear()
        _allData.value = emptyList()
        repository.resetPagination()

    }

    fun filterDataByType(type: String) {
        val filtered = fullList.filter { it.rtype == type }
        _filteredData.value = filtered
    }

    private val _filteredData = MutableLiveData<List<UnifiedResidencyClass>>()
    val filteredData: LiveData<List<UnifiedResidencyClass>> get() = _filteredData
}
