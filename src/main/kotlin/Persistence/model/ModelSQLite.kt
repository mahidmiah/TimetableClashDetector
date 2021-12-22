package Persistence.model

import Persistence.DBConnection.DBConnector
import Persistence.DBConnection.SingletonDBConnector
import java.sql.ResultSet

open class ModelSQLite<TModel>(tableName: String, primaryColumn: String) : Model<TModel>(tableName, primaryColumn) {
    override fun getDbConnector(): DBConnector {
        return SingletonDBConnector.getConnector()
    }
}