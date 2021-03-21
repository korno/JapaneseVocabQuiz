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

import androidx.room.*

@Entity(foreignKeys = arrayOf(ForeignKey(entity = VocabGroup::class,
                                    parentColumns = arrayOf("uuid"),
                                    childColumns = arrayOf("vocabGroupID"),
                                    onDelete = ForeignKey.CASCADE)))
data class VocabItem (
    @PrimaryKey(autoGenerate = true) val id: Int,
    val kana: String,
    val kanji: String?,
    val romaji: String,
    val meaning: String,
    val vocabGroupID: String
)

@Dao
interface  VocabItemDao{
    @Insert
    suspend fun insert(vocabItem: VocabItem): Long

}