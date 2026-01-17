package com.doselfurioso.lalucha.di

import android.content.Context
import androidx.room.Room
import com.doselfurioso.lalucha.data.local.TaskDatabase
import com.doselfurioso.lalucha.data.local.dao.TaskDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideTaskDatabase(
        @ApplicationContext context: Context
    ): TaskDatabase {
        return Room.databaseBuilder(
            context,
            TaskDatabase::class.java,
            "lalucha_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideTaskDao(db: TaskDatabase): TaskDao {
        return db.taskDao
    }
}