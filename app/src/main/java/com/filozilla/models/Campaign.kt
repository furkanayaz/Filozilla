package com.filozilla.models

import java.io.*

data class Campaign(val imageLink: String, val type: String, val title: String, var desc: String) : Serializable