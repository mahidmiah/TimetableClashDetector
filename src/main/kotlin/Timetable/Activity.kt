package Timetable

import com.comp1815.Timetable.Module

class Activity(ID: Int, ModuleID: Int, StartTime: Double, Duration: Double, ActivityType: Int) {

    val ID: Int
    val Module: Int
    val StartTime: Double
    val Duration: Double
    val ActivityType: Int

    init {
        this.ID = ID
        this.Module = ModuleID
        this.StartTime = StartTime
        this.Duration = Duration
        this.ActivityType = ActivityType
    }

}