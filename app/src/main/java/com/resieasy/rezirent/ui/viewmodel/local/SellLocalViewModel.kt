package com.resieasy.rezirent.ui.viewmodel.local

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.resieasy.rezirent.data.local.entity.SellLocalClass
import com.resieasy.rezirent.data.local.repository.SellLocalRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SellLocalViewModel @Inject constructor(private val repo:SellLocalRepo):ViewModel(){
     val offlineSellList=MutableLiveData<List<SellLocalClass>>()

      fun uploadListToLocal(list:List<SellLocalClass>){
         viewModelScope.launch(Dispatchers.IO) {
             repo.uploadSellList(list)
         }

    }
    fun getSellList(){
        viewModelScope.launch(Dispatchers.IO) {
            repo.getSellList().collect{
                offlineSellList.postValue(it.toList())

            }
        }
    }


}