package Persistence.DBConnection
import java.io.File
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

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
                return result
            } catch (e: Exception) {
                throw e;
            } finally {
                conn.close()
            }



        }
        throw Exception("Could not execute statement due to missing connection.")
    }
}