package Timetable

class Day (dayNumber: Int){
    var dayNumber: Int
    var TimeSlot = HashMap<Double, MutableList<Activity>?>()
    init {
        this.dayNumber = dayNumber
        //A day lasts from 9am (9.0) to 9pm (21.0)
        var startNum = 9.0
        repeat(25) {
            this.TimeSlot[startNum] = null
            startNum += 0.5
        }
    }
}