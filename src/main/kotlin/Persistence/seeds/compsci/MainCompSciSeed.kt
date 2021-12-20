package Persistence.seeds.compsci

import Persistence.DBConnection.DBConnector
import Persistence.Entities.activity.ActivityModel
import Persistence.Entities.activity.ResultSetToActivity
import Persistence.Entities.activity_category.ActivityCategoryModel
import Persistence.Entities.activity_category.ResultSetToActivityCategory
import Persistence.Entities.course_type.CourseTypeModel
import Persistence.Entities.course_type.CourseTypeResultSetToModel
import Persistence.Entities.course.CourseModel
import Persistence.Entities.course_module.CourseModuleModel
import Persistence.Entities.module.ModuleModel
import Persistence.Entities.module.ResultSetToModule
import Persistence.Entities.timetable.TimetableModel

import Persistence.model.SelectAll

class MainCompSciSeed() {

    fun seed(dbConn: DBConnector){
        val courseTypes = SelectAll(dbConn, CourseTypeModel(), CourseTypeResultSetToModel()).select()
        val undergraduateDoc = courseTypes.find { c -> c.label == CourseTypeModel.UNDERGRADUATE }
            ?: throw Exception("Could not find course type '${CourseTypeModel.UNDERGRADUATE}'")

        val courseCompSci = CourseModel(
            null,
            "BSc Computer Science",
            2019,
            2022,
            id_course_type = undergraduateDoc.id_course_type
        )
        println("BEFORE SAVE: " + courseCompSci.id_course)
        val insertedCourse = courseCompSci.save(dbConn)
        println("AFTER SAVE: " + courseCompSci.id_course)
        courseCompSci.id_course = insertedCourse.generatedKeys[0]
        SeedModules().seed(dbConn)

        val modules = SelectAll(dbConn, ModuleModel(), ResultSetToModule()).select()
        var jvmModule = modules.find { c -> c.code == SeedModules.jvmCode}
        var hciModule = modules.find { c -> c.code == SeedModules.hciCode}
        if (jvmModule == null || hciModule == null) {
            throw Exception("MODULES NOT FOUND: ${SeedModules.jvmCode},${SeedModules.hciCode}")
        }

        val jvmCourseModule = CourseModuleModel(
            id_course_module = null,
            id_course = courseCompSci.id_course,
            id_module= jvmModule.id_module,
            is_optional = 1,
            available_year = 1,
        )
        jvmCourseModule.save(dbConn)

        val hciCourseModule = CourseModuleModel(
            id_course_module = null,
            id_course = courseCompSci.id_course,
            id_module= hciModule.id_module,
            is_optional = 1,
            available_year = 1,
        )
        hciCourseModule.save(dbConn)

        val compSciModules: ArrayList<Int> = arrayListOf(jvmCourseModule.id_module!!, hciCourseModule.id_module!!)

        val timetable = TimetableModel(null, courseCompSci.id_course)
        timetable.save(dbConn)

        val activities = SelectAll(dbConn, ActivityModel(), ResultSetToActivity()).select()
        val activitiesIds = activities.filter { a -> compSciModules.contains(a.id_course_module) }.map { a -> a.id_activity}
        val activitiesIdsJoin = activitiesIds.joinToString(",")
        if (activitiesIds.size > 0) {
            dbConn.startStatementEnvironment { conn ->
                val deleteQuery = "" +
                        "DELETE FROM " + ActivityModel().tableName + "\n" +
                        "WHERE id_activity IN (${activitiesIdsJoin})";
                println("deleteQuery" + deleteQuery)
                val ptsmt = conn.prepareStatement(deleteQuery)

                val affectedRows = ptsmt.executeUpdate()
                ptsmt.close()

            }

        }



        val tutorialType = ActivityCategoryModel().gFetchByLabel(dbConn, ActivityCategoryModel.tutorial)
        val lectureType = ActivityCategoryModel().gFetchByLabel(dbConn, ActivityCategoryModel.lecture)

        if (tutorialType == null || lectureType == null) {
            throw Exception("Activity Categories not found")
        }
        val newActivities: ArrayList<ActivityModel> = arrayListOf()

        newActivities.add(
            ActivityModel(
            id_activity = null,
                id_act_category = tutorialType.id_act_category,
                id_course_module = jvmCourseModule.id_course_module,
                posted_by = 1,
                term = 1,
                week=1,
                day_week = 0,
                act_starttime = 9.0,
                act_endtime = 11.0
            )
        )
        newActivities.add(
            ActivityModel(
                id_activity = null,
                id_act_category = tutorialType.id_act_category,

                id_course_module = jvmCourseModule.id_course_module,
                posted_by = 1,
                term = 1,
                week=1,
                day_week = 1,
                act_starttime = 9.0,
                act_endtime = 11.0
            )
        )
        newActivities.add(
            ActivityModel(
                id_activity = null,
                id_act_category = tutorialType.id_act_category,
                id_course_module = hciCourseModule.id_course_module,
                posted_by = 1,
                week=1,
                term = 1,
                day_week = 0,
                act_starttime = 9.0,
                act_endtime = 11.0
            )
        )
        newActivities.add(
            ActivityModel(
                id_activity = null,
                id_act_category = tutorialType.id_act_category,
                id_course_module = jvmCourseModule.id_course_module,
                posted_by = 1,
                term = 1,
                week=1,
                day_week = 2,
                act_starttime = 9.0,
                act_endtime = 10.0
            )
        )


        newActivities.add(
            ActivityModel(
                id_activity = null,
                id_act_category = tutorialType.id_act_category,
                id_course_module = jvmCourseModule.id_course_module,
                posted_by = 1,
                term = 1,
                week=1,
                day_week = 2,
                act_starttime = 9.0,
                act_endtime = 10.0
            )
        )


        for (act in newActivities) {
            act.save(dbConn)
        }



    }

}