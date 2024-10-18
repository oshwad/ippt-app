package com.faithie.ipptapp.model.posedetector.repcounting

data class ValidationResult(
    val classification: String,
    val location: String,
    val angle: Float,
    val valid: Boolean,
    val handEarDist: Float? = null
)
