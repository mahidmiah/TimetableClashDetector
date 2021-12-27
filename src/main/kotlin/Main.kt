import GUI.MainScreen
import Timetable.Timetable
import Persistence.DBConnection.SingletonDBConnector
import Persistence.DBCreator
import Persistence.Entities.course_type.CourseTypeModel
import Persistence.Entities.course_type.CourseTypeResultSetToModel
import Persistence.seeds.compsci.MainCompSciSeed
import Timetable.persistence.fetchTimetable.FetchCourseTimetable
import java.security.AccessControlException
import java.util.logging.Logger

import kotlin.system.exitProcess

fun main(args: Array<String>) {

//    val dbConnector = SingletonDBConnector.getConnector()
//    val logger = Logger.getLogger("main")
//    // Uncomment this code if you want to clean the database
//    try {
//        dbConnector.resetFile()
//
//    } catch (e: AccessControlException) {
//
//
//        logger.warning(e.stackTraceToString());
//        logger.warning("Error(AccessControlException): MAKE SURE NOTHING IS ACCESSING THE FILE: " + dbConnector.dbFileLocation)
//        exitProcess(1)
//    } catch(e: Exception) {
//        println(e)
//        exitProcess(1)
//    }
//
//
//    val dbCreator = DBCreator(dbConnector)
//    dbCreator.buildDatabase()
//
//    println("DATABASE LOCATION: " + dbConnector.dbFileLocation)
//
//    val courseTypes = dbConnector.rawSelectWithModel("SELECT * FROM ${CourseTypeModel().tableName}", CourseTypeResultSetToModel())
//    val courseTypesRaw = dbConnector.rawSelect("SELECT * FROM ${CourseTypeModel().tableName}")
//    for (courseType in courseTypes) {
//        println("Course Type: " + courseType.label);
//    }
//
//    for (courseType in courseTypesRaw) {
//        val id = courseType["id_course_type"]
//        val label = courseType["label"]
//        println("Course Type: ${label}" + courseType["label"] + courseType["id_course_type"]);
//    }
//
//
//    MainCompSciSeed().seed(dbConnector)


    val timetable1 = Timetable(1, "BSc Computer Science", 2019, 2022, true)


    timetable1.addModule(1, "JVM", false)
    timetable1.addModule(2, "HCI", true)

    timetable1.addActivity(1, 1, 1, 1, 0, 1, 9.0, 2.0, 0)
    timetable1.addActivity(2, 1, 1, 1, 1, 1, 9.0, 2.0, 1)
    timetable1.addActivity(3, 1, 1, 1, 0, 2, 9.0, 1.0, 3)
    timetable1.addActivity(4, 1, 1, 1, 2, 1, 9.0, 1.0, 2)

    timetable1.addActivity(5, 1, 1, 2, 4, 1, 9.0, 1.0, 1)



    //val timetable1 = FetchCourseTimetable(dbConnector).fetchWithYear("BSc Computer Science", 2019)
    val mainScreen = MainScreen(timetable1)
    mainScreen.isVisible = true



//    timetable1.print()

//    timetable1.removeActivity(1)
//    timetable1.removeModule(1)

//    timetable1.print()

//    main.update(1, 1, 1, timetable1)


}

