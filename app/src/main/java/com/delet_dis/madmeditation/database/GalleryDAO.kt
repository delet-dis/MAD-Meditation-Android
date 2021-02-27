package com.delet_dis.madmeditation.database

import androidx.room.*


@Dao
interface GalleryDAO {

  @Query("SELECT * FROM imagecard")
  fun getAll(): List<ImageCard>

  @Query("SELECT * FROM imagecard WHERE id = :id")
  fun getById(id: Long): ImageCard?

  @Insert
  fun insert(imageCard: ImageCard)

  @Update
  fun update(imageCard: ImageCard)

  @Delete
  fun delete(imageCard: ImageCard)
}