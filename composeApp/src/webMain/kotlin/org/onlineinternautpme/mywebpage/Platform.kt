package org.onlineinternautpme.mywebpage

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform