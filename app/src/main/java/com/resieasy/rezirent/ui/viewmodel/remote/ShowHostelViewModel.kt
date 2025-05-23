package com.resieasy.rezirent.ui.viewmodel.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.resieasy.rezirent.data.remote.firebase.AddHostelClass
import com.resieasy.rezirent.data.remote.repository.ShowHostelRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

    @HiltViewModel
    class ShowHostelViewModel @Inject constructor(private val repository: ShowHostelRepository):
        ViewModel(){

        private val _data = MutableLiveData<AddHostelClass?>(null)
        val data: LiveData<AddHostelClass?> get() = _data

        fun getHostelData(id:String){
            viewModelScope.launch {
                _data.value=repository.fetchHostelData(id)
            }
        }

    }