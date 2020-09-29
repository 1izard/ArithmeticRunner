package com.example.arithmeticrunner

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ResultFragment : Fragment() {
    private var activityCallback: ResultFragmentListener? = null

    private var problemsConfig: ProblemsConfig = ProblemsConfig(1, 1, Operator.ADD, 20)
    private var problems: ArrayList<Problem>? = null
    private var timerStr: String = "99'99'99"

    interface ResultFragmentListener {
        fun saveResult(problemsConfig: ProblemsConfig, timerStr: String)
        fun onHomeResultBtnClick(problemsConfig: ProblemsConfig)
        fun onRetryResultBtnClick(problemsConfig: ProblemsConfig)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            activityCallback = context as ResultFragmentListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement ResultFragmentListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            it.getParcelable<ProblemsConfig>("problemsConfig")?.let {
                problemsConfig = it
            }
            problems = it.getParcelableArrayList("problems")
            it.getString("timerString")?.let {
                timerStr = it
            }
        }

        activityCallback?.saveResult(problemsConfig, timerStr)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        problems?.let {
            view.findViewById<RecyclerView>(R.id.results_result_recycler).apply {
                layoutManager = LinearLayoutManager(context)
                adapter = ResultRecyclerAdapter(it)
            }
        }

        view.findViewById<TextView>(R.id.duration_result_text).text = timerStr
        problems?.count { !it.correct }?.let { incorrectCount ->
            view.findViewById<TextView>(R.id.penalty_expr_result_text).text =
                "(X num = $incorrectCount) * 5 sec"
            (incorrectCount * 5).let { penaltySec ->
                view.findViewById<TextView>(R.id.penalty_sec_result_text).text = "$penaltySec sec"
                timerStr?.split("'")!!.map { it.toInt() }.let { timeList ->
                    timeList[0] * 60 * 1000 + timeList[1] * 1000 + timeList[2] + penaltySec * 1000
                }.let { timerMsec ->
                    view.findViewById<TextView>(R.id.result_result_text).text =
                        "${(timerMsec / 1000 / 60).fmt(2, 0)}'${(timerMsec / 1000 % 60).fmt(
                            2,
                            0
                        )}'${(timerMsec % 1000).fmt(
                            2, 0
                        )}"
                }
            }
        }

        view.findViewById<Button>(R.id.home_result_btn)
            .setOnClickListener { v -> homeResultBtnClicked(v) }
        view.findViewById<Button>(R.id.retry_result_btn)
            .setOnClickListener { v -> retryResultBtnClicked(v) }
    }

    private fun homeResultBtnClicked(view: View) {
        activityCallback?.onHomeResultBtnClick(problemsConfig)
    }

    private fun retryResultBtnClicked(view: View) {
        activityCallback?.onRetryResultBtnClick(problemsConfig)
    }
}