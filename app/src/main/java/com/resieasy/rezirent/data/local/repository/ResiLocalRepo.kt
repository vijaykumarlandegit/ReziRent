package com.resieasy.rezirent.data.local.repository

import com.resieasy.rezirent.data.local.dao.ResiDao
import com.resieasy.rezirent.data.local.entity.HostelLocalClass
import com.resieasy.rezirent.data.local.entity.ResiLocalClass
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class ResiLocalRepo @Inject constructor(private val dao: ResiDao){

    suspend fun saveResiToRoom(data: List<ResiLocalClass>){
        dao.insertResi(data)
    }
     fun getAllResiFlow(): Flow<List<ResiLocalClass>> = dao.getResi()


}