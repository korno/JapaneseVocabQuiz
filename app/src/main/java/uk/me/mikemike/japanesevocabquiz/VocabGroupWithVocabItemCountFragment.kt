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
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_vocabgroup_list.*

class VocabGroupWithVocabItemCountFragment : Fragment() {


    private val viewModel: VocabGroupListViewModel by activityViewModels {
        VocabGroupListViewModelFactory((requireActivity().application as JapaneseVocabQuizApplication).repository)
    }
    private val allVocabGroupsObserver: Observer<List<VocabGroupWithVocabItemCount>> = Observer { i ->
        dataAdapter.refreshData(i)
    }

    private val dataAdapter = VocabGroupWithVocabItemCountRecyclerViewAdapter(mutableListOf(), VocabGroupListener {
        startActivity(QuizActivity.createIntent(it.vocabGroup.uuid, requireContext()))
    })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_vocabgroup_list, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(vocabgroup_list) {
            layoutManager = LinearLayoutManager(context)
            adapter = dataAdapter
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.allGroupsAlphabetizedWithVocabItemCount.observe(viewLifecycleOwner, allVocabGroupsObserver)
    }


}