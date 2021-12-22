package Persistence.Entities.activity

import Persistence.DBConnection.DBConnector
import Persistence.Entities.course_module.CourseModuleModel
import Persistence.Entities.course_module.ResultSetToCourseModule
import Persistence.annotations.Column
import Persistence.annotations.ColumnTypes
import Persistence.model.Model
import Persistence.model.ModelSQLite
import java.sql.ResultSet

class ActivityModel(
    @field:Column(type = ColumnTypes.INTEGER) var id_activity: Int? = null,
    @field:Column(type = ColumnTypes.INTEGER) var id_act_category: Int? = null,
    @field:Column(type = ColumnTypes.INTEGER) var id_course_module: Int? = null,
    @field:Column(type = ColumnTypes.INTEGER) var posted_by: Int? = null,
    @field:Column(type = ColumnTypes.INTEGER) var day_week: Int? = null,
    @field:Column(type = ColumnTypes.REAL) var act_starttime: Double? = null,
    @field:Column(type = ColumnTypes.REAL) var act_endtime: Double? = null,
    @field:Column(type = ColumnTypes.INTEGER) var term: Int? = null,
    @field:Column(type = ColumnTypes.INTEGER) var week: Int? = null,

    ): ModelSQLite<ActivityModel>("activities", "id_activity") {

        fun fetchThisCourseModule() : CourseModuleModel?{
            return CourseModuleModel().selectById(this.id_course_module)
        }

    override fun createFromResultSet(rs: ResultSet): ActivityModel {
        return ResultSetToActivity().rsToModel(rs)
    }

}