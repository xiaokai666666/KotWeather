package com.example.kotweather.module.chooseplace.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.kotweather.model.ChoosePlaceData
import com.example.kotweather.model.LocationTypeConverter
import com.example.kotweather.module.chooseplace.model.dao.ChoosePlaceDao

@Database(entities = [ChoosePlaceData::class], version = 1, exportSchema = false)
@TypeConverters(LocationTypeConverter::class)
abstract class ChoosePlaceDataBase : RoomDatabase() {
    abstract fun choosePlaceDao(): ChoosePlaceDao
    companion object {
        private var instance: ChoosePlaceDataBase? = null
        fun getInstance(context: Context): ChoosePlaceDataBase? {
            if (instance == null) {
                synchronized(ChoosePlaceDataBase::class.java) {
                    if (instance == null) {
                        instance = Room.databaseBuilder(
                                context,
                                ChoosePlaceDataBase::class.java,
                                "database_choose_place"
                        ).build()
                    }
                }
            }
            return instance
        }
    }
}