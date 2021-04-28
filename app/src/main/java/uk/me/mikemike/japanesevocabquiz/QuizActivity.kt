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

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.preference.PreferenceManager

interface QuizActivityInterface {
    public val mFactory: QuizViewModelFactory
}

class QuizActivity : AppCompatActivity(), QuizActivityInterface {


    companion object{
        val UUID_PARAM_NAME = "uuid"
        @JvmStatic
        fun createIntent(uuid: String,  context: Context) : Intent {
            val intent = Intent(context, QuizActivity::class.java)
            intent.putExtra(UUID_PARAM_NAME, uuid)
            return intent
        }
    }

    private var mUUID : String = ""


    private val mViewModel: QuizViewModel by viewModels{
       mFactory
    }

    override val mFactory by lazy {
        QuizViewModelFactory((this.application as JapaneseVocabQuizApplication).repository, mUUID)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mUUID = intent.extras!!.getString(UUID_PARAM_NAME, "error")
        if(mUUID == "error"){
           finish()
        }
        setContentView(R.layout.activity_quiz)
        mViewModel.quiz.observe(this, Observer {
            if(it.isEmpty){
                finish()
            }
            else {
                if(it.isFinished){
                    showResults()
                }
                else if(it.isNew){
                    startQuiz()
                }
            }

        })

        mViewModel.vocabGroup.observe(this,
        Observer {
            supportActionBar?.title = it.vocabGroup.name
            title= it.vocabGroup.name
        })



    }


    private fun showResults(){
        with(supportFragmentManager.beginTransaction()){
            replace(R.id.container, QuizResultsFragment.newInstance(getJapaneseDisplayMode()))
            commitNow()
        }
    }

    private fun startQuiz(){
        with(supportFragmentManager.beginTransaction()) {
            replace(R.id.container, QuizFragment.newInstance(getJapaneseDisplayMode()))
            commitNow()
        }
    }
}