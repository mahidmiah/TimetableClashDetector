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
            const val tutorial = "Tutorial"
            const val lecture = "Lecture"
            const val lab = "Lab"
            const val exam = "Exam"

            fun getStaticLabels() : List<String> {
                return listOf<String>(
                    ActivityCategoryModel.tutorial,
                    ActivityCategoryModel.lecture,
                    ActivityCategoryModel.lab,
                    ActivityCategoryModel.exam,
                )
            }
        }


        /**
         * Fetches Activity Categories by the specified label
         */
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
        /**
         * Converts a list of ActivityCategory models to a list of just their labels
         * Example:
         * `labelsToArray(actCatModels) -> List<String> = {"Exam", "Tutorial"}`
         */
        fun labelsToArray(actCatModels: ArrayList<ActivityCategoryModel>): List<String?> {
            return actCatModels.map { a -> a.label }
        }

        override fun createFromResultSet(rs: ResultSet): ActivityCategoryModel {
            return ResultSetToActivityCategory().rsToModel(rs)
        }

    }

