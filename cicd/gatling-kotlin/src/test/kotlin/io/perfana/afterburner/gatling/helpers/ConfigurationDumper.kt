package io.perfana.afterburner.gatling.helpers

import java.util.Properties
import io.perfana.afterburner.gatling.configuration.*

object ConfigurationDumper {

    val values: String by lazy {
        var values = "\n" + "=".repeat(100) + "\nSystem Properties values:\n\n"
        val systemProperties: Properties = System.getProperties()
        systemProperties.forEach { key, value -> values += "System.getProperty(\"$key\") = $value\n" }
        values += "=".repeat(100) + "\nTestConfiguration values:\n\n"

        Configuration::class.java.declaredFields.forEach { field ->
            field.isAccessible = true
            values += "Configuration.${field.name} = ${field.get(Configuration)}\n"
        }

        println(values) // I print this now, so it will be printed during creation of object instances etc, before running
        values
    }

}
