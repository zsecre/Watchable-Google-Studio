package com.watchable.app.data.local

import androidx.room.*
import com.watchable.app.data.model.Media
import com.watchable.app.data.model.MediaType
import kotlinx.coroutines.flow.Flow

@Dao
interface MediaDao {
    @Query("SELECT * FROM watchlist ORDER BY timestamp DESC")
    fun getWatchlist(): Flow<List<Media>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedia(media: Media)

    @Delete
    suspend fun deleteMedia(media: Media)

    @Query("SELECT EXISTS(SELECT * FROM watchlist WHERE id = :id)")
    fun isInWatchlist(id: String): Flow<Boolean>

    @Query("SELECT * FROM watchlist WHERE id = :id")
    suspend fun getMediaById(id: String): Media?
}

@Database(entities = [Media::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class WatchableDatabase : RoomDatabase() {
    abstract fun dao(): MediaDao
}

class Converters {
    @TypeConverter
    fun fromStringList(value: List<String>): String = value.joinToString(",")

    @TypeConverter
    fun toStringList(value: String): List<String> = value.split(",").filter { it.isNotEmpty() }

    @TypeConverter
    fun fromMediaType(value: MediaType): String = value.name

    @TypeConverter
    fun toMediaType(value: String): MediaType = MediaType.valueOf(value)
}
