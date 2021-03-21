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

import androidx.lifecycle.LiveData
import androidx.room.*


@Entity
data class VocabGroup (
    @PrimaryKey val uuid: String,
    val name: String,
    val description: String?
)

data class VocabGroupWithVocabItemCount (
        @Embedded
        var vocabGroup: VocabGroup,
        var numberOfVocabItems: Int = 0
)

data class VocabGroupWithVocabItems (
    @Embedded
    val vocabGroup: VocabGroup,
    @Relation(
        parentColumn = "uuid",
        entityColumn = "vocabGroupID"
    )
    val vocabItems: List<VocabItem>
)

@Dao
interface VocabGroupDao {
    @Insert
    suspend fun insert(vocabGroup: VocabGroup): Long

    @Query("SELECT * FROM VocabGroup ORDER BY name ASC")
    fun getAllAlphabetized(): LiveData<List<VocabGroup>>

    @Transaction
    @Query(value = "SELECT * FROM VocabGroup WHERE VocabGroup.uuid = :uuid")
    fun getVocabGroupWithItemsByUUID(uuid: String): LiveData<VocabGroupWithVocabItems>

    @Transaction
    @Query(value = "SELECT * FROM VocabGroup WHERE VocabGroup.uuid = :uuid")
    suspend fun getVocabGroupWithItemsByUUIDSus(uuid: String): VocabGroupWithVocabItems

    @Query("DELETE FROM VocabGroup")
    suspend fun deleteAll(): Int

    @Query("SELECT *, (SELECT COUNT(*) FROM VocabItem WHERE VocabItem.vocabGroupID = uuid ) as numberOfVocabItems FROM VocabGroup")
    fun getAllVocabGroupsWithVocabItemCount() : LiveData<List<VocabGroupWithVocabItemCount>>


}




