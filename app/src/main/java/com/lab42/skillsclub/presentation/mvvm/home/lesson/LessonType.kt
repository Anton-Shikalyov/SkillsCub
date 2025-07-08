package com.lab42.skillsclub.presentation.mvvm.home.lesson

enum class LessonType( val type: String) {
    TRUE_FALSE("true_false"),
    CHOOSE_RIGHT_OPTION("choose_right_option"),
    CHOOSE_RIGHT_WORD("choose_right_word"),
    MEDIA_CHOOSE_RIGHT_OPTION("media_choose_right_option");

    override fun toString(): String = type
}