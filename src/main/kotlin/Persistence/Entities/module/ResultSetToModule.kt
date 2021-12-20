package Persistence.Entities.module

import Persistence.ResultSetToModel
import java.sql.ResultSet

class ResultSetToModule : ResultSetToModel<ModuleModel>() {
    override fun rsToModel(rs: ResultSet): ModuleModel {
        return ModuleModel(
            id_module = rs.getInt("id_module"),
            code = rs.getString("code"),
            name = rs.getString("name")
        )
    }
}