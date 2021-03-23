package com.pmprogramms.cloudapp.helpers

enum class UploadState(var message: String) {
    FAIL("Fail of task"),
    SUCCESS("Task done"),
    INGOING("Task in going");
}