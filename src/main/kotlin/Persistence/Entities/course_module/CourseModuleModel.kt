package Persistence.Entities.course_module


import Persistence.DBConnection.DBConnector
import Persistence.Entities.module.ModuleModel
import Persistence.Entities.module.ResultSetToModule
import Persistence.annotations.Column
import Persistence.annotations.ColumnTypes
import Persistence.model.Model

class CourseModuleModel(
    @field:Column(type = ColumnTypes.INTEGER) var id_course_module: Int? = null,
    @field:Column(type = ColumnTypes.INTEGER) var id_course: Int? = null,
    @field:Column(type = ColumnTypes.INTEGER) var id_module: Int? = null,
    @field:Column(type = ColumnTypes.INTEGER) var is_optional: Int? = null,
    @field:Column(type = ColumnTypes.INTEGER) var available_year: Int? = null,

): Model("course_modules", "id_course_module") {
    fun fetchModule(dbConnector: DBConnector) : ModuleModel?{
        return ModuleModel().gFetchById(dbConn = dbConnector, this.id_module, ResultSetToModule())
    }

}