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
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import kotlinx.android.synthetic.main.fragment_quiz_results.*


class QuizResultsFragment() : QuizBaseFragment() {

    companion object{

        public val MODE_PARAM_NAME = "japanese_mode"

        @JvmStatic
        fun newInstance(mode: Int) : QuizResultsFragment {
            var b = Bundle()
            b.putInt(MODE_PARAM_NAME, mode)
            return QuizResultsFragment().apply {
                arguments = b
            }

        }
    }


    private var mMode = JapaneseVocabQuiz.DEFAULT_JAPANESE_DISPLAY

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_quiz_results, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        restart_button.setOnClickListener {
            mViewModel.restartTest()
        }
        //probably shouldnt force the activity to close from a fragment???
        finish_button.setOnClickListener {
            requireActivity().finish()
        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val source = savedInstanceState ?: arguments
        mMode = source?.getInt(QuizFragment.DISPLAY_MODE_PARAM_NAME, mMode) ?:mMode
        mViewModel.quiz.observe(viewLifecycleOwner, Observer{
            if(it != null) updateUI(it)
        })
    }

    private fun updateUI(quiz: Quiz){
        pie_chart.setUsePercentValues(true)

        var entries: MutableList<PieEntry> = mutableListOf<PieEntry>()
        var correct = PieEntry(quiz.correctPercent.toFloat())
        var wrong = PieEntry(quiz.wrongPercent.toFloat())
        entries.add(correct)
        entries.add(wrong)



        var dataSet = PieDataSet(entries, "Results")



        var data = PieData(dataSet)

        pie_chart.data = data


        mistakes_list.adapter = VocabItemRecyclerViewAdapter(quiz.wrongAnswers, mMode)
        mistakes_list.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

    }


}