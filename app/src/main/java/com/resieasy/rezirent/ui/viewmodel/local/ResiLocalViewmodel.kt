package com.resieasy.rezirent.ui.viewmodel.local

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Insert
import com.resieasy.rezirent.data.local.entity.HostelLocalClass
import com.resieasy.rezirent.data.local.entity.ResiLocalClass
import com.resieasy.rezirent.data.local.repository.ResiLocalRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ResiLocalViewmodel @Inject constructor(private val repository: ResiLocalRepo) : ViewModel() {

    val offlineResi = MutableLiveData<MutableList<ResiLocalClass>>()

    fun saveResiToLocalRoom(data: List<ResiLocalClass>) {
        viewModelScope.launch {
            repository.saveResiToRoom(data)
        }
    }

    fun loadResiFromRoom() {
        viewModelScope.launch {
            repository.getAllResiFlow().collect { list ->
                offlineResi.postValue(list.toMutableList())
            }
        }
    }



}