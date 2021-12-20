package Persistence.Entities.course_type

import Persistence.Entities.course.CourseModel

import java.sql.ResultSet
import Persistence.ResultSetToModel

class CourseTypeResultSetToModel : ResultSetToModel<CourseTypeModel>() {
    override fun rsToModel(rs: ResultSet) : CourseTypeModel {
        return CourseTypeModel(
            id_course_type=rs.getInt("id_course_type"),
            label = rs.getString("label")
        )
    }
}