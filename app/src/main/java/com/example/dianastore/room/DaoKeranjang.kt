package com.example.dianastore.room

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.example.dianastore.model.TabelKeranjang

@Dao
interface DaoKeranjang {

    @Insert(onConflict = REPLACE)
    fun insert(data: TabelKeranjang)

    @Delete
    fun delete(data: TabelKeranjang)

    @Delete
    fun delete(data: List<TabelKeranjang>)

    @Update
    fun update(data: TabelKeranjang): Int

    @Query("UPDATE tabel_keranjang SET selected = :status")
    fun update(status: Boolean = false): Int

    @Query("SELECT * from tabel_keranjang ORDER BY idTabel ASC")
    fun getAll(): List<TabelKeranjang>

    @Query("SELECT * FROM tabel_keranjang WHERE idTabel = :id LIMIT 1")
    fun getProduk(id: String): TabelKeranjang?

    @Query("DELETE FROM tabel_keranjang WHERE idTabel = :id")
    fun deleteById(id: String): Int

    @Query("DELETE FROM tabel_keranjang")
    fun deleteAll(): Int

    @Query("SELECT * from tabel_keranjang ORDER BY idTabel DESC LIMIT 1")
    fun getLastItem(): TabelKeranjang?

}