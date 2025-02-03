package com.resieasy.rezirent.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.resieasy.rezirent.Class.AddFlatClass
import com.resieasy.rezirent.Class.AddHostelClass
import com.resieasy.rezirent.Class.SellResiClass
import com.resieasy.rezirent.Repository.MainActivityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainActivityViewModel @Inject constructor(private val repository: MainActivityRepository) :ViewModel(){

   private val _hostelPG=MutableLiveData<List<AddHostelClass>>()
    val hostePG:LiveData<List<AddHostelClass>>get() = _hostelPG

    fun loadHostelPG(){
        viewModelScope.launch {
            try {
                val userList = repository.fetchHostelPG()
                Log.d("UserViewModel", "Users loaded: $userList")
                _hostelPG.postValue(userList)
            } catch (e: Exception) {
                Log.e("UserViewModel", "Error loading users", e)
                _hostelPG.postValue(emptyList())
            }
        }
    }
    private val _rent=MutableLiveData<List<AddFlatClass>>()
    val rent:LiveData<List<AddFlatClass>>get() = _rent

    fun loadRent(){
        viewModelScope.launch {
            try {
                val userList = repository.fetchRent()
                Log.d("UserViewModel", "Users loaded: $userList")
                _rent.postValue(userList)
            } catch (e: Exception) {
                Log.e("UserViewModel", "Error loading users", e)
                _rent.postValue(emptyList())
            }
        }
    }private val _sell=MutableLiveData<List<SellResiClass>>()
    val sell:LiveData<List<SellResiClass>>get() = _sell

    fun loadSell(){
        viewModelScope.launch {
            try {
                val userList = repository.fetchSell()
                Log.d("UserViewModel", "Users loaded: $userList")
                _sell.postValue(userList)
            } catch (e: Exception) {
                Log.e("UserViewModel", "Error loading users", e)
                _sell.postValue(emptyList())
            }
        }
    }
}