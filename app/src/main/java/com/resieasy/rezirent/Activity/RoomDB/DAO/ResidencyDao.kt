package com.resieasy.rezirent.Activity.RoomDB.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.resieasy.rezirent.Class.AddHostelClass
import kotlinx.coroutines.flow.Flow

@Dao
interface ResidencyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(residencies: List<HostelRoomDBClass>)

    @Query("SELECT * FROM hostelRoomDB")
    fun getAllResidencies(): Flow<List<HostelRoomDBClass>>
}