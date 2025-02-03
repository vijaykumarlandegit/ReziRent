package com.resieasy.rezirent.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.resieasy.rezirent.Class.AddFlatClass
import com.resieasy.rezirent.Class.SellResiClass
import com.resieasy.rezirent.Repository.ShowResiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShowResiViewModel @Inject constructor(private val repository:ShowResiRepository):ViewModel(){

    private val _data = MutableStateFlow<AddFlatClass?>(null)
    val data: StateFlow<AddFlatClass?> = _data

    fun getResiData(id:String){
        viewModelScope.launch {
            _data.value=repository.fetchResiData(id)
        }
    }

}