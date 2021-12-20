package Timetable.persistence.fetchTimetable

import Persistence.DBConnection.DBConnector
import Persistence.Entities.activity.ActivityModel
import Persistence.Entities.activity.ResultSetToActivity
import Persistence.Entities.course.CourseModel
import Persistence.Entities.course.ResultSetToCourse
import Persistence.Entities.course_module.CourseModuleModel
import Persistence.Entities.course_type.CourseTypeModel
import Persistence.Entities.module.ModuleModel
import Persistence.Entities.timetable.ResultSetToTimetable
import Persistence.Entities.timetable.TimetableModel
import Timetable.Timetable
import java.util.logging.Logger

class FetchCourseTimetable(val dbConnector: DBConnector) {
    fun fetchWithYear(courseName: String, startYear: Int) :  Timetable? {
        val logger = Logger.getLogger("fetchCourseTimetable")
        val courses = dbConnector.rawSelectWithModel(
            "SELECT * FROM ${CourseModel().tableName} WHERE name = '${courseName}' AND start_year = ${startYear};",
            ResultSetToCourse()
        )

        if (courses.size == 0) {
            logger.info("DID NOT FIND COURSES WITH name=${courseName} and start_year = ${startYear}")
            return null
        }

        val targetCourse = courses[0]

        val timetableDocs = dbConnector.rawSelectWithModel("" +
                "SELECT * FROM ${TimetableModel().tableName}\n" +
                "WHERE id_course = ${targetCourse.id_course}",
        ResultSetToTimetable())

        if (timetableDocs.size == 0) {
            logger.info("DID NOT FIND TIMETABLES WITH id_course=${targetCourse.id_course}")
            return null;
        }

        val targetTimetable = timetableDocs[0]
        ActivityModel().id_course_module

        val activitiesQuery ="SELECT act.* FROM ${ActivityModel().tableName} as act\n" +
                "INNER JOIN ${CourseModuleModel().tableName} as cm\n" +
                "   ON cm.id_course_module = act.id_course_module\n" +
                "WHERE cm.id_course = ${targetCourse.id_course}";

        println("ACT QUERY\n" + activitiesQuery)
        val activities = dbConnector.rawSelectWithModel(activitiesQuery,
            ResultSetToActivity()
        )

        println("ACTS" + activities)

        val courseType = targetCourse.fetchCourseType(dbConnector)
        var courseTypeBoolean = true;
        if (courseType != null) {
            courseTypeBoolean = (courseType.label == CourseTypeModel.UNDERGRADUATE)
        }


        val timetable = Timetable(targetTimetable.id_timetable, courseName, targetCourse.start_year!!, targetCourse.end_year!!, courseTypeBoolean)
        //val modulesInfo: ArrayList<ModuleModel> = arrayListOf()
        //val modulesInfoHashMap: MutableMap<Int, ModuleModel> = mutableMapOf<Int, ModuleModel>()
        val courseModulesInfoHashMap: MutableMap<Int, CourseModuleModel> = mutableMapOf<Int, CourseModuleModel>()
        for (act in activities) {
            println("Actibity: " + act.id_activity)
            val courseModule = act.fetchCourseModule(dbConnector)
            if (courseModule != null) {
                if (courseModulesInfoHashMap.containsKey(courseModule.id_course_module) == false) {
                    val moduleDoc = courseModule.fetchModule(dbConnector)
                    if (moduleDoc != null) {
                        timetable.addModule(courseModule.id_course_module!!, moduleDoc.name!!, courseModule.is_optional == 1)
                    } else {
                        throw Exception("COULD NOT FIND MODULE WITH ID: " + courseModule.id_module)
                    }
                } else {
                    courseModulesInfoHashMap[courseModule.id_course_module!!] = courseModule
                }

                val duration = (act.act_endtime!! - act.act_starttime!!)

                println("WEEK" + act.week)
                timetable.addActivity(
                    act.id_activity!!,
                    courseModule.available_year!!,
                    act.term!!,
                    act.week!!,
                    act.day_week!!,
                    courseModule.id_course_module!!, // There is separation between "courses"(id_course) and "module courses"(id_course_module). Need to decide which ID is better here
                    act.act_starttime!!,
                    duration,
                    act.id_act_category!!
                )
            }

        }

        return timetable;


    }

}