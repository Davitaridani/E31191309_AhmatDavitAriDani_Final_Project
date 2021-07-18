package com.example.dianastore.room

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.example.dianastore.model.TabelProduct

@Dao
interface DaoProduct {

    @Insert(onConflict = REPLACE)
    suspend fun insert(data: TabelProduct)

    @Insert(onConflict = REPLACE)
    suspend fun insert(data: List<TabelProduct>)

    @Delete
    suspend fun delete(data: TabelProduct)

    @Update
    suspend fun update(data: TabelProduct)

    @Query("SELECT * from tabel_produk ORDER BY id DESC")
    fun getAll(): LiveData<List<TabelProduct>>

    @Query("DELETE FROM tabel_produk")
    suspend fun deleteAll()

}