package com.example.busschedule

import android.app.Application
import database.schedule.AppDatabase

class BusScheduleApplication : Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
}