package com.resieasy.rezirent.Activity.RoomDB.DAO

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.resieasy.rezirent.Class.AddHostelClass
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ResiRoomViewmodel @Inject constructor(private val repository: ResidencyRepo) : ViewModel() {

    val offlineResidencies = MutableLiveData<MutableList<HostelRoomDBClass>>()


    fun loadFromRoom() {
        viewModelScope.launch {
            repository.getAllResidenciesFlow().collect { list ->
                offlineResidencies.postValue(list.toMutableList())
            }
        }
    }


    fun saveToLocalRoom(data: List<HostelRoomDBClass>) {
        viewModelScope.launch {
            repository.saveResidenciesToRoom(data)
        }
    }
    fun HostelRoomDBClass.toAddHostelClass(): AddHostelClass {
        return AddHostelClass(
            mail = mail,
            status = status,
            rtype = rtype,
            type = type,
            subtype = subtype,
            name = name,
            lowercase = lowercase,
            address = address,
            area = area,
            oname = oname,
            number = number,
            whatsapp = whatsapp,
            rent = rent,
            erent = erent,
            deposit = deposit,
            extra = extra,
            more = more,
            policy = policy,
            gopen = gopen,
            gclose = gclose,
            userid = userid,
            id = id,
            f1 = f1,
            f2 = f2,
            f3 = f3,
            i1 = i1,
            input = input,
            period = period,
            latitude = latitude,
            longitude = longitude,
            time = time
        )
    }
}
