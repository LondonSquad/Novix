package com.london.app.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module(includes = [DatabaseModule::class])
@ComponentScan("com.london")
class AppModule