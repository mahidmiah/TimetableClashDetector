package Timetable

class Day (dayInt: Int){

    var dayOfWeek = dayInt
    var TimeSlot = HashMap<Double, MutableList<Activity>?>()

    init {
        var startNum = 9.0
        repeat(25) {
            this.TimeSlot[startNum] = null
            startNum += 0.5
        }
    }

}