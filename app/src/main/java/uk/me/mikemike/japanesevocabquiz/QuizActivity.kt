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
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Observer

interface QuizActivityInterface {
    public val factory: QuizViewModelFactory
}

class QuizActivity : AppCompatActivity(), QuizActivityInterface, QuizResulsFragmentListener {

    private var uuid : String = ""

    private val viewModel: QuizViewModel by viewModels{
       factory
    }

    override val factory by lazy {
        QuizViewModelFactory((this.application as JapaneseVocabQuizApplication).repository, uuid)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        uuid = intent.extras!!.getString(UUID_PARAM_NAME, "error")
        if(uuid == "error"){
            quit()
        }
        setContentView(R.layout.activity_quiz)
        viewModel.quiz.observe(this, Observer {
            if(it.isEmpty){
                Log.w("JVQ", "A quiz with no items was created")
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

    }

    private fun showResults(){
        with(supportFragmentManager.beginTransaction()){
            replace(R.id.container, QuizResultsFragment.newInstance())
            commitNow()
        }
    }

    private fun startQuiz(){

        with(supportFragmentManager.beginTransaction()) {
            replace(R.id.container, QuizFragment.newInstance())
            commitNow()
        }
    }


    private fun restart(){
        viewModel.restartTest()
    }

    private fun quit(){
        finish()
    }

    companion object{

        val UUID_PARAM_NAME = "uuid"

        fun createIntent(uuid: String, context: Context) : Intent {
            val intent = Intent(context, QuizActivity::class.java)
            intent.putExtra(UUID_PARAM_NAME, uuid)
            return intent
        }
    }

    override fun onRestartChosen() {
        restart()
    }

    override fun onFinishChosen() {
       quit()
    }
}