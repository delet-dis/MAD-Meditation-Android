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
) {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as ImageCard

    if (id != other.id) return false
    if (!image.contentEquals(other.image)) return false
    if (time != other.time) return false

    return true
  }

  override fun hashCode(): Int {
    var result = id.hashCode()
    result = 31 * result + image.contentHashCode()
    result = 31 * result + time.hashCode()
    return result
  }
}