package com.example.pulsemonitor.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pulsemonitor.databinding.ActivityMainBinding
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.qualifier.named

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val adapter: MainAdapter by lazy {
        get(named("main_adapter"))
    }
    private val viewModel: MainViewModel by viewModel(named("main_view_model"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fab.setOnClickListener {
            viewModel.writeData()
            viewModel.readData()
        }
        initRV()
        receiveData()
    }

    private fun initRV() {
        binding.mainRv.layoutManager = LinearLayoutManager(this)
        binding.mainRv.adapter = adapter
    }

    private fun receiveData() {
        viewModel.readData()
        viewModel.result.observe(this) { result ->
            adapter.setData(result)
        }
    }
}