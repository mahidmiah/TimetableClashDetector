package Persistence.Entities
import Persistence.annotations.Column
import Persistence.annotations.ColumnVars
import java.lang.reflect.Field
import java.sql.Connection
import java.sql.PreparedStatement
import kotlin.Exception
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

        fun getColumnType(field: Field) : String {

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


    }

    private fun setStatementViaJavaField(pstmt: PreparedStatement, statementIndex: Int, field: Field){
        val columnType = getColumnType(field)
        field.setAccessible(true);


        
        if (columnType == ColumnVars.TEXT) {
            pstmt.setString(statementIndex, field.get(this) as String?)
        } else if (columnType == ColumnVars.INT) {
            pstmt.setInt(statementIndex, field.get(this) as Int)
        }
        field.setAccessible(false);
    }
    private fun generateInsertQuery() : InsertQueryPreparation {
        var columns = Model.getListOfColumns(this);
        var questionMarks = columns.map { _ -> "?" }.joinToString(",")
        val columnOrder = columns.map{f -> getColumnNameFromField(f.javaField!!)}.joinToString(",")

        return InsertQueryPreparation(query="INSERT INTO ${tableName} (${columnOrder})\n" +
                "VALUES(${questionMarks});", columns=columns) ;
    }

    public fun save(conn: Connection){
        val insertQueryPrep = generateInsertQuery()
        val query = insertQueryPrep.query
        println("QUERY INSERT" + query)
        try {
            val pstmt : PreparedStatement = conn.prepareStatement(query)
            var i = 0;
            for (col in insertQueryPrep.columns) {
                println(i)
                i += 1;
                val field = col.javaField
                if (field != null) {
                    this.setStatementViaJavaField(pstmt, i, field)
                }

            }

            pstmt.executeUpdate();

        } catch (e: Exception) {
            System.out.println(e.message);
        }

    }
}