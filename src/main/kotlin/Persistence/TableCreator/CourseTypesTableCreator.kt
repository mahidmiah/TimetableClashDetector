package Persistence.TableCreator
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

class CourseTypesTableCreator : TableCreator {
    constructor(filePath: String):
            super("course_types", filePath)

    override fun createTable() {

    }
}