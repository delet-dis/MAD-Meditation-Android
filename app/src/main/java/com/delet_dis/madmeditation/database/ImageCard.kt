package com.delet_dis.madmeditation.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ImageCard(
  @PrimaryKey
  var id: Long,

  @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
  var image: ByteArray,

  var time: String
)