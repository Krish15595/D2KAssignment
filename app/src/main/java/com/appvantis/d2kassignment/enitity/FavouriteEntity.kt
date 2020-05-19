package com.appvantis.d2kassignment.enitity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class FavouriteEntity {
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0

    @ColumnInfo(name = "fav_shirt")
    var fav_shirt:String=""

    @ColumnInfo(name = "fav_pant")
    var fav_pant:String=""
}