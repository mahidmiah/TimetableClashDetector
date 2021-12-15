package Persistence.TableCreator

abstract class TableCreator(val tableName: String, val filePath: String) {
    abstract fun createTable()
}