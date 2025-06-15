package com.resieasy.rezirent.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.firestore.FirebaseFirestore
import com.resieasy.rezirent.data.local.database.HostelLocalDatabase
import com.resieasy.rezirent.data.local.dao.HostelDao
import com.resieasy.rezirent.data.local.dao.ResiDao
import com.resieasy.rezirent.data.local.dao.SellDao
import com.resieasy.rezirent.data.local.database.ResiLocalDatabase
import com.resieasy.rezirent.data.local.database.SellLocalDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module//Marks this object as a Hilt module, which means it contains methods that provide dependencies.
@InstallIn(SingletonComponent::class)//Tells Hilt where to install this module.
//SingletonComponent means the provided dependencies will be scoped to the application. and its single instance
object AppModule {

    @Provides//Tells Hilt that this method provides a dependency.
    @Singleton
    fun provideFirebase() : FirebaseFirestore{
        return FirebaseFirestore.getInstance()
    }


    @Provides
    @Singleton
    fun provideHostelDatabase(@ApplicationContext appContext: Context): HostelLocalDatabase {
        return Room.databaseBuilder(appContext, HostelLocalDatabase::class.java, "hostelRoomDBB").build()
    }

    @Provides
    @Singleton
    fun provideHostelDao(database: HostelLocalDatabase): HostelDao {
        return database.hostelDao()
    }


    @Provides
    @Singleton
    fun provideResiDatabase(@ApplicationContext appContext: Context): ResiLocalDatabase {
        return Room.databaseBuilder(appContext, ResiLocalDatabase::class.java, "resiRoomDB").build()
    }

    @Provides
    @Singleton
    fun provideResidencyDao(database: ResiLocalDatabase): ResiDao {
        return database.resiDao()
    }


    @Provides
    @Singleton
    fun giveSellLocalDatabase(@ApplicationContext context: Context):SellLocalDatabase{
        return Room.databaseBuilder(context,SellLocalDatabase::class.java,"sellRoomDB").build()
    }

    @Provides
    @Singleton
    fun giveSellDao(database:SellLocalDatabase ):SellDao{
        return database.getSellDao()

    }





























}