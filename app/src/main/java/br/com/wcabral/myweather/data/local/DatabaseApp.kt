package br.com.wcabral.myweather.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.com.wcabral.myweather.data.local.model.Favorite

@Database(entities = [Favorite::class], version = 1)
abstract class DatabaseApp: RoomDatabase() {

    abstract fun getFavoriteDao(): FavoriteDao

    companion object {
        private var instance: DatabaseApp? = null

        fun getInstance(context: Context): DatabaseApp {
            if (instance == null) {
                synchronized(DatabaseApp::class.java) {
                    // Criar DB
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        DatabaseApp::class.java,
                        "weather.db"
                    ).allowMainThreadQueries()
                     .build()
                }
            }

            return instance!!
        }
    }

}