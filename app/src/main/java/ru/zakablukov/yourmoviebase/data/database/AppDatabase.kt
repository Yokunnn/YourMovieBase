package ru.zakablukov.yourmoviebase.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.zakablukov.yourmoviebase.data.database.dao.MovieDao
import ru.zakablukov.yourmoviebase.data.database.entity.GenreEntity
import ru.zakablukov.yourmoviebase.data.database.entity.MovieEntity
import ru.zakablukov.yourmoviebase.data.database.entity.MovieGenreCrossRef
import ru.zakablukov.yourmoviebase.data.database.entity.MoviePersonCrossRef
import ru.zakablukov.yourmoviebase.data.database.entity.PersonEntity

@Database(
    entities = [
        MovieEntity::class,
        PersonEntity::class,
        GenreEntity::class,
        MoviePersonCrossRef::class,
        MovieGenreCrossRef::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}