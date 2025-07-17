package com.london.data.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module(includes =[DataBaseModule::class, NetworkModule::class] )
@ComponentScan("com.london.data.**")
class DataModule{}
