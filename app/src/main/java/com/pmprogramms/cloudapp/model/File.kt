package com.pmprogramms.cloudapp.model

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.pmprogramms.cloudapp.helpers.FileType
import org.jetbrains.annotations.NotNull
import java.io.Serializable

class File(
    @NotNull val title: String,
    @NotNull val firebasePath: String,
    @NotNull val type: FileType
) : Serializable
