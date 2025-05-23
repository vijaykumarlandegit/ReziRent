package com.resieasy.rezirent.ui.viewmodel.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.resieasy.rezirent.data.remote.firebase.AddFlatClass
import com.resieasy.rezirent.data.remote.repository.ShowResiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShowResiViewModel @Inject constructor(private val repository: ShowResiRepository):ViewModel(){

    private val _data = MutableLiveData<AddFlatClass?>(null)
    val data: LiveData<AddFlatClass?> get()  = _data

    fun getResiData(id:String){
        viewModelScope.launch {
            _data.value=repository.fetchResiData(id)
        }
    }

}