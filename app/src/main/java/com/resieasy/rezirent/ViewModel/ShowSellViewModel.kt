package com.resieasy.rezirent.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.resieasy.rezirent.Class.SellResiClass
import com.resieasy.rezirent.Repository.ShowSellRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ShowSellViewModel @Inject constructor(private val repository :ShowSellRepository):ViewModel(){

    private val _data = MutableStateFlow<SellResiClass?>(null)
    val data: StateFlow<SellResiClass?> = _data

     fun getSellData(id:String){
         viewModelScope.launch {
             _data.value = repository.fetchSingleSellData(id)
         }

     }
}