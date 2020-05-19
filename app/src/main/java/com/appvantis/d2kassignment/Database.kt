package com.appvantis.d2kassignment

import androidx.room.Database
import androidx.room.RoomDatabase
import com.appvantis.d2kassignment.enitity.FavouriteEntity

@Database(entities = arrayOf(FavouriteEntity::class), version = 1)
abstract class AppDB : RoomDatabase() {
    abstract fun favDao(): FAV_DAO
}
