package com.resieasy.rezirent.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.resieasy.rezirent.data.local.dao.HostelDao
import com.resieasy.rezirent.data.local.entity.HostelLocalClass

@Database(entities = [HostelLocalClass::class], version = 1)
abstract class HostelLocalDatabase : RoomDatabase() {
    abstract fun hostelDao(): HostelDao
}