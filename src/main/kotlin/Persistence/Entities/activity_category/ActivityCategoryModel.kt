package Persistence.Entities.activity_category

import Persistence.DBConnection.DBConnector
import Persistence.Entities.activity.ActivityModel
import Persistence.Entities.activity.ResultSetToActivity
import Persistence.annotations.Column
import Persistence.annotations.ColumnTypes
import Persistence.model.Model
import Persistence.model.ModelSQLite
import java.sql.ResultSet

class ActivityCategoryModel(
    @field:Column(type= ColumnTypes.INTEGER) var id_act_category: Int? = null,
    @field:Column(type= ColumnTypes.TEXT) var label: String? = null,

    ): ModelSQLite<ActivityCategoryModel>("activity_categories", "id_act_category") {

        companion object {
            const val tutorial = "tutorial"
            const val lecture = "lecture"
        }

        fun fetchByLabel(qlabel: String) : ActivityCategoryModel?{
            val dbConnector = this.getDbConnector()
            val results = dbConnector.rawSelectResultSet("SELECT * FROM ${tableName} WHERE label = '${qlabel}'") { rs ->
                this.createArrayListFromResultSet(rs)
            }
            if (results.size > 0) {
                return results[0]
            }
            return null;

        }

        override fun createFromResultSet(rs: ResultSet): ActivityCategoryModel {
            return ResultSetToActivityCategory().rsToModel(rs)
        }

    }

