package Persistence.Entities.course

import Persistence.DBConnection.DBConnector
import Persistence.annotations.Column
import Persistence.annotations.ColumnTypes
import Persistence.model.Model
import Persistence.model.SelectAll
import java.sql.ResultSet
import java.sql.Statement

class CourseModel(
    @field:Column(type= ColumnTypes.INTEGER) var id_course: Int? = null,
    @field:Column(type= ColumnTypes.TEXT) var name: String? = null,
    @field:Column(type= ColumnTypes.INTEGER) var start_year: String? = null,
    @field:Column(type= ColumnTypes.INTEGER) var end_year: String? = null,
    @field:Column(type= ColumnTypes.INTEGER) var id_course_type: Int?= null,

): Model("courses") {
}