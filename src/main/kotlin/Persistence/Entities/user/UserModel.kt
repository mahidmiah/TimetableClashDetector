package Persistence.Entities.user

import Persistence.annotations.Column
import Persistence.annotations.ColumnTypes
import Persistence.model.Model

class ModuleModel(
    @field:Column(type = ColumnTypes.INTEGER) var id_user: Int?,
    @field:Column(type = ColumnTypes.TEXT) var email: String?,
    @field:Column(type = ColumnTypes.TEXT) var first_name: String?,
    @field:Column(type = ColumnTypes.TEXT) var last_name: String?,
    @field:Column(type = ColumnTypes.TEXT) var passw: String?,

): Model("users") {

}