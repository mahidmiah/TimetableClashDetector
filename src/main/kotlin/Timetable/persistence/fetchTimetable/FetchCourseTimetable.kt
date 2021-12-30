package Timetable.persistence.fetchTimetable

import Persistence.DBConnection.DBConnector
import Persistence.Entities.activity.ActivityModel
import Persistence.Entities.activity.ResultSetToActivity
import Persistence.Entities.course.CourseModel
import Persistence.Entities.course_module.CourseModuleModel
import Persistence.Entities.course_type.CourseTypeModel
import Persistence.Entities.timetable.TimetableModel
import Timetable.Timetable
import java.util.logging.Logger

class FetchCourseTimetable(val dbConnector: DBConnector) {

    fun generateTimetableFromTimetableModel(timetableModel: TimetableModel) : Timetable?{
        val logger = Logger.getLogger("FechCourseTimetable.generateTimetableFromTimetableModel");
        val targetCourse = timetableModel.fetchThisCourse();
        if (targetCourse == null) {
            logger.warning("Could not courseModel with id: ${timetableModel.id_course}");
            return null;
        }
        val activitiesQuery ="SELECT act.* FROM ${ActivityModel().tableName} as act\n" +
                "INNER JOIN ${CourseModuleModel().tableName} as cm\n" +
                "   ON cm.id_course_module = act.id_course_module\n" +
                "WHERE cm.id_course = ${targetCourse.id_course}";

        println("ACT QUERY\n" + activitiesQuery)
        val activities = dbConnector.rawSelectWithRsToModel(activitiesQuery,
            ResultSetToActivity()
        )

        println("ACTS" + activities)

        val courseType = targetCourse.fetchThisCourseType()
        var courseTypeBoolean = true;
        if (courseType != null) {
            courseTypeBoolean = (courseType.label == CourseTypeModel.UNDERGRADUATE)
        }


        val timetable = Timetable(timetableModel.id_timetable, targetCourse.name!!, targetCourse.start_year!!, targetCourse.end_year!!, courseTypeBoolean)

        val courseModules = CourseModuleModel().selectAll().filter { e -> e.id_course == targetCourse.id_course }

        for (courseModule in courseModules) {
            val moduleDoc = courseModule.fetchThisModule()
            if (moduleDoc != null) {
                timetable.addModule(courseModule.id_course_module!!, moduleDoc.name!!, courseModule.is_optional == 1)
            } else {
                throw Exception("COULD NOT FIND MODULE WITH ID: " + courseModule.id_module)
            }

        }
        //val modulesInfo: ArrayList<ModuleModel> = arrayListOf()
        //val modulesInfoHashMap: MutableMap<Int, ModuleModel> = mutableMapOf<Int, ModuleModel>()
        val courseModulesInfoHashMap: MutableMap<Int, CourseModuleModel> = mutableMapOf<Int, CourseModuleModel>()

        for (act in activities) {
            println("Actibity: " + act.id_activity)
            val courseModule = act.fetchThisCourseModule()
            if (courseModule != null) {

                val duration = (act.act_endtime!! - act.act_starttime!!)

                println("WEEK" + act.week)
                logger.info("ACTIVITY TYPE FOUND: " + act.id_act_category)
                timetable.addActivity(
                    act.id_activity!!,
                    act.year!!,
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
    fun fetchWithYear(courseName: String, startYear: Int) :  Timetable? {
        val logger = Logger.getLogger("fetchCourseTimetable")
        val courses = dbConnector.rawSelectResultSet(
            "SELECT * FROM ${CourseModel().tableName} WHERE name = '${courseName}' AND start_year = ${startYear};",
            {rs -> CourseModel().createArrayListFromResultSet(rs)}
        )

        if (courses.size == 0) {
            logger.info("DID NOT FIND COURSES WITH name=${courseName} and start_year = ${startYear}")
            return null
        }

        val targetCourse = courses[0]

        val timetableDocs = dbConnector.rawSelectResultSet("" +
                "SELECT * FROM ${TimetableModel().tableName}\n" +
                "WHERE id_course = ${targetCourse.id_course}",
            {rs -> TimetableModel().createArrayListFromResultSet(rs)})

        if (timetableDocs.size == 0) {
            logger.info("DID NOT FIND TIMETABLES WITH id_course=${targetCourse.id_course}")
            return null;
        }

        val targetTimetable = timetableDocs[0]

        return generateTimetableFromTimetableModel(targetTimetable);


    }

    fun fetchByTimetableId(timetableId: Int) : Timetable?{
        val logger = Logger.getLogger("FechCourseTimetable.fetchByTimetableId");
        val timetableModel = TimetableModel().selectById(timetableId);
        if (timetableModel == null) {
            logger.warning("Could not timtableModel with id: ${timetableId}");
            return null;
        }

        return generateTimetableFromTimetableModel(timetableModel);



    }

}