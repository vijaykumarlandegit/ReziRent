package com.resieasy.rezirent.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.resieasy.rezirent.data.local.entity.ResiLocalClass
import kotlinx.coroutines.flow.Flow

@Dao
interface ResiDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResi(resi: List<ResiLocalClass>)

    @Query("SELECT * FROM resiRoomDB")
    fun getResi(): Flow<List<ResiLocalClass>>
}