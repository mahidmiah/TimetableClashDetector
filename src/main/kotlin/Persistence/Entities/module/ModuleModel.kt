package Persistence.Entities.module

import Persistence.annotations.Column
import Persistence.annotations.ColumnTypes
import Persistence.model.Model

class ModuleModel(
    @field:Column(type = ColumnTypes.INTEGER) var id_module: Int?,
    @field:Column(type = ColumnTypes.TEXT) var code: String?,
    @field:Column(type = ColumnTypes.TEXT) var name: String?
): Model("modules") {

}