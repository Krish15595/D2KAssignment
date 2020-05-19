package com.appvantis.d2kassignment

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.appvantis.d2kassignment.enitity.FavouriteEntity

@Dao
interface FAV_DAO {
    @Insert
    fun savFav(fav: FavouriteEntity):Long

    @Query("select * from FavouriteEntity")
    fun readFav():List<FavouriteEntity>

    @Query("DELETE FROM FavouriteEntity WHERE fav_pant=:fav_pant and fav_shirt=:fav_shirt")
    fun deleteFav(fav_pant:String,fav_shirt:String):Int

    @Query("SELECT * FROM FavouriteEntity WHERE fav_pant=:fav_pant and fav_shirt=:fav_shirt")
    fun checkifExist(fav_pant:String,fav_shirt:String):Long
}
