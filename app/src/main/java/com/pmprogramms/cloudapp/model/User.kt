package com.pmprogramms.cloudapp.model

import org.jetbrains.annotations.NotNull
import java.io.Serializable


class User(
    @NotNull val uid: String,
    @NotNull val name: String,
    @NotNull val email: String
) : Serializable
