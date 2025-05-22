package com.resieasy.rezirent.Activity.Hilt

import android.content.Context
import androidx.room.Room
import com.google.firebase.firestore.FirebaseFirestore
import com.resieasy.rezirent.Activity.RoomDB.DAO.AppDatabase
import com.resieasy.rezirent.Activity.RoomDB.DAO.ResidencyDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

//MIISCC
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
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "hostelRoomDB"
        ).build()
    }

    @Provides
    fun provideResidencyDao(database: AppDatabase): ResidencyDao {
        return database.residencyDao()
    }

//
//    @Provides
//    @Singleton
//    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
//        return Room.databaseBuilder(context, AppDatabase::class.java, "my_db").build()
//    }
//



}