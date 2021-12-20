package Persistence.Entities.activity_category

import Persistence.annotations.Column
import Persistence.annotations.ColumnTypes
import Persistence.model.Model

class ActivityCategoryModel(
    @field:Column(type= ColumnTypes.INTEGER) var id_act_category: Int?,
    @field:Column(type= ColumnTypes.TEXT) var label: String?,

    ): Model("activity_categories") {

    }

