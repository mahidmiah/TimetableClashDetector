package Persistence.seeds.compsci

import Persistence.DBConnection.DBConnector
import Persistence.Entities.course_type.CourseTypeModel
import Persistence.Entities.course_type.CourseTypeResultSetToModel
import Persistence.Entities.course.CourseModel
import Persistence.Entities.course_module.CourseModuleModel
import Persistence.Entities.module.ModuleModel
import Persistence.Entities.module.ResultSetToModule

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



    }

}