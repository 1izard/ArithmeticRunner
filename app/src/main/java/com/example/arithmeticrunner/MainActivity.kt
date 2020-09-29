package com.example.arithmeticrunner

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where

class MainActivity : AppCompatActivity(), MainFragment.MainFragmentListener,
    GameFragment.GameFragmentListener, ResultFragment.ResultFragmentListener {

    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        realm = Realm.getDefaultInstance()

        val topRecords = selectTopRecords(3, ProblemsConfig(1, 1, Operator.ADD, 20)).toList().let {
            ArrayList(it)
        }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_container, MainFragment().apply {
                    arguments = Bundle().apply {
                        putParcelableArrayList("topRecords", topRecords)
                    }
                })
                .commit()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    override fun onStartMainBtnClick(problemsConfig: ProblemsConfig) {
        GameFragment().apply {
            arguments = Bundle().apply {
                putParcelable("problemsConfig", problemsConfig)
            }
        }.let {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_container, it)
                .commit()
        }
    }

    override fun onSelectedItemChanged(
        fragment: MainFragment,
        top: Long,
        problemsConfig: ProblemsConfig
    ) {
        selectTopRecords(3, problemsConfig).let {
            ArrayList<Record>(it)
        }.let {
            fragment.updateTopRecordsMain(it)
        }
    }

    override fun onQuitGameBtnClick(problemsConfig: ProblemsConfig) {
        transferToMainFragment(problemsConfig)
    }

    override fun onNextGameBtnClick(
        problemsConfig: ProblemsConfig,
        problems: ArrayList<Problem>?,
        timerString: String
    ) {
        ResultFragment().apply {
            arguments = Bundle().apply {
                putParcelable("problemsConfig", problemsConfig)
                putParcelableArrayList("problems", problems)
                putString("timerString", timerString)
            }
        }.let {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_container, it)
                .commit()
        }
    }

    override fun saveResult(problemsConfig: ProblemsConfig, timerStr: String) {
        realm.executeTransaction { realm ->
            realm.createObject<Record>().apply {
                digit1 = problemsConfig.digit1
                digit2 = problemsConfig.digit2
                opStr = problemsConfig.operator.opStr
                problemsNum = problemsConfig.problemsNum
                this.timerStr = timerStr
            }
        }
    }

    override fun onHomeResultBtnClick(problemsConfig: ProblemsConfig) {
        transferToMainFragment(problemsConfig)
    }

    override fun onRetryResultBtnClick(problemsConfig: ProblemsConfig) {
        GameFragment().apply {
            arguments = Bundle().apply {
                putParcelable("problemsConfig", problemsConfig)
            }
        }.let {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_container, it)
                .commit()
        }
    }

    private fun selectTopRecords(top: Long, problemsConfig: ProblemsConfig) =
        realm.where<Record>()
            .sort("timerStr")
            .equalTo("digit1", problemsConfig.digit1)
            .equalTo("digit2", problemsConfig.digit2)
            .equalTo("opStr", problemsConfig.operator.opStr)
            .equalTo("problemsNum", problemsConfig.problemsNum)
            .limit(top)
            .findAll()

    private fun transferToMainFragment(
        problemsConfig: ProblemsConfig
    ) {
        MainFragment().apply {
            arguments = Bundle().apply {
                putParcelableArrayList(
                    "topRecords",
                    selectTopRecords(3, problemsConfig).let {
                        ArrayList<Record>(it)
                    })
                putParcelable("problemsConfig", problemsConfig)
            }
        }.let {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_container, it)
                .commit()
        }
    }
}