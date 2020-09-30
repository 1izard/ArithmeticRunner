package com.example.arithmeticrunner

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class GameFragment : Fragment() {
    companion object {
        const val TAG = "GameFragment"
    }

    private var activityCallback: GameFragmentListener? = null
    private var problemsConfig: ProblemsConfig = ProblemsConfig(1, 1, Operator.ADD, 20)
    private var problems: ArrayList<Problem>? = null

    private var problemNoGameText: TextView? = null
    private var operand1GameText: TextView? = null
    private var operand2GameText: TextView? = null
    private var operatorGameText: TextView? = null
    private var inputAnswerGameText: TextView? = null
    private var correctGameImage: ImageView? = null
    private var timerGameText: TextView? = null

    interface GameFragmentListener {
        fun onQuitGameBtnClick(problemsConfig: ProblemsConfig)
        fun onNextGameBtnClick(
            problemsConfig: ProblemsConfig,
            problems: ArrayList<Problem>?,
            timerString: String
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            activityCallback = context as GameFragmentListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement GameFragmentListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            it.getParcelable<ProblemsConfig>("problemsConfig")?.let { it ->
                problemsConfig = it
            }
        }
        problems = ArrayList(List(problemsConfig.problemsNum) {
            Problem(
                it + 1,
                problemsConfig.digit1,
                problemsConfig.digit2,
                problemsConfig.operator
            )
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.problem_no_game_text)?.let {
            problemNoGameText = it
        }
        view.findViewById<TextView>(R.id.operand1_game_text)?.let {
            operand1GameText = it
        }
        view.findViewById<TextView>(R.id.operand2_game_text)?.let {
            operand2GameText = it
        }
        view.findViewById<TextView>(R.id.operator_game_text)?.let {
            operatorGameText = it
        }
        view.findViewById<TextView>(R.id.input_answer_game_text)?.let {
            inputAnswerGameText = it
        }
        view.findViewById<ImageView>(R.id.correct_game_image)?.let {
            correctGameImage = it
        }
        view.findViewById<TextView>(R.id.timer_game_text)?.let {
            timerGameText = it
        }

        problems?.get(0)?.let {
            problemNoGameText?.text = it.no.fmtQNo(problemsConfig.problemsNum.toString().length)
            operand1GameText?.text = it.operand1.toString()
            operand2GameText?.text = it.operand2.toString()
            operatorGameText?.text = it.operator.opStr
        }

        view.findViewById<Button>(R.id.int_zero_game_btn)
            .setOnClickListener { v -> onIntZeroGameBtnClick(v) }

        listOf(
            R.id.int_one_game_btn,
            R.id.int_two_game_btn,
            R.id.int_three_game_btn,
            R.id.int_four_game_btn,
            R.id.int_five_game_btn,
            R.id.int_six_game_btn,
            R.id.int_seven_game_btn,
            R.id.int_eight_game_btn,
            R.id.int_nine_game_btn
        ).forEachIndexed { idx, it ->
            view.findViewById<Button>(it).setOnClickListener { v -> onIntGameBtnClick(v, idx + 1) }
        }

        view.findViewById<ImageButton>(R.id.minus_sign_game_btn)
            .setOnClickListener { v -> onMinusSignGameBtnClick(v) }
        view.findViewById<ImageButton>(R.id.backspace_game_btn)
            .setOnClickListener { v -> onBackspaceBtnClick(v) }
        view.findViewById<Button>(R.id.next_game_btn)
            .setOnClickListener { v -> nextGameBtnClicked(v) }
        view.findViewById<Button>(R.id.quit_game_btn)
            .setOnClickListener { v -> quitGameBtnClicked(v) }

        val msecTimelimit: Long = 99 * 60 * 1000 + 99 * 1000 + 999
        object : CountDownTimer(msecTimelimit, 10) {
            override fun onTick(millisUntilFinished: Long) {
                val msecCount = msecTimelimit - millisUntilFinished
                timerGameText?.text =
                    "${(msecCount / 1000 / 60).fmt(2, 0)}'${(
                            msecCount / 1000 % 60
                            ).fmt(2, 0)}'${(msecCount % 1000 / 10).fmt(2, 0)}"
            }

            override fun onFinish() {
            }
        }.start()
    }

    private fun onIntGameBtnClick(view: View, i: Int) {
        inputAnswerGameText?.apply {
            text = (text.toString() + i.toString()).toInt().toString().let {
                it.slice(IntRange(0, minOf(7, it.length - 1)))
            }
        }
        correctGameImage?.visibility = View.INVISIBLE
    }

    private fun onIntZeroGameBtnClick(view: View) {
        inputAnswerGameText?.apply {
            text.toString().let {
                if (it != "0" && it != "-0") {
                    text = (it + "0").let {
                        it.slice(IntRange(0, minOf(7, it.length - 1)))
                    }
                }
            }
        }
        correctGameImage?.visibility = View.INVISIBLE
    }

    private fun onMinusSignGameBtnClick(view: View) {
        inputAnswerGameText?.apply {
            text.toString().let {
                try {
                    text = (-text.toString().toInt()).toString().let {
                        it.slice(IntRange(0, minOf(7, it.length - 1)))
                    }
                } catch (e: NumberFormatException) {
                    if (it.isEmpty()) {
                        text = "-"
                    } else if (it == "-") {
                        text = ""
                    }
                }
            }
        }
        correctGameImage?.visibility = View.INVISIBLE
    }

    private fun onBackspaceBtnClick(view: View) {
        inputAnswerGameText?.apply {
            text = text.toString().dropLast(1)
        }
        correctGameImage?.visibility = View.INVISIBLE
    }

    private fun nextGameBtnClicked(view: View) {
        val inputAnswer = inputAnswerGameText?.text.toString().let {
            try {
                it.toInt().toString()
            } catch (e: java.lang.NumberFormatException) {
                ""
            }
        }
        val problemNo = problemNoGameText?.text.toString().split(" ")[1].toInt()
        problems?.get(problemNo - 1)?.let {
            if (inputAnswer == it.answer.toString()) {
                correctGameImage?.apply {
                    setImageResource(R.drawable.ic_baseline_radio_button_unchecked_24)
                    setColorFilter(
                        ContextCompat.getColor(context, R.color.waterblue2),
                        android.graphics.PorterDuff.Mode.SRC_IN
                    )
                    visibility = View.VISIBLE
                }
//                correctGameImage?.visibility = View.INVISIBLE
                problems?.getOrNull(problemNo)?.let {
                    problemNoGameText?.text =
                        it.no.fmtQNo(problemsConfig.problemsNum.toString().length)
                    operand1GameText?.text = it.operand1.toString()
                    operand2GameText?.text = it.operand2.toString()
                    operatorGameText?.text = it.operator.opStr
                    inputAnswerGameText?.text = ""
                } ?: activityCallback?.onNextGameBtnClick(
                    problemsConfig,
                    problems,
                    timerGameText?.text.toString()
                )
            } else {
                it.correct = false
                correctGameImage?.apply {
                    setImageResource(R.drawable.ic_baseline_close_24)
                    setColorFilter(
                        ContextCompat.getColor(context, R.color.red1),
                        android.graphics.PorterDuff.Mode.SRC_IN
                    )
                    visibility = View.VISIBLE
                }
            }
        }
    }

    private fun quitGameBtnClicked(view: View) {
        activityCallback?.onQuitGameBtnClick(problemsConfig)
    }
}

