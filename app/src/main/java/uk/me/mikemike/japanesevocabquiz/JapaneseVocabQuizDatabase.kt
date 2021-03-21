/*

Copyright 2021 Michael Hall

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

*/
package uk.me.mikemike.japanesevocabquiz

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [VocabItem::class, VocabGroup::class], version = 5)
abstract class JapaneseVocabQuizDatabase : RoomDatabase() {

    abstract fun vocabGroupDao(): VocabGroupDao
    abstract fun vocabItemDao(): VocabItemDao

    /*
        Based on Google's tutorials for a singleton instance of the database
     */
    companion object {

        // Location of the database asset
        private const val DATABASE_NAME = "jvq"
        private const val DATABASE_ASSET = "database/jvq.db"

        @Volatile
        private var INSTANCE: JapaneseVocabQuizDatabase? = null

        fun getInstance(context: Context): JapaneseVocabQuizDatabase{
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    JapaneseVocabQuizDatabase::class.java,
                        DATABASE_NAME
                ).fallbackToDestructiveMigration().createFromAsset(DATABASE_ASSET).build()
                INSTANCE = instance

                instance
            }
        }
    }
}