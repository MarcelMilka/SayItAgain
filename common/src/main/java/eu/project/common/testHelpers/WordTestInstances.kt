package eu.project.common.testHelpers

import eu.project.common.model.Word

object WordTestInstances {

    val list = listOf(
        Word("Cat"),
        Word("nequaquam ut"),
        Word("Hagetreboa")
    )

    val first = list[0]
    val second = list[1]
    val third = list[2]
}