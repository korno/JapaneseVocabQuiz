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

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VocabGroupListViewModel(private val repositoryJapanese: JapaneseVocabQuizRepository) : ViewModel() {

    val allGroupsAlphabetizedWithVocabItemCount: LiveData<List<VocabGroupWithVocabItemCount>>
            = repositoryJapanese.allGroupsAlphabetizedWithVocabItemCount

    fun insert(vocabGroup: VocabGroup): LiveData<Long>{
        var result = MutableLiveData<Long>()
        viewModelScope.launch(Dispatchers.IO) {
            result.postValue(repositoryJapanese.insert(vocabGroup))
        }
        return result
    }


    fun insert(vocabItem: VocabItem): LiveData<Long>{
        var result = MutableLiveData<Long>()
        viewModelScope.launch(Dispatchers.IO) {
            result.postValue(repositoryJapanese.insert(vocabItem))
        }
        return result
    }

    fun deleteAllVocabGroups(): LiveData<Int>{
        var result = MutableLiveData<Int>()
        viewModelScope.launch(Dispatchers.IO) {
            result.postValue(repositoryJapanese.deleteAllVocabGroups())
        }
        return result
    }

}


class VocabGroupListViewModelFactory(private val repositoryJapanese: JapaneseVocabQuizRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VocabGroupListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return VocabGroupListViewModel(repositoryJapanese) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}