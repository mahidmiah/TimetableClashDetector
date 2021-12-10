package Timetable

class Day (){

    var TimeSlot = HashMap<Double, MutableList<Activity>?>()

    init {
        var startNum = 9.0
        repeat(25) {
            this.TimeSlot[startNum] = null
            startNum += 0.5
        }
    }

}