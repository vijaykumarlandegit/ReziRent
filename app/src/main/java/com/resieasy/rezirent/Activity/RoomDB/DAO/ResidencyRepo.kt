package com.resieasy.rezirent.Activity.RoomDB.DAO

import com.resieasy.rezirent.Class.AddHostelClass
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class ResidencyRepo @Inject constructor(private val dao: ResidencyDao) {

    fun getAllResidenciesFlow(): Flow<List<HostelRoomDBClass>> = dao.getAllResidencies()

    suspend fun saveResidenciesToRoom(data: List<HostelRoomDBClass>) {
        dao.insertAll(data)
    }
}
