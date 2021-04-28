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


class QuizViewModel(private val vocabGroupUUID: String, private val repositoryJapanese: JapaneseVocabQuizRepository) : ViewModel(){

    public val vocabGroup: LiveData<VocabGroupWithVocabItems> = repositoryJapanese.getVocabGroupWithVocabItemsByUUID((vocabGroupUUID))


    public val quiz: MutableLiveData<Quiz> by lazy{
        val q = MutableLiveData<Quiz>()
        viewModelScope.launch(Dispatchers.IO){
            val items = repositoryJapanese.getVocabGroupWithVocabItemsByUUIDSus(vocabGroupUUID)
            val qu = Quiz(items.vocabItems, 3)
            q.postValue(qu)
        }
        q
    }

    /* This is needed as updating fields in the object does not refresh the object (i.e fire the live data observe methods
     */
    public fun refreshQuiz() {
        quiz.postValue(quiz.value)
    }


    public fun answerQuizQuestion(answerIndex: Int): Boolean{
        val res = quiz.value?.answerQuestion(answerIndex)
        refreshQuiz()
        return res!!
    }

    public fun restartTest(){
        quiz.value?.reset()
        refreshQuiz()
    }

}


class QuizViewModelFactory(private val repositoryJapanese: JapaneseVocabQuizRepository,
                        private val uuid: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuizViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return QuizViewModel(uuid, repositoryJapanese) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}