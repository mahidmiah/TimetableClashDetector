package Timetable

class Week(weekNumber: Int) {
    var weekNumber: Int
    var days = mutableMapOf<Int, Day>()
    init {
        this.weekNumber = weekNumber
        for (i in 0..4) {
            this.days[i] = Day(i) //Days 0-4 represent Monday to Friday.
        }
    }
}