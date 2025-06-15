package com.resieasy.rezirent.data.local.repository

import com.resieasy.rezirent.data.local.dao.HostelDao
import com.resieasy.rezirent.data.local.entity.HostelLocalClass
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class HostelLocalRepo @Inject constructor(private val dao: HostelDao) {

    suspend fun saveHostelsToRoom(data: List<HostelLocalClass>) = dao.insertAllHostels(data)


    fun getAllHostelsFlow(): Flow<List<HostelLocalClass>> = dao.getAllHostels()


}
