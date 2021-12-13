package Timetable

class Year(yearNumber: Int) {
    var yearNumber: Int
    var terms = mutableMapOf<Int, Term>()
    init {
        this.yearNumber = yearNumber
        this.terms[1] = Term(1) //Term 1
        this.terms[2] = Term(2) //Term 2
    }
}