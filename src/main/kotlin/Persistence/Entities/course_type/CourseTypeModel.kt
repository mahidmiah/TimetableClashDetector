package Persistence.Entities.course_type;
import Persistence.Entities.Model
import Persistence.annotations.Column
import Persistence.annotations.ColumnVars
import java.sql.Connection
import java.sql.ResultSet
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaField

public class CourseTypeModel(
    @field:Column(type=ColumnVars.INT) public val id_course_type: Int?,
    @field:Column(type=ColumnVars.TEXT) public val label: String,
    val test: String) : Model("course_types") {


    constructor(rs: ResultSet) : this(
        rs.getInt("id_course_type"),
        rs.getString("label"), "")

    fun saveAlt(conn: Connection){
        val insertQuery = "INSERT INTO warehouses(id_course_type,label) VALUES(?,?)";



        /*
        Iterate through class instance properties/attributes
        Posts/Articles about it:
        https://stackoverflow.com/questions/46512924/kotlin-get-field-annotation-always-empty
        https://stackoverflow.com/questions/51218309/finding-field-annotations-using-objclass-declaredmemberproperties?noredirect=1&lq=1
        https://stackoverflow.com/questions/35525122/kotlin-data-class-how-to-read-the-value-of-property-if-i-dont-know-its-name-at
        http://tutorials.jenkov.com/java-reflection/annotations.html
        http://tutorials.jenkov.com/java/annotations.html
        https://stackoverflow.com/questions/1805200/retrieve-java-annotation-attribute

        */
        for (member in this::class.memberProperties) {
            val javaField = member.javaField;
            if (javaField != null) {
                val FieldColumn = javaField.getAnnotation(Column::class.java)
                for (item in javaField.annotations) {
                    if (item is Column) {
                        val nameOfField = javaField.name // Name of the Field
                        val value = javaField.get(this) // Value of the Field of the instance
                        val columnType = item.type
                        val variableType = javaField.type
                        println("variableType: " + variableType)
                        println("${nameOfField}<${columnType}>=${value})");
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
