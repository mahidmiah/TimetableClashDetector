package Timetable

class Activity(ID: Int, ModuleID: Int, ModuleName: String, StartTime: Double, Duration: Double, ActivityType: Int, Year: Int, Term: Int, Week: Int, Day: Int) {

    val ModuleName: String
    val ID: Int
    val Module: Int
    val StartTime: Double
    val Duration: Double
    val ActivityType: Int

    val Year: Int
    val Term: Int
    val Week: Int
    val Day: Int

    val lessonType = mutableMapOf(0 to "Lecture", 1 to "Lab", 2 to "Tutorial", 3 to "Exam")

    init {
        this.ID = ID
        this.Module = ModuleID
        this.ModuleName = ModuleName
        this.StartTime = StartTime
        this.Duration = Duration
        this.ActivityType = ActivityType
        this.Year = Year
        this.Term = Term
        this.Week = Week
        this.Day = Day
    }

    override fun toString(): String {
        return "ID: ${this.ID} - ${this.ModuleName} - (${lessonType[this.ActivityType]})"
    }

}