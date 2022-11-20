package com.example.pulsemonitor.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pulsemonitor.data.PulseData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.random.Random

class MainViewModel : ViewModel() {
    private val _result = MutableLiveData<List<PulseData>>()
    val result: LiveData<List<PulseData>> = _result
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private val fireStoreDB = Firebase.firestore

    companion object {
        private const val LOWER_BP = "lower_bp"
        private const val UPPER_BP = "upper_bp"
        private const val PULSE = "pulse"
    }

    fun writeData() {
        coroutineScope.launch {
            val result = PulseData(
                date = getDate(),
                getTime(),
                randomGenerator(LOWER_BP),
                randomGenerator(UPPER_BP),
                randomGenerator(PULSE)
            )
            fireStoreDB.collection("measures")
                .add(result)
                .addOnSuccessListener {
                    Log.i("MY", "Receive data success")
                }
                .addOnFailureListener {
                    Log.e("MY", it.message.toString())
                }
        }
    }

    fun readData() {
        coroutineScope.launch {
            val pulseData = fireStoreDB
                .collection("measures")
                .orderBy("date")
                .orderBy("time")

            pulseData
                .get()
                .addOnSuccessListener { result ->
                    val resultList = mutableListOf<PulseData>()
                    for (doc in result) {
                        println(doc.data)
                        resultList.add(
                            PulseData(
                                doc.data["date"].toString(),
                                doc.data["time"].toString(),
                                doc.data["lowerBP"] as Long,
                                doc.data["upperBP"] as Long,
                                doc.data["pulse"] as Long
                            )
                        )
                        _result.postValue(resultList)
                    }
                }
        }
    }

    private fun getDate(): String {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd MMMM", Locale.getDefault())
        return current.format(formatter)
    }

    private fun getTime(): String {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())
        return current.format(formatter)
    }

    private fun randomGenerator(type: String): Long {
        var value = 0L
        when (type) {
            LOWER_BP -> {
                value = Random.nextLong(50, 90)
            }
            UPPER_BP -> {
                value = Random.nextLong(100, 140)
            }
            PULSE -> {
                value = Random.nextLong(60, 100)
            }
        }
        return value
    }

    override fun onCleared() {
        coroutineScope.cancel()
        super.onCleared()
    }
}