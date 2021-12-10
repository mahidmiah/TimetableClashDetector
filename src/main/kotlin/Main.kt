import GUI.MainScreen
import Timetable.Timetable

fun main(args: Array<String>) {

    val timetable1 = Timetable(1, "BSc Computer Science", 2019, 2022, 1)
    timetable1.addModule(1, "HCI", false)
    timetable1.addModule(28, "JVM", true)
    timetable1.addModule(5, "Maths", true)

    timetable1.addActivity(10, 1, 0, 1, 9.0, 2.5, 1)
    timetable1.addActivity(20, 1, 0, 2, 9.0, 2.0, 1)
//    timetable1.addActivity(20, 1, 0, 1, 12.0, 3.0, 1)
//    timetable1.addActivity(40, 1, 0, 1, 14.5, 3.0, 1)
//    timetable1.addActivity(30, 1, 0, 1, 9.0, 2.0, 1)
//    timetable1.addActivity(60, 1, 1, 1, 9.0, 2.0, 1)

    timetable1.print()

    timetable1.removeModule(1)

    timetable1.print()

}