package Persistence.model

/**
 * Result of the INSERT statement
 * @param affectedRows Number of affected rows after insert
 * @param generatedKeys Generated IDS
 */
class InsertResult(val affectedRows: Int, val generatedKeys: ArrayList<Int>)