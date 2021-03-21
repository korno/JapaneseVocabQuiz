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


class JapaneseVocabQuizRepository(
    database: JapaneseVocabQuizDatabase
) {
    private val vocabGroupDao = database.vocabGroupDao()
    private val vocabItemDao = database.vocabItemDao()

    val allGroupsAlphabetized: LiveData<List<VocabGroup>> = vocabGroupDao.getAllAlphabetized();

    val allGroupsAlphabetizedWithVocabItemCount: LiveData<List<VocabGroupWithVocabItemCount>> =
                        vocabGroupDao.getAllVocabGroupsWithVocabItemCount()

    suspend fun insert(vocabGroup: VocabGroup): Long{
        return vocabGroupDao.insert(vocabGroup)
    }


    suspend fun insert(vocabItem: VocabItem): Long{
        return vocabItemDao.insert(vocabItem)
    }

    suspend fun deleteAllVocabGroups(): Int{
        return vocabGroupDao.deleteAll()
    }

    fun getVocabGroupWithVocabItemsByUUID(uuid: String):LiveData<VocabGroupWithVocabItems>{
        return vocabGroupDao.getVocabGroupWithItemsByUUID(uuid)
    }

    suspend fun getVocabGroupWithVocabItemsByUUIDSus(uuid: String): VocabGroupWithVocabItems{
        return vocabGroupDao.getVocabGroupWithItemsByUUIDSus(uuid)
    }

}