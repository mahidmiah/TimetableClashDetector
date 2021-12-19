package Persistence.model
import Persistence.DBConnection.DBConnector
import Persistence.annotations.Column
import Persistence.annotations.ColumnTypes
import java.lang.reflect.Field
import java.sql.*
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaField

/**
 Resources about Reflections:
 Iterate through class instance properties/attributes
    Posts/Articles about it:
    https://stackoverflow.com/questions/46512924/kotlin-get-field-annotation-always-empty
    https://stackoverflow.com/questions/51218309/finding-field-annotations-using-objclass-declaredmemberproperties?noredirect=1&lq=1
    https://stackoverflow.com/questions/35525122/kotlin-data-class-how-to-read-the-value-of-property-if-i-dont-know-its-name-at
    http://tutorials.jenkov.com/java-reflection/annotations.html
    http://tutorials.jenkov.com/java/annotations.html
    https://stackoverflow.com/questions/1805200/retrieve-java-annotation-attribute
 */

/**
 * Class Model to identify an entity/table in the Database.
 *
 * It uses some techinques of reflection to map the information of the database
 * with Kotlin objects.
 * You can assign attributes to Database columns by adding annotations
 *
 *
 * `@field:Column(name="id_course_type", type=ColumnTypes.INTEGER) public val id_course_type: Int?`
 *
 */
open class Model(val tableName: String) {
    companion object {

        /**
         * Returns a list of attributes that are used as Columns to map their equivalent Database columns.
         * Example:
         * The attribute `idCourseType`<INT> might represent the column "id_course_type" in the database.
         * So you you would declare:
         *
         * `@field:Column(name="id_course_type", type=ColumnVars.INTEGER) public val id_course_type: Int?`
         */
        fun getListOfColumns(model: Model) : ArrayList<ColumnDetailsJavaField>{ // ArrayList<KProperty1<out Model, *>>
            val columnList: ArrayList<ColumnDetailsJavaField> = arrayListOf();
            for (member in model::class.memberProperties) {
                val javaField = member.javaField;
                if (javaField != null) {
                    val FieldColumn = javaField.getAnnotation(Column::class.java)
                    for (item in javaField.annotations) {


                        if (item is Column) {
                            columnList.add(ColumnDetailsJavaField(member.javaField!!))
                        }
                    }
                }



            }
            return columnList
        }
        class InsertQueryPreparation(val query: String, val columns: ArrayList<ColumnDetailsJavaField>) {

        }

        class InsertResult(val affectedRows: Int, val generatedKeys: ArrayList<Int>)


    }

    private fun setStatementViaJavaField(pstmt: PreparedStatement, statementIndex: Int, field: Field){

        val columnDetails = ColumnDetails(field)

        val columnType = columnDetails.type
        field.setAccessible(true); // Needed to use the "get" method

        try {
            val value = field.get(this)
            if (columnType == ColumnTypes.TEXT) {
                if (value == null)  {
                    pstmt.setNull(statementIndex, Types.VARCHAR)
                } else {
                    pstmt.setString(statementIndex, field.get(this) as String?)
                }

            } else if (columnType == ColumnTypes.INTEGER) {
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
        val columnOrder = columns.map{f -> f.name }.joinToString(",")

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
                val field = col.attrJavaField
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