package Persistence.DBConnection
import Persistence.ResultSetToModel
import Persistence.model.Model
import java.io.File
import java.sql.*

interface IResultSetToArrayList {

}
/**
 * Class that handles connections to the database.
 */
class DBConnector(val dbFileLocation: String) {
    private var conn: Connection? = null

    abstract class StatementExecutor {
        abstract fun execute(sqlConnection: Connection?)
    }

    fun isConnected() : Boolean {
        if (conn != null) {
            return !(conn!!.isClosed())
        }
        return false;
    }

    /**
     * Gets java.sql.Connection instance
     */
    fun getConnection() : Connection? {
        return this.conn;
    }

    /**
     * Creates an open connection
     */
    fun getOpenConnection() : Connection? {
        this.connect()
        return this.getConnection()
    }

    /**
     * Creates File for DB
     */
    fun createFile(){
        if(!File(this.dbFileLocation).exists()){
            File(this.dbFileLocation).createNewFile()
        }
    }

    /**
     * Deletes file and restores it.
     * It tries to reconnect if the connection was open.
     */
    fun resetFile(){
        var shouldReconnect = this.isConnected()

        if (this.isConnected() && this.conn != null) {
            this.conn!!.close()
        }
        if(File(this.dbFileLocation).exists()){
            if(File(this.dbFileLocation).delete()){
                this.createFile()
                if (shouldReconnect) {
                    this.connect()
                }
            } else {
                SecurityManager().checkDelete(this.dbFileLocation)
            }

        }



    }

    /**
     * Connects to the database. The connections keeps active after executing it.
     */
    fun connect(){
        if (this.isConnected()) {
            return
        }
        try {
            this.createFile()
            val dbUrl = "jdbc:sqlite:${this.dbFileLocation}"
            // create a connection to the database
            this.conn = DriverManager.getConnection(dbUrl)
            println("Connection to SQLite has been established.");

            // Create Database
            if (this.conn != null) {
                val meta = this.conn!!.metaData
                println("The driver name is " + meta.driverName)
                println("A new database has been created.")
            }

        } catch (e: SQLException) {
            println(e.message);
        }
    }

    /**
     * This method allows to make a safe connection while executing SQL Statements.
     * It makes sure the connection is properly closed after any statement
     * that is inside the `statementExecutor` function
     * @param statementExecutor Function that holds a `Connection` as argument.
     */
    fun <T> startStatementEnvironment(statementExecutor: (conn: Connection) -> T): T {
        val conn = this.getOpenConnection()
        if (conn != null) {

            try {
                val result = statementExecutor(conn)
                conn.close()
                return result
            } catch (e: Exception) {
                conn.close()
                throw e;
            }



        }
        throw Exception("Could not execute statement due to missing connection.")
    }

    /**
     * https://stackoverflow.com/questions/7507121/efficient-way-to-handle-resultset-in-java
     */
    fun rawSelect(query: String) : ArrayList<MutableMap<String, String>> {


        return this.startStatementEnvironment { conn ->
            val rowsList: ArrayList<MutableMap<String, String>> = arrayListOf();
            val stmt: Statement = conn!!.createStatement()
            val rs: ResultSet = stmt.executeQuery(query)
            val md = rs.metaData
            val columns: Int = md.getColumnCount()
            while (rs.next()) {
                val row: MutableMap<String, String> = mutableMapOf()
                for (i in 1..columns) {
                    //println("" + i + md.getColumnName(i) + rs.getObject(i).toString())
                    row[md.getColumnName(i)] = rs.getObject(i).toString()
                }
                rowsList.add(row);
            }
            rs.close()

            return@startStatementEnvironment rowsList;
        }


    }

    fun <T> rawSelectResultSet(query: String, callback: (rs: ResultSet) -> T) : T{
        return this.startStatementEnvironment { conn ->
            val stmt: Statement = conn!!.createStatement()
            val rs: ResultSet = stmt.executeQuery(query)
            val result = callback(rs)
            rs.close()
            return@startStatementEnvironment result;
        }
    }

    fun <TModel> rawSelectWithRsToModel(query: String, resultSetToModel: ResultSetToModel<TModel>): ArrayList<TModel> {
        return this.startStatementEnvironment { conn ->
            val stmt: Statement = conn!!.createStatement()
            val rs: ResultSet = stmt.executeQuery(query)
            val results = resultSetToModel.rsListToModel(rs)
            rs.close()
            return@startStatementEnvironment results;
        }
    }


    fun <TModel, T: Model<TModel>> v2rawSelectWithModel(query: String, model: T) : ArrayList<T>{

        return this.startStatementEnvironment { conn ->
            val stmt: Statement = conn.createStatement()
            val rs: ResultSet = stmt.executeQuery(query)
            return@startStatementEnvironment model.createArrayListFromResultSet(rs) as ArrayList<T>
        }
    }
}