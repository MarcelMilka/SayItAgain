package eu.project.localdata.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import eu.project.localdata.converter.UUIDConverter
import eu.project.localdata.dao.SavedWordDAO
import eu.project.localdata.entity.SavedWordEntity

@Database(
    version = 1,
    entities = [SavedWordEntity::class],
)
@TypeConverters(UUIDConverter::class)
internal abstract class ApplicationDatabase: RoomDatabase() {

    abstract fun savedWordDAO(): SavedWordDAO
}