package com.watchable.app.di

import android.content.Context
import androidx.room.Room
import com.watchable.app.BuildConfig
import com.watchable.app.data.api.JikanApi
import com.watchable.app.data.api.TmdbApi
import com.watchable.app.data.local.HistoryDao
import com.watchable.app.data.local.MediaDao
import com.watchable.app.data.local.WatchableDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideTmdbApi(): TmdbApi {
        val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer ${BuildConfig.TMDB_TOKEN}")
                    .build()
                chain.proceed(request)
            }
            .build()

        return Retrofit.Builder()
            .baseUrl(TmdbApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(TmdbApi::class.java)
    }

    @Provides
    @Singleton
    fun provideJikanApi(): JikanApi {
        return Retrofit.Builder()
            .baseUrl(JikanApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(JikanApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): WatchableDatabase {
        return Room.databaseBuilder(
            context,
            WatchableDatabase::class.java,
            "watchable.db"
        )
            .fallbackToDestructiveMigration() // Added for easy development
            .build()
    }

    @Provides
    fun provideMediaDao(db: WatchableDatabase): MediaDao = db.mediaDao()

    @Provides
    fun provideHistoryDao(db: WatchableDatabase): HistoryDao = db.historyDao()
}
