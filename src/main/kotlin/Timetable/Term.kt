package Timetable

class Term(termNumber: Int) {
    var termNumber: Int
    var weeks = mutableMapOf<Int, Week>()
    init {
        this.termNumber = termNumber
        this.weeks[1] = Week(1) //Week 1
        this.weeks[2] = Week(2) //Week 2
    }
}