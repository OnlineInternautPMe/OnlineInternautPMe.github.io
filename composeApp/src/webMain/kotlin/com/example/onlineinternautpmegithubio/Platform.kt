package com.example.onlineinternautpmegithubio

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform