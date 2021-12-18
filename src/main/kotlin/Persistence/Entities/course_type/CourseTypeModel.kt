package Persistence.Entities.course_type;
import Persistence.Entities.Model
import Persistence.annotations.Column
import java.lang.Exception
import java.lang.reflect.Field
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaField

public class CourseTypeModel(
    @field:Column() val id_course_type: Int,
    @field:Column() val label: String,
    val test: String) : Model("course_types") {


    constructor(rs: ResultSet) : this(
        rs.getInt("id_course_type"),
        rs.getString("label"), "")

    fun save(conn: Connection){
        val insertQuery = "INSERT INTO warehouses(id_course_type,label) VALUES(?,?)";


        // Iterate through class instance properties/attributes
        for (member in this::class.memberProperties) {
            val javaField = member.javaField;
            if (javaField != null) {
                val FieldColumn = javaField.getAnnotation(Column::class.java)
                for (item in javaField.annotations) {
                    if (item is Column) {
                        val nameOfField = javaField.name // Name of the Field
                        val value = javaField.get(this) // Value of the Field of the instance
                        println(nameOfField + "=" + value)
                    }
                }
                if (FieldColumn != null) {
                    println("javaField WITH COLUMN:" + member);

                }
            }



        }
        /*
        try {
            val pstmt: PreparedStatement = conn.prepareStatement(insertQuery)
            pstmt.setInt(1, this.id_course_type);
            pstmt.setString(2, this.label);
            pstmt.executeUpdate();
        } catch (e: Exception) {
            println(e.message);
        }
        */
    }
}
