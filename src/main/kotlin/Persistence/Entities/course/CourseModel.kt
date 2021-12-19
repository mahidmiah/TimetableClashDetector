package Persistence.Entities.course

import Persistence.annotations.Column
import Persistence.annotations.ColumnTypes
import Persistence.model.Model

class CourseModel(
    @field:Column(type= ColumnTypes.INTEGER) var id_course: Int?,
    @field:Column(type= ColumnTypes.TEXT) var name: String?,
    @field:Column(type= ColumnTypes.INTEGER) var start_year: String?,
    @field:Column(type= ColumnTypes.INTEGER) var end_year: String?,
    @field:Column(type= ColumnTypes.INTEGER) var id_course_type: Int?,

): Model("courses") {


}