package com.example.arithmeticrunner

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment

class MainFragment : Fragment(), AdapterView.OnItemSelectedListener {
    companion object {
        const val TAG = "MainFragment"

        private val digitSpinnerArray = listOf("1", "2", "3")
        private val operatorSpinnerArray =
            listOf(Operator.ADD.opStr, Operator.SUB.opStr, Operator.MUL.opStr, Operator.DIV.opStr)
        private val problemsNumSpinnerArray = listOf("10", "20", "30", "50", "100")
    }

    private var activityCallBack: MainFragmentListener? = null

    private var topRecords: ArrayList<Record>? = null
    private var problemsConfig: ProblemsConfig? = null

    private var digit1Spinner: Spinner? = null
    private var digit2Spinner: Spinner? = null
    private var operatorSpinner: Spinner? = null
    private var problemsNumSpinner: Spinner? = null

    private var firstTimeMainText: TextView? = null
    private var secondTimeMainText: TextView? = null
    private var thirdTimeMainText: TextView? = null

    interface MainFragmentListener {
        fun onStartMainBtnClick(problemsConfig: ProblemsConfig)
        fun onSelectedItemChanged(fragment: MainFragment, top: Long, problemsConfig: ProblemsConfig)
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        activityCallBack?.onSelectedItemChanged(
            this, 3, ProblemsConfig(
                digit1Spinner?.selectedItem.toString().toInt(),
                digit2Spinner?.selectedItem.toString().toInt(),
                operatorSpinner?.selectedItem.toString().let { Operator.s2o(it) },
                problemsNumSpinner?.selectedItem.toString().toInt()
            )
        )
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            activityCallBack = context as MainFragmentListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement MainFragmentListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            it.getParcelableArrayList<Record>("topRecords")?.let {
                topRecords = it
            }
            it.getParcelable<ProblemsConfig>("problemsConfig")?.let {
                problemsConfig = it
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        digit1Spinner = view.findViewById(R.id.digit1_main_spinner)
        digit2Spinner = view.findViewById(R.id.digit2_main_spinner)
        operatorSpinner = view.findViewById(R.id.operator_main_spinner)
        problemsNumSpinner = view.findViewById(R.id.problems_num_main_spinner)

        ArrayAdapter(
            view.context,
            android.R.layout.simple_spinner_item,
            digitSpinnerArray
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            digit1Spinner?.apply {
                this.adapter = adapter
                problemsConfig?.digit1.toString().let {
                    setSelection(digitSpinnerArray.indexOf(it))
                }
            }
            digit2Spinner?.apply {
                this.adapter = adapter
                problemsConfig?.digit2.toString().let {
                    setSelection(digitSpinnerArray.indexOf(it))
                }
            }
        }

        ArrayAdapter(
            view.context,
            android.R.layout.simple_spinner_item,
            operatorSpinnerArray
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            operatorSpinner?.apply {
                this.adapter = adapter
                problemsConfig?.operator?.opStr.let {
                    setSelection(operatorSpinnerArray.indexOf(it))
                }
            }
        }

        ArrayAdapter(
            view.context,
            android.R.layout.simple_spinner_item,
            problemsNumSpinnerArray
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            problemsNumSpinner?.apply {
                this.adapter = adapter
                problemsConfig?.problemsNum.toString().let {
                    setSelection(problemsNumSpinnerArray.indexOf(it))
                }
            }
        }

        listOf(digit1Spinner, digit2Spinner, operatorSpinner, problemsNumSpinner).forEach {
            it?.onItemSelectedListener = this
        }

        firstTimeMainText = view.findViewById(R.id.first_time_main_text)
        secondTimeMainText = view.findViewById(R.id.second_time_main_text)
        thirdTimeMainText = view.findViewById(R.id.third_time_main_text)
        listOf(
            firstTimeMainText,
            secondTimeMainText,
            thirdTimeMainText
        ).forEachIndexed { index, v ->
            v?.apply {
                text = topRecords?.getOrNull(index)?.timerStr ?: "99'99'99"
            }
        }

        view.findViewById<Button?>(R.id.start_main_btn)?.apply {
            setOnClickListener { v: View -> startMainBtnClicked(v) }
        }
    }

    fun updateTopRecordsMain(records: ArrayList<Record>) {
        listOf(
            firstTimeMainText,
            secondTimeMainText,
            thirdTimeMainText
        ).forEachIndexed { index, v ->
            v?.apply {
                text = records?.getOrNull(index)?.timerStr ?: "99'99'99"
            }
        }
    }

    private fun startMainBtnClicked(view: View) {
        activityCallBack?.onStartMainBtnClick(
            ProblemsConfig(
                digit1Spinner?.selectedItem.toString().toInt(),
                digit2Spinner?.selectedItem.toString().toInt(),
                operatorSpinner?.selectedItem.toString().let { it -> Operator.s2o(it) },
                problemsNumSpinner?.selectedItem.toString().toInt()
            )
        )
    }
}