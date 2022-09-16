package io.perfana.afterburner.gatling.helpers

import java.util.Properties

import io.perfana.afterburner.gatling.configuration._


object ConfigurationDumper {

    val values: String = {

    var values: String  = "\n"+"="*100+"\nSystem Properties values:\n\n"
    val systemProperties : Properties = System.getProperties
    systemProperties.forEach( (key, value) => values += "System.getProperty(\"" + key + "\") = " + value + "\n")
    values += "="*100 + "\nTestConfiguration values:\n\n"

    for (field <- Configuration.getClass.getDeclaredFields()) {
    field.setAccessible(true)
    values += s"Configuration.${field.getName} = ${field.get(Configuration)}\n"
    }

    println(values) // I print this now, so it will be printed during creation of object instances etc, before running
    values
    }


}