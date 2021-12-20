package Persistence.Entities.activity_category

import Persistence.DBConnection.DBConnector
import Persistence.annotations.Column
import Persistence.annotations.ColumnTypes
import Persistence.model.Model

class ActivityCategoryModel(
    @field:Column(type= ColumnTypes.INTEGER) var id_act_category: Int? = null,
    @field:Column(type= ColumnTypes.TEXT) var label: String? = null,

    ): Model("activity_categories", "id_act_category") {

        companion object {
            const val tutorial = "tutorial"
            const val lecture = "lecture"
        }

        fun gFetchByLabel(dbConnector: DBConnector, qlabel: String) : ActivityCategoryModel?{
            val results = dbConnector.rawSelectWithModel("SELECT * FROM ${tableName} WHERE label = '${qlabel}'", ResultSetToActivityCategory())
            if (results.size > 0) {
                return results[0]
            }
            return null;

        }

    }

