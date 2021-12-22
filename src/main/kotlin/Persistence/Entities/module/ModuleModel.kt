package Persistence.Entities.module

import Persistence.Entities.course_type.CourseTypeModel
import Persistence.Entities.course_type.CourseTypeResultSetToModel
import Persistence.annotations.Column
import Persistence.annotations.ColumnTypes
import Persistence.model.Model
import Persistence.model.ModelSQLite
import java.sql.ResultSet

class ModuleModel(
    @field:Column(type = ColumnTypes.INTEGER) var id_module: Int? = null,
    @field:Column(type = ColumnTypes.TEXT) var code: String? = null,
    @field:Column(type = ColumnTypes.TEXT) var name: String? = null,
): ModelSQLite<ModuleModel>("modules", "id_module") {
    override fun createFromResultSet(rs: ResultSet): ModuleModel {
        return ResultSetToModule().rsToModel(rs)
    }
}