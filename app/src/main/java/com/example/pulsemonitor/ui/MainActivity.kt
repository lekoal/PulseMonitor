package com.example.pulsemonitor.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pulsemonitor.data.PulseData
import com.example.pulsemonitor.databinding.ActivityMainBinding
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var fireStoreDB: FirebaseFirestore
    private lateinit var adapter: MainAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = MainAdapter()
        fireStoreDB = Firebase.firestore
        binding.fab.setOnClickListener {
            val result = PulseData(
                date = getDate(),
                PulseData.MeasureData(
                    getTime(),
                    90,
                    130,
                    72
                )
            )
            fireStoreDB.collection("measures")
                .add(result)
                .addOnSuccessListener {
                    Toast.makeText(this, "SUCCESS", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "FAIL", Toast.LENGTH_SHORT).show()
                    Log.e("MY", it.message.toString())
                }
            receiveData()
        }
        initRV()
    }

    private fun initRV() {
        binding.mainRv.layoutManager = LinearLayoutManager(this)
        binding.mainRv.adapter = adapter
    }

    private fun receiveData() {
        val pulseData = fireStoreDB
            .collection("measures").document("date")
            .collection("data")

        pulseData
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    println(document.data)
                }
            }
//        fireStoreDB
//            .collection("measures")
//            .orderBy("date")
//            .orderBy("data.time", Query.Direction.ASCENDING)
//            .get()
//            .addOnSuccessListener { result ->
//                val resultList = mutableListOf<PulseData>()
//                for (document in result) {
//                    resultList.add(
//                        PulseData(
//                            document.data["date"].toString(),
//                            PulseData.MeasureData(
//                                document.data["data"].toString(),
//                                document.get("lowerBP") as Long,
//                                document.get("upperBP") as Long,
//                                document.get("pulse") as Long
//                            )
//                        )
//                    )
//                }
//                adapter.setData(resultList)
//            }
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
}