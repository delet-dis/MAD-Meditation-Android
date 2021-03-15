package com.delet_dis.madmeditation.database

import android.app.Application
import android.database.sqlite.SQLiteDatabase
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


@Dao
interface GalleryDAO {

  @Query("SELECT * FROM imagecard")
  fun getAll(): LiveData<List<ImageCard>>

  @Query("SELECT * FROM imagecard WHERE id = :id")
  fun getById(id: Long): Flow<ImageCard>

  @Query("DELETE FROM imagecard WHERE id = :id")
  suspend fun removeById(id: Long)

  @Query("DELETE FROM imagecard")
  suspend fun nukeTable()

  @Insert
  suspend fun insert(imageCard: ImageCard)

  @Update
  suspend fun update(imageCard: ImageCard)

  @Delete
  suspend fun delete(imageCard: ImageCard)

  suspend fun getImageById(application: Application, id: Int): ImageCard {
    return getById(id.toLong()).first()
  }

  suspend fun clearTables(afterCallFunction: () -> Unit) =
    run {
      nukeTable()

      afterCallFunction()
    }

  suspend fun removeImageById(application: Application, id: Int, afterCallFunction: () -> Unit) =
    run {
      removeById(id.toLong())

      afterCallFunction()
    }

}