package com.example.pulsemonitor.data

data class PulseData(
    val date: String,
    val time: String,
    val lowerBP: Long,
    val upperBP: Long,
    val pulse: Long
)
