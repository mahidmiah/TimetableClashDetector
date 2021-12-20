package Persistence.Entities.course_module

import Persistence.Entities.course_module.CourseModuleModel
import java.sql.ResultSet
import Persistence.ResultSetToModel

class ResultSetToCourseModule: ResultSetToModel<CourseModuleModel>() {
    override fun rsToModel(rs: ResultSet): CourseModuleModel {
        return CourseModuleModel(
            id_course_module = rs.getInt("id_course_module"),
            id_course = rs.getInt("id_course"),
            id_module= rs.getInt("id_module"),
            is_optional = rs.getInt("is_optional"),
            available_year = rs.getInt("available_year")
        )
    }

}