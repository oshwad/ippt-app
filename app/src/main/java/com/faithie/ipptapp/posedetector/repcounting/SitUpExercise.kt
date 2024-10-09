package com.faithie.ipptapp.posedetector.repcounting

import android.util.Log
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseLandmark

class SitUpExercise : ExerciseType() {
    private val TAG = "SitUpExercise"
    override val name = "SitUp"
    companion object{
        const val SITUP_DOWN = "situp_down"
        const val SITUP_UP = "situp_up"
    }

    override val poseSequence = PoseSequence(
        listOf(SITUP_DOWN, SITUP_UP, SITUP_DOWN)
    )

    override fun validateSequence(currentPose: Pose, currentClassification: String): Int {
        if (poseSequence.isNextPoseValid(currentClassification)) {
            if (validateCurrentStage(currentPose, currentClassification)) { // Check if the current stage is valid
                poseSequence.advance() // Advance the sequence if valid

                if (poseSequence.isCompleted()) {
                    numReps++
                    playBeep()
                    poseSequence.currentIndex = 0
                }
                return numReps
            }
        }
        return numReps
    }

    private fun validateCurrentStage(pose: Pose, currentPose: String): Boolean {
        return when (currentPose) {
            SITUP_DOWN -> validateSitUpDown(pose)
            SITUP_UP -> validateSitUpUp(pose)
            else -> false
        }
    }

    private fun validateSitUpDown(pose: Pose): Boolean {
        val leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER)
        val rightShoulder = pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER)
        val leftHip = pose.getPoseLandmark(PoseLandmark.LEFT_HIP)
        val rightHip = pose.getPoseLandmark(PoseLandmark.RIGHT_HIP)
        val leftHeel = pose.getPoseLandmark(PoseLandmark.LEFT_HEEL)
        val rightHeel = pose.getPoseLandmark(PoseLandmark.RIGHT_HEEL)

        if (leftShoulder == null || rightShoulder == null || leftHip == null ||
            rightHip == null || leftHeel == null || rightHeel == null) {
            // At least one required landmark is not detected
            return false
        }

        val leftBodyAngle = calculateAngle(leftShoulder, leftHip, leftHeel)
        val rightBodyAngle = calculateAngle(rightShoulder, rightHip, rightHeel)

        if (!(leftBodyAngle in MIN_STRAIGHT_ANGLE..MAX_STRAIGHT_ANGLE
                && rightBodyAngle in MIN_STRAIGHT_ANGLE..MAX_STRAIGHT_ANGLE)) {
            Log.d(TAG, "situp_down pose validation failed")
        }

        return (leftBodyAngle in MIN_STRAIGHT_ANGLE..MAX_STRAIGHT_ANGLE
                && rightBodyAngle in MIN_STRAIGHT_ANGLE..MAX_STRAIGHT_ANGLE)
    }

    private fun validateSitUpUp(pose: Pose): Boolean {
        val leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER)
        val rightShoulder = pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER)
        val leftHip = pose.getPoseLandmark(PoseLandmark.LEFT_HIP)
        val rightHip = pose.getPoseLandmark(PoseLandmark.RIGHT_HIP)
        val leftHeel = pose.getPoseLandmark(PoseLandmark.LEFT_HEEL)
        val rightHeel = pose.getPoseLandmark(PoseLandmark.RIGHT_HEEL)

        if (leftShoulder == null || rightShoulder == null || leftHip == null ||
            rightHip == null || leftHeel == null || rightHeel == null) {
            // At least one required landmark is not detected
            return false
        }

        val leftBodyAngle = calculateAngle(leftShoulder, leftHip, leftHeel)
        val rightBodyAngle = calculateAngle(rightShoulder, rightHip, rightHeel)

        if (!(leftBodyAngle in MIN_90DEG_ANGLE..MAX_90DEG_ANGLE
                    && rightBodyAngle in MIN_90DEG_ANGLE..MAX_90DEG_ANGLE)) {
            Log.d(TAG, "situp_up pose validation failed")
            Log.d(TAG, "situp_up: left body angle: $leftBodyAngle")
            Log.d(TAG, "situp_up: right body angle: $rightBodyAngle")
        }

        return (leftBodyAngle in MIN_90DEG_ANGLE..MAX_90DEG_ANGLE
                && rightBodyAngle in MIN_90DEG_ANGLE..MAX_90DEG_ANGLE)
    }
}
