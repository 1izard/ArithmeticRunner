package com.example.arithmeticrunner

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView


class ResultRecyclerAdapter(private val problems: ArrayList<Problem>) :
    RecyclerView.Adapter<ResultRecyclerAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val problemNoText: TextView = itemView.findViewById(R.id.problem_no_item_text)
        val problemExprText: TextView = itemView.findViewById(R.id.problem_expr_item_text)
        val correctImage: ImageView = itemView.findViewById(R.id.correct_item_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return LayoutInflater.from(parent.context)
            .inflate(R.layout.results_result_recycler_item, parent, false).let {
                ViewHolder(it)
            }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val problem = problems[position]

        holder.problemNoText.text = problem.no.fmtQNo(itemCount.toString().length)
        holder.problemExprText.text = problem.run {
            "${operand1.fmt(digit1)} ${operator.opStr} ${operand2.fmt(digit2)} = $answer"
        }
        holder.correctImage.apply {
            if (problem.correct) {
                setImageResource(R.drawable.ic_baseline_radio_button_unchecked_24)
                setColorFilter(
                    ContextCompat.getColor(context, R.color.waterblue2),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
            } else {
                setImageResource(R.drawable.ic_baseline_close_24)
                setColorFilter(
                    ContextCompat.getColor(context, R.color.red1),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return problems.size
    }
}