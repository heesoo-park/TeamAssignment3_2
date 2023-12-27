package com.example.teamsns

object SignUpValidExtension {
    /*
    * 한글 이름
    */
    fun String.includeAlphabetAndNumber() =
        Regex("^[a-z0-9]*$")

    /*
    * 한글 이름
    */
    fun String.includeKorean() =
        Regex("^[가-힣]*$")
    /*
    * 특문 포함
    */
    fun String.includeSpecialCharacters() =
        Regex("[!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]+").containsMatchIn(this)

    /*
    * 대문자 포함
    */
    fun String.includeUpperCase() = Regex("[A-Z]").containsMatchIn(this)
}