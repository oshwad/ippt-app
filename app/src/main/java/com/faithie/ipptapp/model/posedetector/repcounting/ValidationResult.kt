package com.faithie.ipptapp.model.posedetector.repcounting

data class ValidationResult(
    val classification: String,
    val location: String? = null,
    val angle: Float? = null,
    val validAngles: Boolean? = null,
    val handEarDist: Float? = null,
    val validHandEarDist: Boolean? = null
)
