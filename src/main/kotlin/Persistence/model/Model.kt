package Persistence.model
import Persistence.DBConnection.DBConnector
import Persistence.ResultSetToModel
import Persistence.annotations.CEntity
import Persistence.annotations.Column
import Persistence.annotations.ColumnTypes
import java.lang.reflect.Field
import java.sql.*
import java.util.logging.Logger
import kotlin.reflect.KMutableProperty
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
 * You can denote the primary key by setting a value for `primaryColumn` OR
 * Annotating the class with `@CEntity(pkField='id_course')`
 *
 */
abstract class Model<TModel>(val tableName: String, val primaryColumn: String) {


    companion object {
        /**
         * Returns a list of attributes that are used as Columns to map their equivalent Database columns.
         * Example:
         * The attribute `idCourseType`<INT> might represent the column "id_course_type" in the database.
         * So you you would declare:
         *
         * `@field:Column(name="id_course_type", type=ColumnVars.INTEGER) public val id_course_type: Int?`
         */
        fun getListOfColumns(model: Model<Any>) : ArrayList<ColumnDetailsJavaField>{ // ArrayList<KProperty1<out Model, *>>
            val columnList: ArrayList<ColumnDetailsJavaField> = arrayListOf();

            // Iterate though the list of instance members(attributes, methods, e.t.c)
            val columnList2 = model::class.memberProperties
                .filter { m ->
                    m.javaField != null && m.javaField!!.getAnnotation(Column::class.java) != null
                }.map { m ->
                    ColumnDetailsJavaField(m.javaField!!)
                }
            /*
            IMPERATIVE WAY
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
            */

            return ArrayList(columnList2)
        }

        private fun assignPrimaryKeyValueViaInstanceMetadata(model: Model<Any>, newId: Int) {

            var truePrimaryColumn: String? = null
            val entityAnnot = model::class.java.getAnnotation<CEntity>(CEntity::class.java)
            if(entityAnnot != null) {
                truePrimaryColumn = entityAnnot.pkColumn


                //val field = this::class.java.getField(entityAnnot.PKField)
                //field.set(this, insertedId)
            } else if (model.primaryColumn != null) {
                truePrimaryColumn = model.primaryColumn
            }

            if (truePrimaryColumn != null) {
                val primaryField = model::class.memberProperties.find { e -> e.name == truePrimaryColumn}
                if (primaryField != null) {
                    // https://stackoverflow.com/questions/44304480/how-to-set-delegated-property-value-by-reflection-in-kotlin
                    val primaryJavaField =primaryField.javaField
                    if (primaryJavaField != null) {
                        primaryJavaField.setAccessible(true); // Needed to use the "get" method
                        if (primaryField is KMutableProperty<*>) {
                            primaryField.setter.call(model, newId)
                        }
                        primaryJavaField.setAccessible(false); // Needed to use the "get" method
                    }
                }
            }
        }

        /**
         * Content of the Insert Query
         * @param query Generated query
         * @param columns Fields that hold value
         */
        class InsertQueryPreparation(val query: String, val columns: ArrayList<ColumnDetailsJavaField>) {

        }

        /**
         * Result of the INSERT statement
         * @param affectedRows Number of affected rows after insert
         * @param generatedKeys Generated IDS
         */
        class InsertResult(val affectedRows: Int, val generatedKeys: ArrayList<Int>)


    }

    /**
     * Sets variable statements based on instance attributes' properties/type
     * @param pstmt Prepared Statement
     * @param statementIndex Variable Statement Index
     * @param field JavaField from the Reflection
     */
    private fun setStatementViaJavaField(pstmt: PreparedStatement, statementIndex: Int, field: Field){

        val columnDetails = ColumnDetails(field)

        val logger = Logger.getLogger("Model::setStatementViaJavaField")

        val columnType = columnDetails.type
        field.setAccessible(true); // Needed to use the "get", "set" method

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

            } else if (columnType == ColumnTypes.REAL) {
                if (value == null)  {
                    pstmt.setNull(statementIndex, Types.REAL)
                } else {
                    pstmt.setDouble(statementIndex, field.get(this) as Double)
                }
            }
        } catch (e : Exception) {
            logger.warning(e.stackTraceToString())
        }

        field.setAccessible(false);
    }

    /**
     * Generate INSERT SQL statement based on instance attributes properties.
     */
    private fun generateInsertQuery() : InsertQueryPreparation {
        val columns = getListOfColumns(this as Model<Any>);
        val questionMarks = columns.map { _ -> "?" }.joinToString(",")
        val columnOrder = columns.map{c -> c.name }.joinToString(",")

        return InsertQueryPreparation(query="INSERT INTO ${tableName} (${columnOrder})\n" +
                "VALUES(${questionMarks});", columns=columns) ;
    }

    /**
     * Method to obtain the DBConnector.
     */
    open fun getDbConnector(): DBConnector {
        throw Exception("Not implemented")
    }

    /**
     * Method to convert ResultSet to a Model
     */
    open fun createFromResultSet(rs: ResultSet) : TModel {
        throw Exception("Not implemented")
    }

    fun createArrayListFromResultSet(rs: ResultSet) : ArrayList<TModel>{
        val arrayList: ArrayList<TModel> = arrayListOf()
        while(rs.next()){
            arrayList.add(this.createFromResultSet(rs))
        }
        return arrayList

    }


    /**
     * Saves the content of the instance to the Database
     * If the `Model.primaryColumn` is provided, then the save method
     * will set a value to the according field/class attribute.
     */
    fun save() : InsertResult {
        return this.connSave(this.getDbConnector())
    }


    /**
     * Saves the content of the instance to the Database
     * If the `Model.primaryColumn` is provided, then the save method
     * will set a value to the according field/class attribute.
     */
    public fun connSave(dbConnector: DBConnector) : InsertResult{


        val insertQueryPrep = generateInsertQuery()
        val query = insertQueryPrep.query
        //println("QUERY INSERT" + query)
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


                Model.assignPrimaryKeyValueViaInstanceMetadata(this as Model<Any>, insertedId)

            }
            rs.close();


            return@startStatementEnvironment InsertResult(affectedRows, generatedKeysList)
        }


    }

    /**
     * Fetches the document via ID.
     * The column that stores the ID is defined by the `primaryColumn`
     */
    fun <PK> selectById(id: PK) : TModel? {
        val dbConnector = this.getDbConnector()
        val results = dbConnector.rawSelectResultSet("SELECT * FROM ${this.tableName} WHERE ${this.primaryColumn} = ${id.toString()}") { rs ->
            this.createArrayListFromResultSet(rs)
        }
        if (results.size > 0) {
            return results[0]
        }
        return null;
    }

    /**
     * Fetches all records from the table
     */
    fun selectAll() : ArrayList<TModel> {
        val dbConnector = this.getDbConnector()
        val results = dbConnector.rawSelectResultSet("SELECT * FROM ${this.tableName}", { rs ->
            this.createArrayListFromResultSet(rs)
        })
        return results
    }
    /*
    fun <T : TModel, PK> gFetchById(dbConn: DBConnector, id: PK, rsToModel: ResultSetToModel<T>) : T? {
        val results = dbConn.rawSelectWithModel<T>("SELECT * FROM ${tableName} WHERE ${primaryColumn} = ${id.toString()}", rsToModel)
        if (results.size > 0) {
            return results[0]
        }
        return null;

    }
    */



}