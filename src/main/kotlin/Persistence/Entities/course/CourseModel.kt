package Persistence.Entities.course

import Persistence.DBConnection.DBConnector
import Persistence.Entities.activity_category.ActivityCategoryModel
import Persistence.Entities.activity_category.ResultSetToActivityCategory
import Persistence.Entities.course_type.CourseTypeModel
import Persistence.Entities.course_type.CourseTypeResultSetToModel
import Persistence.annotations.Column
import Persistence.annotations.ColumnTypes
import Persistence.model.Model
import Persistence.model.ModelSQLite
import java.sql.ResultSet


class CourseModel(
    @field:Column(type= ColumnTypes.INTEGER) var id_course: Int? = null,
    @field:Column(type= ColumnTypes.TEXT) var name: String? = null,
    @field:Column(type= ColumnTypes.INTEGER) var start_year: Int? = null,
    @field:Column(type= ColumnTypes.INTEGER) var end_year: Int? = null,
    @field:Column(type= ColumnTypes.INTEGER) var id_course_type: Int?= null,

): ModelSQLite<CourseModel>("courses", "id_course") {

    override fun createFromResultSet(rs: ResultSet): CourseModel {
        return ResultSetToCourse().rsToModel(rs)
    }
    fun fetchThisCourseType() : CourseTypeModel? {
        val dbConnector = this.getDbConnector()
        val courseTypes = dbConnector.rawSelectResultSet("SELECT * FROM ${CourseTypeModel().tableName}\n" +
                "WHERE id_course_type = ${this.id_course_type}",
            {rs -> CourseTypeModel().createArrayListFromResultSet(rs)})

        if (courseTypes.size > 0) {
            return courseTypes[0];
        }
        return null;

    }
}