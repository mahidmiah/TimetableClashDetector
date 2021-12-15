package Persistence.TableCreator

interface ITableCreator {
    var tableName: String
    fun createTable()
}