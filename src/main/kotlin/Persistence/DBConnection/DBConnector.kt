package Persistence.DBConnection
import java.io.File
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

/**
 * Class that connects to a database file.
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

    fun getConnection() : Connection? {
        this.connect()
        return this.conn;
    }

    fun createFile(){
        if(!File(this.dbFileLocation).exists()){
            File(this.dbFileLocation).createNewFile()
        }
    }

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

    fun <T> startStatementEnvironment(statementExecutor: (conn: Connection) -> T): T {
        val conn = this.getConnection()
        if (conn != null) {
            val result = statementExecutor(conn)
            conn.close()
            return result
        }
        throw Exception("Could not execute statement due to missing connection.")
    }
}