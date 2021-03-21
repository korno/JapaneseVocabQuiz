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


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer

import kotlinx.android.synthetic.main.fragment_quiz.*
import kotlin.math.roundToInt


class QuizFragment : QuizBaseFragment() {

    private var mToast : Toast? = null

    companion object {
        fun newInstance() = QuizFragment()
    }


    private fun showToast(t: Toast){
        mToast?.cancel()
        mToast = t
        t.show()
    }

    private val buttonClick = View.OnClickListener{
        showToast(if(viewModel.answerQuizQuestion(Integer.valueOf(it.tag as String))){
            Toast.makeText(requireContext(), "Correct!", Toast.LENGTH_SHORT)
        }
        else{
            Toast.makeText(requireContext(), "Wrong!", Toast.LENGTH_SHORT)
        })

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_quiz, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        answer_1_button.setOnClickListener(buttonClick)
        answer_2_button.setOnClickListener(buttonClick)
        answer_3_button.setOnClickListener(buttonClick)
        answer_4_button.setOnClickListener(buttonClick)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.quiz.observe(viewLifecycleOwner, Observer {
            if(it.isInProgress || it.isNew){
                // next question
                refreshUI(it)
            }
        })
    }

    private fun refreshUI(quiz: Quiz){
        // Data binding is probably better here, need to learn it...
        question_textview.text = quiz.currentQuestion!!.kana
        answer_1_button.text = quiz.currentQuestionAnswers[0].meaning
        answer_2_button.text = quiz.currentQuestionAnswers[1].meaning
        answer_3_button.text = quiz.currentQuestionAnswers[2].meaning
        answer_4_button.text = quiz.currentQuestionAnswers[3].meaning
        quiz_progress.progress = 100 - ((100f * quiz.remainingRatio)).roundToInt()
        question_count_textview.text = resources.getString(R.string.format_question_count, quiz.currenQuestionNumber, quiz.totalQuestions)
        correctcount_textview.text = quiz.correctAnswerCount.toString();
        wrongcount_textview.text = quiz.wrongAnswerCount.toString()

    }

}