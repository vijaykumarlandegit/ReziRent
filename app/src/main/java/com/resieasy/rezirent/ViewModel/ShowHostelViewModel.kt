package com.resieasy.rezirent.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.resieasy.rezirent.Class.AddFlatClass
import com.resieasy.rezirent.Class.AddHostelClass
import com.resieasy.rezirent.Repository.ShowHostelRepository
import com.resieasy.rezirent.Repository.ShowResiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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