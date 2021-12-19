package Persistence.model
import Persistence.DBConnection.DBConnector
import Persistence.annotations.Column
import Persistence.annotations.ColumnVars
import java.lang.reflect.Field
import java.sql.*
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaField


open class Model(val tableName: String) {
    companion object {


        fun getColumnNameFromField(field: Field) : String {

            val columnAnnotation = field.getAnnotation(Column::class.java)
            if (columnAnnotation != null && columnAnnotation.name != "") {
                return columnAnnotation.name
            }
            return field.name;
        }

        fun getColumnDetails(field: Field) : Column {
            val columnAnnotation = field.getAnnotation(Column::class.java)
            if (columnAnnotation != null) {
                return columnAnnotation
            }
            throw Exception("NOT A COLUMN")
        }

        fun getColumnType(field: Field) : Int {

            val columnDetails: Column = getColumnDetails(field)
            return columnDetails.type

        }
        fun getListOfColumns(model: Model) : ArrayList<KProperty1<out Model, *>>{
            val columnList: ArrayList<KProperty1<out Model, *>> = arrayListOf();
            for (member in model::class.memberProperties) {
                val javaField = member.javaField;
                if (javaField != null) {
                    val FieldColumn = javaField.getAnnotation(Column::class.java)
                    for (item in javaField.annotations) {
                        if (item is Column) {
                            columnList.add(member)
                        }
                    }
                }



            }
            return columnList
        }
        class InsertQueryPreparation(val query: String, val columns: ArrayList<KProperty1<out Model, *>>) {

        }

        class InsertResult(val affectedRows: Int, generatedKeys: ArrayList<Int>)


    }

    private fun setStatementViaJavaField(pstmt: PreparedStatement, statementIndex: Int, field: Field){
        val columnType = getColumnType(field)
        field.setAccessible(true); // Needed to use the "get" method

        try {
            val value = field.get(this)
            if (columnType == ColumnVars.TEXT) {
                if (value == null)  {
                    pstmt.setNull(statementIndex, Types.VARCHAR)
                } else {
                    pstmt.setString(statementIndex, field.get(this) as String?)
                }

            } else if (columnType == ColumnVars.INTEGER) {
                if (value == null)  {
                    pstmt.setNull(statementIndex, Types.INTEGER)
                } else {
                    pstmt.setInt(statementIndex, field.get(this) as Int)
                }

            }
        } catch (e : Exception) {
            println(e.message)
        }

        field.setAccessible(false);
    }
    private fun generateInsertQuery() : InsertQueryPreparation {
        var columns = getListOfColumns(this);
        var questionMarks = columns.map { _ -> "?" }.joinToString(",")
        val columnOrder = columns.map{f -> getColumnNameFromField(f.javaField!!) }.joinToString(",")

        return InsertQueryPreparation(query="INSERT INTO ${tableName} (${columnOrder})\n" +
                "VALUES(${questionMarks});", columns=columns) ;
    }

    public fun save(dbConnector: DBConnector) : InsertResult{
        val insertQueryPrep = generateInsertQuery()
        val query = insertQueryPrep.query
        println("QUERY INSERT" + query)
        return dbConnector.startStatementEnvironment<InsertResult> { conn ->
            val pstmt: PreparedStatement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
            var i = 0;
            for (col in insertQueryPrep.columns) {
                println(i)
                i += 1;
                val field = col.javaField
                if (field != null) {
                    this.setStatementViaJavaField(pstmt, i, field)
                }

            }

            val affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw SQLException("Creating '${tableName}' record failed, no rows affected.");
            }
            val generatedKeysList: ArrayList<Int> = arrayListOf();

            val rs: ResultSet = pstmt.getGeneratedKeys()
            if (rs.next()) {
                val insertedId = rs.getInt(1)
                generatedKeysList.add(insertedId)
            }
            rs.close();


            return@startStatementEnvironment InsertResult(affectedRows, generatedKeysList)
        }


    }
}