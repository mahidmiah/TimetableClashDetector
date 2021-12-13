package Timetable

class Activity(ID: Int, ModuleID: Int, StartTime: Double, Duration: Double, ActivityType: Int, Year: Int, Term: Int, Week: Int, Day: Int) {

    val ID: Int
    val Module: Int
    val StartTime: Double
    val Duration: Double
    val ActivityType: Int

    val Year: Int
    val Term: Int
    val Week: Int
    val Day: Int

    init {
        this.ID = ID
        this.Module = ModuleID
        this.StartTime = StartTime
        this.Duration = Duration
        this.ActivityType = ActivityType
        this.Year = Year
        this.Term = Term
        this.Week = Week
        this.Day = Day
    }

    override fun toString(): String {
        //return "[ID: ${this.ID} - Module: ${this.Module}]"
        return "ID: ${this.ID}"
    }

}