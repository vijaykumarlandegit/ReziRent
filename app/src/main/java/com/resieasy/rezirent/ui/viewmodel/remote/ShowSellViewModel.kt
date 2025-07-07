package com.resieasy.rezirent.ui.viewmodel.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.resieasy.rezirent.data.remote.firebase.SellResiClass
import com.resieasy.rezirent.data.remote.repository.ShowSellRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ShowSellViewModel @Inject constructor(private val repository : ShowSellRepository):ViewModel(){

    private val _data = MutableLiveData<SellResiClass?>(null)
    val data: LiveData<SellResiClass?> = _data

     fun getSellData(id:String){
         viewModelScope.launch {
             _data.postValue(repository.fetchSingleSellData(id))
         }

     }
}