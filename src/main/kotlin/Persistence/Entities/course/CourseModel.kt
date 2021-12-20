package Persistence.Entities.course

import Persistence.DBConnection.DBConnector
import Persistence.Entities.course_type.CourseTypeModel
import Persistence.Entities.course_type.CourseTypeResultSetToModel
import Persistence.annotations.Column
import Persistence.annotations.ColumnTypes
import Persistence.model.Model


class CourseModel(
    @field:Column(type= ColumnTypes.INTEGER) var id_course: Int? = null,
    @field:Column(type= ColumnTypes.TEXT) var name: String? = null,
    @field:Column(type= ColumnTypes.INTEGER) var start_year: Int? = null,
    @field:Column(type= ColumnTypes.INTEGER) var end_year: Int? = null,
    @field:Column(type= ColumnTypes.INTEGER) var id_course_type: Int?= null,

): Model("courses", "id_course") {
    fun fetchCourseType(dbConn: DBConnector) : CourseTypeModel? {
        val courseTypes = dbConn.rawSelectWithModel("SELECT * FROM ${CourseTypeModel().tableName}\n" +
                "WHERE id_course_type = ${this.id_course_type}",
            CourseTypeResultSetToModel())

        if (courseTypes.size > 0) {
            return courseTypes[0];
        }
        return null;

    }
}