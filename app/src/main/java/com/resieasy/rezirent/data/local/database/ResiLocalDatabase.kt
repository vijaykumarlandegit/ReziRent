package com.resieasy.rezirent.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.resieasy.rezirent.data.local.dao.ResiDao
import com.resieasy.rezirent.data.local.entity.ResiLocalClass

@Database(entities = [ResiLocalClass::class],version=1)
abstract class ResiLocalDatabase :RoomDatabase(){
    abstract fun resiDao(): ResiDao
}