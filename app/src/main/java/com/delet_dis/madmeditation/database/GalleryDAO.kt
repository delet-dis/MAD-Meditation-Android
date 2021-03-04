package com.delet_dis.madmeditation.database

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface GalleryDAO {

  @Query("SELECT * FROM imagecard")
  fun getAll(): LiveData<List<ImageCard>>

  @Query("SELECT * FROM imagecard WHERE id = :id")
  suspend fun getById(id: Long): ImageCard?

  @Insert
  suspend fun insert(imageCard: ImageCard)

  @Update
  suspend fun update(imageCard: ImageCard)

  @Delete
  suspend fun delete(imageCard: ImageCard)
}