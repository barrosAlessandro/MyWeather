package br.com.wcabral.myweather.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tb_favorites")
data class Favorite(

    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: Long,

    @ColumnInfo(name = "cityName")
    var cityName: String
)