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
import kotlinx.android.synthetic.main.adapter_vocabitem.*
import kotlinx.android.synthetic.main.fragment_quiz.*


class QuizFragment : QuizBaseFragment() {

    companion object {

        const val QUESTION_MODE_JAPANESE_QUESTION = 1
        const val QUESTION_MODE_MAIN_LANGUAGE_QUESTION = 2
        const val QUESTION_MODE_RANDOM = 3

        public val DISPLAY_MODE_PARAM_NAME = "japanese_mode"
        public val QUESTION_MODE_PARAM_NAME = "question_mode"

        @JvmStatic
        fun newInstance(mode: Int, questionMode: Int): QuizFragment {
            var b = Bundle()
            b.putInt(DISPLAY_MODE_PARAM_NAME, mode)
            b.putInt(QUESTION_MODE_PARAM_NAME, questionMode)
            return QuizFragment().apply {
                arguments = b
            }

        }
    }

    private var mToast: Toast? = null
    private var mDisplayMode = JapaneseVocabQuiz.DEFAULT_JAPANESE_DISPLAY
    private var mQuestionMode: Int = QuizFragment.QUESTION_MODE_JAPANESE_QUESTION


    private fun showToast(t: Toast) {
        mToast?.cancel()
        mToast = t
        t.show()
    }

    private val buttonClick = View.OnClickListener {
        showToast(
            if (mViewModel.answerQuizQuestion(Integer.valueOf(it.tag as String))) {
                Toast.makeText(requireContext(), "Correct!", Toast.LENGTH_SHORT)
            } else {
                Toast.makeText(requireContext(), "Wrong!", Toast.LENGTH_SHORT)
            }
        )

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
        val source = savedInstanceState ?: arguments
        mDisplayMode = source?.getInt(DISPLAY_MODE_PARAM_NAME, mDisplayMode) ?: mDisplayMode
        mViewModel.quiz.observe(viewLifecycleOwner, Observer {
            if (it.isInProgress || it.isNew) {
                // next question
                refreshUI(it)
            }
        })
    }

    private fun refreshUI(quiz: Quiz) {
        val updateFun = when (mQuestionMode) {
            QUESTION_MODE_JAPANESE_QUESTION -> {
                ::setQuestionJapaneseQuestion
            }
            QUESTION_MODE_MAIN_LANGUAGE_QUESTION -> {
                ::setQuestionMainLanguageQuestion
            }
            else -> ::setQuestionJapaneseQuestion
        }
        updateFun(quiz)
        updateProgress(quiz)
    }


    private fun setQuestionJapaneseQuestion(quiz: Quiz) {
        val current = quiz.currentQuestion
        if (current != null) {
            question_textview.text = getCorrectJapanese(current, mDisplayMode)
            answer_1_button.text = quiz.currentQuestionAnswers[0].meaning
            answer_2_button.text = quiz.currentQuestionAnswers[1].meaning
            answer_3_button.text = quiz.currentQuestionAnswers[2].meaning
            answer_4_button.text = quiz.currentQuestionAnswers[3].meaning
        }
    }


    private fun setQuestionMainLanguageQuestion(quiz: Quiz) {
        val current =  quiz.currentQuestion
        if(current != null){
            mainlanguage_textview.text = current.meaning
            answer_1_button.text = getCorrectJapanese(quiz.currentQuestionAnswers[0], mDisplayMode)
            answer_2_button.text = getCorrectJapanese(quiz.currentQuestionAnswers[1], mDisplayMode)
            answer_3_button.text = getCorrectJapanese(quiz.currentQuestionAnswers[2], mDisplayMode)
            answer_4_button.text = getCorrectJapanese(quiz.currentQuestionAnswers[3], mDisplayMode)
        }
    }


    private fun updateProgress(quiz: Quiz){
        quiz_progress.progress = quiz.progressPercentage
        question_count_textview.text = resources.getString(
            R.string.format_question_count,
            quiz.currenQuestionNumber,
            quiz.totalQuestions
        )
        correctcount_textview.text = quiz.correctAnswerCount.toString();
        wrongcount_textview.text = quiz.wrongAnswerCount.toString()
    }




}




