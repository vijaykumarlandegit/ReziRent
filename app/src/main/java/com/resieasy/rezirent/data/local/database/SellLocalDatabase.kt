package com.resieasy.rezirent.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.resieasy.rezirent.data.local.dao.SellDao
import com.resieasy.rezirent.data.local.entity.SellLocalClass

@Database(entities = [SellLocalClass::class], version = 1)
abstract class SellLocalDatabase : RoomDatabase(){
    abstract fun getSellDao(): SellDao
}