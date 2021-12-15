package Persistence.DBConnection
import java.io.File
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

/**
 * Class that connects to a database file.
 */
class DBConnection(val dbFileLocation: String) {
    private var conn: Connection? = null

    fun isConnected() : Boolean {
        if (conn != null) {
            return !(conn!!.isClosed())
        }
        return false;
    }

    fun getConnection() : Connection? {
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
}