package com.example.pulsemonitor.ui

import android.app.ActionBar.LayoutParams
import android.graphics.Paint.Align
import android.text.Layout.Alignment
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.marginBottom
import androidx.recyclerview.widget.RecyclerView
import com.example.pulsemonitor.R
import com.example.pulsemonitor.data.PulseData
import org.w3c.dom.Text

class MainAdapter : RecyclerView.Adapter<MainAdapter.MainViewHolder>() {
    private val data = mutableListOf<PulseData>()

    fun setData(pulseDataList: List<PulseData>) {
        data.clear()
        data.addAll(pulseDataList)
        notifyItemRangeChanged(0, pulseDataList.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_item, parent, false)
        return MainViewHolder(view)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        Log.i("MY", "OnBindViewHolder")
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
            Log.i("MY", "Holder is bound")
            val newLayout = LinearLayout(itemView.context)
            val time = AppCompatTextView(itemView.context)
            val pressure = AppCompatTextView(itemView.context)
            val pulse = AppCompatTextView(itemView.context)

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
            newLayout.setBackgroundResource(R.drawable.green_pulse_gradient)
            time.text = item.data.time
            time.gravity = Gravity.START
            pressure.text = context.getString(
                R.string.pressure,
                item.data.upperBP,
                item.data.lowerBP
            )
            pressure.textAlignment = View.TEXT_ALIGNMENT_CENTER
            pulse.setCompoundDrawables(
                AppCompatResources.getDrawable(
                    context,
                    R.drawable.ic_baseline_favorite_24
                ), null, null, null
            )
            pulse.text = item.data.pulse.toString()
            pulse.gravity = Gravity.END
            newLayout.addView(time)
            newLayout.addView(pressure)
            newLayout.addView(pulse)
            layout.addView(newLayout)
        }
    }
}