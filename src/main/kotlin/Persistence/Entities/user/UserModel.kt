package Persistence.Entities.user

import Persistence.Entities.timetable.ResultSetToTimetable
import Persistence.Entities.timetable.TimetableModel
import Persistence.annotations.Column
import Persistence.annotations.ColumnTypes
import Persistence.model.Model
import Persistence.model.ModelSQLite
import java.sql.ResultSet

class UserModel(
    @field:Column(type = ColumnTypes.INTEGER) var id_user: Int? = null,
    @field:Column(type = ColumnTypes.TEXT) var email: String? = null,
    @field:Column(type = ColumnTypes.TEXT) var first_name: String? = null,
    @field:Column(type = ColumnTypes.TEXT) var last_name: String? = null,
    @field:Column(type = ColumnTypes.TEXT) var passw: String? = null,

): ModelSQLite<UserModel>("users", "id_user") {
    override fun createFromResultSet(rs: ResultSet): UserModel {
        return UserResultSetToModel().rsToModel(rs)
    }
}