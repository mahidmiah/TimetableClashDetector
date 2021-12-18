package Persistence

import Persistence.DBConnection.DBConnection
import java.io.File
import java.nio.file.Files
import java.sql.ResultSet
import java.sql.Statement

/**
 * Class to create database tables/schema
 */
class DBCreator(val dbConnection: DBConnection) {
    fun buildDatabase(){
        var dbCreationQuery = ""
        val cl: ClassLoader = ClassLoader.getSystemClassLoader()
        val sqlCommandsFile = cl.getResource("./greenwich_timetables.sq")
        if (sqlCommandsFile != null) {
            println("File of SQL Commands to create Database: " + sqlCommandsFile.path)

            val sqlCommandsContent = Files.readAllLines(File(sqlCommandsFile.path).toPath(), Charsets.UTF_8)
            dbCreationQuery = sqlCommandsContent.joinToString("\n")
        } else {
            throw Exception("Could not access resource 'greenwich_timetables.sq'")
        }



        val conn = this.dbConnection.getConnection()


        if (conn != null) {
            var stmt: Statement = conn.createStatement()

            // Query to check if the table "users" already exists
            val res: ResultSet = stmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='users'")
            var count: Int = 0;
            while(res.next()){
                count += 1;

                break
            }
            // If it does not exist, then create the tables
            if (count == 0) {
                stmt = conn.createStatement()
                val updateRes = stmt.executeUpdate(dbCreationQuery)
                println(updateRes)
            }

        }
    }
}