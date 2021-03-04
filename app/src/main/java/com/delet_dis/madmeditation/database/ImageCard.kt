package com.delet_dis.madmeditation.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ImageCard(
  @PrimaryKey(autoGenerate = true)
  var id: Long?,

  var imagePath: String,

  var time: String
)