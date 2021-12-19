import GUI.MainScreen
import Timetable.Timetable
import Persistence.DBConnection.SingletonDBConnector
import Persistence.DBCreator
import Persistence.Entities.course_type.CourseTypeModel

fun main(args: Array<String>) {

    val dbConnector = SingletonDBConnector.getConnector()

    // Uncomment this code if you want to clean the database
    //dbConnection.resetFile()

    val dbCreator = DBCreator(dbConnector)
    dbCreator.buildDatabase()

    println("DATABASE LOCATION: " + dbConnector.dbFileLocation)




    val timetable1 = Timetable(1, "BSc Computer Science", 2019, 2022, true)


    timetable1.addModule(1, "JVM", true)
    timetable1.addModule(2, "HCI", true)

    timetable1.addActivity(1, 1, 1, 1, 0, 1, 9.0, 2.0, 0)
    timetable1.addActivity(2, 1, 1, 1, 1, 1, 9.0, 2.0, 1)
    timetable1.addActivity(3, 1, 1, 1, 0, 2, 9.0, 1.0, 3)
    timetable1.addActivity(4, 1, 1, 1, 2, 1, 9.0, 1.0, 2)

    timetable1.addActivity(5, 1, 1, 2, 4, 1, 9.0, 1.0, 1)

    val mainScreen = MainScreen(timetable1)
    mainScreen.isVisible = true



//    timetable1.print()

//    timetable1.removeActivity(1)
//    timetable1.removeModule(1)

//    timetable1.print()

//    main.update(1, 1, 1, timetable1)


}

