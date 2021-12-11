import GUI.MainScreen
import Timetable.Timetable

fun main(args: Array<String>) {

    val main = MainScreen()
    main.isVisible = true

    val timetable1 = Timetable(1, "BSc Computer Science", 2019, 2022, 1)
    timetable1.addModule(1, "JVM", true)
    timetable1.addActivity(1, 1, 0, 1, 9.0, 1.0, 1)

    main.update(timetable1)

}