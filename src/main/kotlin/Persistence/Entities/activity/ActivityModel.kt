package Persistence.Entities.activity

import Persistence.DBConnection.DBConnector
import Persistence.Entities.course_module.CourseModuleModel
import Persistence.Entities.course_module.ResultSetToCourseModule
import Persistence.annotations.Column
import Persistence.annotations.ColumnTypes
import Persistence.model.Model

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

    ): Model("activities", "id_activity") {

        fun fetchCourseModule(dbConn: DBConnector) : CourseModuleModel?{
            val results = dbConn.rawSelectWithModel("SELECT * FROM ${CourseModuleModel().tableName}\n" +
                    "WHERE ${CourseModuleModel().primaryColumn} = ${this.id_course_module}",
                ResultSetToCourseModule()
            )

            if (results.size > 0) {
                return results[0];
            }
            return null;
        }

}