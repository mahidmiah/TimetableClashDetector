package Persistence.Entities.course

import java.sql.ResultSet
import Persistence.ResultSetToModel

class CourseResultSetToModel : ResultSetToModel<CourseModel>() {
    override fun rsToModel(rs: ResultSet) : CourseModel {
        return CourseModel(
            id_course = rs.getInt("id_course"),
            id_course_type=rs.getInt("id_course_type"),
            start_year = rs.getString("start_year"),
            end_year = rs.getString("end_year"),
            name = rs.getString("name")
        )
    }
}