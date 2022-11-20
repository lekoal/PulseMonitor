package com.example.pulsemonitor.di

import com.example.pulsemonitor.ui.MainAdapter
import com.example.pulsemonitor.ui.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val mainKoinModule = module {
    single(named("main_adapter")) {
        MainAdapter()
    }
    viewModel(named("main_view_model")) {
        MainViewModel()
    }
}