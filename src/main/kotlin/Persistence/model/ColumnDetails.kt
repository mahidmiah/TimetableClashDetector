package Persistence.model

import Persistence.annotations.Column
import java.lang.reflect.Field


/**
 * Column Details
 */
open class ColumnDetails() {
    /**
     * Name of the column in the database
     */
    var name: String? = null;

    /**
     * Type of the column
     */
    var type: Int = -1;

    companion object {
        /**
         * Get the information of the `@Column` annotation from an attribute.
         * Only valid for those who have the annotation `@Column`
         */
        fun getColumnAnnotation(field: Field) : Column {
            val columnAnnotation = field.getAnnotation(Column::class.java)
            if (columnAnnotation != null) {
                return columnAnnotation
            }
            throw Exception("NOT A COLUMN")
        }

        fun getColumnType(field: Field) : Int {

            val columnDetails: Column = getColumnAnnotation(field)
            if (columnDetails != null) {
                return columnDetails.type
            }
            return -1;


        }

        fun getColumnNameFromField(field: Field) : String {

            val columnAnnotation = field.getAnnotation(Column::class.java)
            if (columnAnnotation != null && columnAnnotation.name != "") {
                return columnAnnotation.name
            }
            return field.name;
        }
    }
    constructor(name: String, type: Int) : this() {
        this.name = name
        this.type = type
    }




    constructor(attrJavaField: Field) : this() {
        this.name = getColumnNameFromField(attrJavaField)
        this.type = getColumnType(attrJavaField)

    }
}