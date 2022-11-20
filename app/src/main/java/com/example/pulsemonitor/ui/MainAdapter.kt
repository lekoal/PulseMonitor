package com.example.pulsemonitor.ui

import android.app.ActionBar.LayoutParams
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.pulsemonitor.R
import com.example.pulsemonitor.data.PulseData

class MainAdapter : RecyclerView.Adapter<MainAdapter.MainViewHolder>() {
    private var data: List<PulseData> = emptyList()

    fun setData(pulseDataList: List<PulseData>) {
        notifyItemRangeRemoved(0, data.size)
        data = pulseDataList
        notifyItemRangeChanged(0, pulseDataList.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_item, parent, false)
        return MainViewHolder(view)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        var prevDate = ""
        if (position > 0) {
            prevDate = data[position - 1].date
        }
        holder.bind(data[position], prevDate)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val date: AppCompatTextView = itemView.findViewById(R.id.rv_item_date)
        private val layout: LinearLayout = itemView.findViewById(R.id.rv_item_data_layout)
        private val context = itemView.context

        fun bind(item: PulseData, prevDate: String) {
            val newLayout = LinearLayout(itemView.context)
            newLayout.layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            val time = AppCompatTextView(itemView.context)
            time.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
            time.layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1f
            )
            val pressure = AppCompatTextView(itemView.context)
            pressure.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f)
            pressure.setTextColor(ContextCompat.getColor(context, R.color.black))
            pressure.layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1f
            )
            val pulse = AppCompatTextView(itemView.context)
            pulse.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f)
            pulse.layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1f
            )

            if (item.date != prevDate) {
                date.visibility = View.VISIBLE
                date.text = item.date
            } else {
                date.visibility = View.GONE
            }

            newLayout.orientation = LinearLayout.HORIZONTAL
            val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            layoutParams.bottomMargin = 5
            newLayout.layoutParams = layoutParams
            if (item.lowerBP < 80 && item.upperBP < 120) {
                newLayout.setBackgroundResource(R.drawable.green_pulse_gradient)
            } else if (item.lowerBP < 80 && item.upperBP in 120..129) {
                newLayout.setBackgroundResource(R.drawable.yellow_pulse_gradient)
            } else {
                newLayout.setBackgroundResource(R.drawable.orange_pulse_gradient)
            }

            time.text = item.time
            pressure.text = context.getString(
                R.string.pressure,
                item.upperBP,
                item.lowerBP
            )
            pressure.textAlignment = View.TEXT_ALIGNMENT_CENTER
            pulse.text = context.getString(R.string.pulse, item.pulse)
            pulse.gravity = Gravity.END
            newLayout.addView(time)
            newLayout.addView(pressure)
            newLayout.addView(pulse)
            layout.addView(newLayout)
        }
    }
}