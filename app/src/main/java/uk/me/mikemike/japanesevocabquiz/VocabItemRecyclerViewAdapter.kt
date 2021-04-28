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

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.adapter_vocabitem.view.*

class VocabItemRecyclerViewAdapter(var values: List<VocabItem>, val displayMode: Int) : RecyclerView.Adapter<VocabItemRecyclerViewAdapter.ViewHolder> () {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        public fun bindToVocabItem(item: VocabItem){
            itemView.kanji_textview.text = item.kanji ?: item.kana
            itemView.romaji_textview.text = item.meaning
            itemView.kana_textview.text = item.kana
            itemView.mainlanguage_textview.text = item.romaji
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_vocabitem, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = values.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindToVocabItem(values[position])
    }
}