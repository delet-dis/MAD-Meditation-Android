package com.delet_dis.madmeditation.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [ImageCard::class], version = 1)
abstract class GalleryDatabase : RoomDatabase() {
  abstract fun galleryDao(): GalleryDAO

  companion object {
    private var INSTANCE: GalleryDatabase? = null

    fun getAppDataBase(context: Context): GalleryDatabase {
      if (INSTANCE == null) {
        synchronized(GalleryDatabase::class) {
          INSTANCE =
            Room.databaseBuilder(
              context.applicationContext,
              GalleryDatabase::class.java,
              "galleryDB"
            )
              .build()
        }
      }
      return INSTANCE!!
    }

    fun destroyDataBase() {
      INSTANCE = null
    }
  }
}