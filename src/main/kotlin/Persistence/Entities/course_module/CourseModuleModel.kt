package Persistence.Entities.course_module


import Persistence.DBConnection.DBConnector
import Persistence.Entities.course.CourseModel
import Persistence.Entities.course.ResultSetToCourse
import Persistence.Entities.module.ModuleModel
import Persistence.Entities.module.ResultSetToModule
import Persistence.annotations.Column
import Persistence.annotations.ColumnTypes
import Persistence.model.Model
import Persistence.model.ModelSQLite
import java.sql.ResultSet

class CourseModuleModel(
    @field:Column(type = ColumnTypes.INTEGER) var id_course_module: Int? = null,
    @field:Column(type = ColumnTypes.INTEGER) var id_course: Int? = null,
    @field:Column(type = ColumnTypes.INTEGER) var id_module: Int? = null,
    @field:Column(type = ColumnTypes.INTEGER) var is_optional: Int? = null,
    @field:Column(type = ColumnTypes.INTEGER) var available_year: Int? = null,

): ModelSQLite<CourseModuleModel>("course_modules", "id_course_module") {

    override fun createFromResultSet(rs: ResultSet): CourseModuleModel {
        return ResultSetToCourseModule().rsToModel(rs)
    }

    fun fetchThisModule() : ModuleModel?{
        return ModuleModel().selectById(this.id_module)
    }

}