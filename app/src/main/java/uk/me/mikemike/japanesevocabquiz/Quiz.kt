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

import java.lang.IllegalStateException
import java.util.*

class Quiz (val allItems: List<VocabItem>, var numberOfAnswersToGenerate : Int) {

    var correctAnswers: MutableList<VocabItem> = mutableListOf()
    var wrongAnswers: MutableList<VocabItem> = mutableListOf()

    var currentQuestion: VocabItem? = null
    var currentQuestionAnswers: MutableList<VocabItem> = mutableListOf()
    val remainingItems: Queue<VocabItem> = LinkedList(allItems)
    val isFinished: Boolean get() = currentQuestion == null
    val isNew: Boolean get() = !isFinished && allItems.size == remainingItems.size+1
    val isInProgress get() = !isFinished && !isNew
    val remainingRatio get() = if(answeredQuestionsCount == 0) 1f else 1f - (answeredQuestionsCount.toFloat() / totalQuestions.toFloat())
    val answeredQuestionsCount get() = correctAnswers.size + wrongAnswers.size
    val remainingQuestionCount get() = remainingItems.size
    val totalQuestions get() = allItems.size;
    val currenQuestionNumber get() = answeredQuestionsCount + 1
    val correctAnswerCount get() = correctAnswers.size
    val wrongAnswerCount get() = wrongAnswers.size
    val isEmpty get() = totalQuestions == 0

    init {
        numberOfAnswersToGenerate = if(numberOfAnswersToGenerate > allItems.size) allItems.size else numberOfAnswersToGenerate
        reset()
    }

    fun answerQuestion(answerIndex: Int): Boolean{
        return answerQuestion(currentQuestionAnswers[answerIndex])
    }

    fun answerQuestion(answer: VocabItem): Boolean{
        if(!isFinished) {
            val result = currentQuestion!!.id == answer.id
            (if (result) correctAnswers else wrongAnswers).add(currentQuestion!!)
            getNextQuestion()
            return result
        }
        throw IllegalStateException("The test is finished but answerQuestion was called")
    }

    private fun getNextQuestion(){
        currentQuestion = remainingItems.poll()
        if(!isFinished){
            var tmp = allItems.toMutableList()
            tmp.remove(currentQuestion!!)
            currentQuestionAnswers.apply {
                clear()
                add(currentQuestion!!)
                for(i in 1..numberOfAnswersToGenerate){
                    val wrongAnswer = tmp.random()
                    add(wrongAnswer)
                    tmp.remove(wrongAnswer)
                }
                shuffle()
            }
        }
    }

    public fun reset(){
        with(remainingItems){
            clear()
            addAll(allItems.shuffled())
        }
        correctAnswers.clear()
        wrongAnswers.clear()
        getNextQuestion()
    }

}