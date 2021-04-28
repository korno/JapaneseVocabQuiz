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


import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup



import kotlinx.android.synthetic.main.adapter_vocabgroupitem.view.*;

class VocabGroupListener(val selectListener: (value: VocabGroupWithVocabItemCount) -> Unit){
    fun onSelected(value: VocabGroupWithVocabItemCount) = selectListener(value)
}

class VocabGroupWithVocabItemCountRecyclerViewAdapter(
    private val values: MutableList<VocabGroupWithVocabItemCount>,
    private val selectedListener: VocabGroupListener
) : RecyclerView.Adapter<VocabGroupWithVocabItemCountRecyclerViewAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        public fun bindVocabGroup(vals: VocabGroupWithVocabItemCount){
            itemView.vocabgroup_name.text = vals.vocabGroup.name
            itemView.vocabgroup_description.text = vals.vocabGroup.description
            itemView.start_quiz_button.setOnClickListener {
                selectedListener.onSelected(vals)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_vocabgroupitem, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.bindVocabGroup(item)
    }

    public fun refreshData(newValues: List<VocabGroupWithVocabItemCount>){
        this.values.clear()
        this.values.addAll(newValues)
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int = values.size


}