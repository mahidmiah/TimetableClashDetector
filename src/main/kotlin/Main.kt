import GUI.MainScreen
import Timetable.Timetable

fun main(args: Array<String>) {



    val timetable1 = Timetable(1, "BSc Computer Science", 2019, 2022, true)

    val main = MainScreen(timetable1)
    main.isVisible = true

    timetable1.addModule(1, "JVM", true)
    timetable1.addModule(2, "HCI", true)

    timetable1.addActivity(1, 1, 1, 1, 0, 1, 9.0, 2.0, 1)
    timetable1.addActivity(2, 1, 1, 1, 1, 1, 9.0, 2.0, 1)
    timetable1.addActivity(3, 1, 1, 1, 0, 2, 9.0, 1.0, 1)
    timetable1.addActivity(4, 1, 1, 1, 2, 1, 9.0, 1.0, 1)

//    timetable1.print()

//    timetable1.removeActivity(1)
//    timetable1.removeModule(1)

//    timetable1.print()

    main.update(1, 1, 1, timetable1)


}