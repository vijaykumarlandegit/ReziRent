package com.resieasy.rezirent.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.resieasy.rezirent.data.local.entity.HostelLocalClass
import kotlinx.coroutines.flow.Flow

@Dao
interface HostelDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllHostels(hostels: List<HostelLocalClass>)

    @Query("SELECT * FROM hostelRoomDB")
    fun getAllHostels(): Flow<List<HostelLocalClass>>
}