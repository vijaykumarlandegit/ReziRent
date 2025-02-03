package com.resieasy.rezirent.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.resieasy.rezirent.Class.FacilityClass
import com.resieasy.rezirent.Repository.FacilityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FacilityViewModel @Inject constructor(private var repository:FacilityRepository):ViewModel(){

    private val _data= MutableStateFlow<FacilityClass?>(null)
    val data:StateFlow<FacilityClass?> =_data

    fun getFacility(id:String){
        viewModelScope.launch {
            _data.value=repository.fetchFacility(id)
        }
    }
}