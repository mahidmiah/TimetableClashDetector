package Timetable

class Week {

    var days = mutableListOf<Day>()

    init {
        for (i in 0..4) {
            this.days.add(Day())
        }
    }

}