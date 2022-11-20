package com.example.pulsemonitor.data

data class PulseData(
    val date: String,
    val data: MeasureData
) {
    data class MeasureData(
        val time: String,
        val lowerBP: Long,
        val upperBP: Long,
        val pulse: Long
    )
}
