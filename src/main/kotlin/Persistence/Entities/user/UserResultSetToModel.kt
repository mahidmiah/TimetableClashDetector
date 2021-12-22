package Persistence.Entities.user

import Persistence.Entities.user.UserModel

import Persistence.ResultSetToModel
import java.sql.ResultSet

class UserResultSetToModel : ResultSetToModel<UserModel>() {
    override fun rsToModel(rs: ResultSet): UserModel {
        return UserModel(
            id_user = rs.getInt("id_user"),
            first_name = rs.getString("first_name"),
            last_name = rs.getString("first_name"),
            passw = rs.getString("passw"),
            email = rs.getString("email")
        )
    }
}