package com.resieasy.rezirent.ui.viewmodel.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.resieasy.rezirent.data.remote.firebase.RulesClass
import com.resieasy.rezirent.data.remote.repository.RuleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

     @HiltViewModel
    class RulesViewModel @Inject constructor(private var repository: RuleRepository):
        ViewModel(){

        private val _data= MutableLiveData<RulesClass?>(null)
         val data: LiveData<RulesClass?> get()  =_data

        fun getRules(id:String){
            viewModelScope.launch {
                _data.value=repository.fetchRule(id)
            }
        }
    }