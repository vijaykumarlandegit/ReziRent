package com.resieasy.rezirent.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.resieasy.rezirent.data.local.entity.SellLocalClass
import kotlinx.coroutines.flow.Flow


@Dao
interface SellDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun uploadSellListToRoom(list:List<SellLocalClass>)

    @Query("SELECT * FROM sellRoomDB")
    fun getSellList(): Flow<List<SellLocalClass>>

}