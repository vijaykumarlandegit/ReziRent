package com.resieasy.rezirent.data.local.repository

import com.resieasy.rezirent.data.local.dao.SellDao
import com.resieasy.rezirent.data.local.entity.SellLocalClass
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SellLocalRepo @Inject constructor(private val dao: SellDao){

    suspend fun uploadSellList(list:List<SellLocalClass>){
        dao.uploadSellListToRoom(list)

    }

     fun getSellList(): Flow<List<SellLocalClass>> {
        return dao.getSellList()
    }

}