package Persistence.DBConnection
import Persistence.ResultSetToModel
import Persistence.model.Model
import java.io.File
import java.sql.*


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
     * NEED TO IMPLEMENT
     */
    fun rawSelect(query: String) : MutableMap<String, String>? {
        return this.startStatementEnvironment { conn ->
            val row: MutableMap<String, String> = mutableMapOf()
            var stmt: Statement = conn!!.createStatement()
            val rs: ResultSet = stmt.executeQuery(query)
            val md = rs.metaData
            val columns: Int = md.getColumnCount()
            while (rs.next()) {
                for (i in 1..columns) {
                    //println("" + i + md.getColumnName(i) + rs.getObject(i).toString())
                    row[md.getColumnName(i)] = rs.getObject(i).toString()
                }
            }

            return@startStatementEnvironment row;
        }


    }
    fun <T: Model> rawSelectWithModel(query: String, rsToModel: ResultSetToModel<T>) : ArrayList<T>{

        return this.startStatementEnvironment { conn ->
            val stmt: Statement = conn.createStatement()
            val rs: ResultSet = stmt.executeQuery(query)
            return@startStatementEnvironment rsToModel.rsListToModel(rs)
        }
    }
}