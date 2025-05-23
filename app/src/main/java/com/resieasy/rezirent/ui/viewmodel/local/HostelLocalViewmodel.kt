package com.resieasy.rezirent.ui.viewmodel.local

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.resieasy.rezirent.data.local.entity.HostelLocalClass
import com.resieasy.rezirent.data.local.repository.HostelLocalRepo
import com.resieasy.rezirent.data.remote.firebase.AddHostelClass
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HostelLocalViewmodel @Inject constructor(private val repository: HostelLocalRepo) : ViewModel() {

    val offlineHostels = MutableLiveData<MutableList<HostelLocalClass>>()


    fun loadFromRoom() {
        viewModelScope.launch {
            repository.getAllHostelsFlow().collect { list ->
                offlineHostels.postValue(list.toMutableList())
            }
        }
    }


    fun saveToLocalRoom(data: List<HostelLocalClass>) {
        viewModelScope.launch {
            repository.saveHostelsToRoom(data)
        }
    }
 }
