package Persistence.Entities.activity_category

import Persistence.ResultSetToModel
import java.sql.ResultSet

class ResultSetToActivityCategory: ResultSetToModel<ActivityCategoryModel>() {
    override fun rsToModel(rs: ResultSet): ActivityCategoryModel {
        return ActivityCategoryModel(
            id_act_category = rs.getInt("id_act_category"),
            label = rs.getString("label")

        )
    }
}