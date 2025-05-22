package com.resieasy.rezirent.Activity.RoomDB.DAO

import androidx.room.Database
import androidx.room.RoomDatabase
import com.resieasy.rezirent.Class.AddHostelClass

@Database(entities = [HostelRoomDBClass::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun residencyDao(): ResidencyDao
}