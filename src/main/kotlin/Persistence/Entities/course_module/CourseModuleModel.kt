package Persistence.Entities.course_module


import Persistence.annotations.Column
import Persistence.annotations.ColumnTypes
import Persistence.model.Model

class CourseModuleModel(
    @field:Column(type = ColumnTypes.INTEGER) var id_course_module: Int?,
    @field:Column(type = ColumnTypes.INTEGER) var id_course: Int?,
    @field:Column(type = ColumnTypes.INTEGER) var id_module: Int?,
    @field:Column(type = ColumnTypes.INTEGER) var is_optional: Int?,
    @field:Column(type = ColumnTypes.INTEGER) var available_year: Int?,

): Model("course_modules") {

}