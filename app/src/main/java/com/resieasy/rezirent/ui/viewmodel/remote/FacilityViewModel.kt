package com.resieasy.rezirent.ui.viewmodel.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.resieasy.rezirent.data.remote.firebase.FacilityClass
import com.resieasy.rezirent.data.remote.repository.FacilityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FacilityViewModel @Inject constructor(private var repository: FacilityRepository):ViewModel(){

    private val _data= MutableLiveData<FacilityClass?>(null)
    val data:LiveData<FacilityClass?> get() =_data

    fun getFacility(id:String){
        viewModelScope.launch {
            _data.value=repository.fetchFacility(id)
        }
    }
}