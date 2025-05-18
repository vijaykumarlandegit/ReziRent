package com.resieasy.rezirent.Activity.Hilt

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
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


//
//    @Provides
//    @Singleton
//    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
//        return Room.databaseBuilder(context, AppDatabase::class.java, "my_db").build()
//    }
//



}