package com.faithie.ipptapp.model.posedetector.repcounting

import android.util.Log
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseLandmark

class SitUpExercise : ExerciseType() {
    private val TAG = "SitUpExercise"
    override val name = "SitUp"
    companion object{
        const val SITUP_UP = "situp_up"
        const val SITUP_MID = "situp_mid"
        const val SITUP_DOWN = "situp_down"
        const val MAX_HAND_EAR_DISTANCE = 15 // needs further experimentation
    }

    override val poseSequence = PoseSequence(
        listOf(SITUP_UP, SITUP_MID, SITUP_DOWN)
    )

    override fun validateCurrentStage(pose: Pose, currentPose: String): Boolean {
        return when (currentPose) {
            SITUP_UP -> validateSitUpUp(pose)
            SITUP_MID -> validateSitUpMid(pose)
            SITUP_DOWN -> validateSitUpDown(pose)
            else -> false
        }
    }

    private fun validateSitUpUp(pose: Pose): Boolean {
        Log.d(TAG, "situp_up")
        val isHandsCuppingEars = validateHandsCuppingEars(pose)

        if (!isHandsCuppingEars) {
            return false
        }

        return validateBodyPositionForSitUp(pose, MIN_90DEG_ANGLE)
    }

    private fun validateSitUpMid(pose: Pose): Boolean {
        Log.d(TAG, "situp_mid")
        return validateHandsCuppingEars(pose)
    }

    private fun validateSitUpDown(pose: Pose): Boolean {
        Log.d(TAG, "situp_down")
        val isHandsCuppingEars = validateHandsCuppingEars(pose)

        if (!isHandsCuppingEars) {
            return false
        }

        return validateBodyPositionForSitUp(pose, MIN_STRAIGHT_ANGLE)
    }

    private fun validateBodyPositionForSitUp(pose: Pose, requiredAngle: Double): Boolean {
        val leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER)
        val rightShoulder = pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER)
        val leftHip = pose.getPoseLandmark(PoseLandmark.LEFT_HIP)
        val rightHip = pose.getPoseLandmark(PoseLandmark.RIGHT_HIP)
        val leftHeel = pose.getPoseLandmark(PoseLandmark.LEFT_HEEL)
        val rightHeel = pose.getPoseLandmark(PoseLandmark.RIGHT_HEEL)

        if (leftShoulder == null || rightShoulder == null || leftHip == null || rightHip == null || leftHeel == null || rightHeel == null) {
            return false
        }

        val leftBodyAngle = calculateAngle(leftShoulder, leftHip, leftHeel)
        val rightBodyAngle = calculateAngle(rightShoulder, rightHip, rightHeel)

        if (leftBodyAngle >= requiredAngle && rightBodyAngle >= requiredAngle) {
            Log.d(TAG,"situp stage SUCCESS leftBodyAngle: $leftBodyAngle, rightBodyAngle $rightBodyAngle")
            return true
        } else {
            Log.d(TAG,"situp stage failed leftBodyAngle: $leftBodyAngle, rightBodyAngle $rightBodyAngle")
            return false
        }
    }

    private fun validateHandsCuppingEars(pose: Pose): Boolean {
        val leftEar = pose.getPoseLandmark(PoseLandmark.LEFT_EAR)
        val rightEar = pose.getPoseLandmark(PoseLandmark.RIGHT_EAR)
        val leftWrist = pose.getPoseLandmark(PoseLandmark.LEFT_INDEX)
        val rightWrist = pose.getPoseLandmark(PoseLandmark.RIGHT_INDEX)

        if (leftEar == null || rightEar == null || leftWrist == null || rightWrist == null) {
            return false
        }

        val leftHandEarDistance = calculateDistance(leftWrist, leftEar)
        val rightHandEarDistance = calculateDistance(rightWrist, rightEar)

        if (leftHandEarDistance > MAX_HAND_EAR_DISTANCE || rightHandEarDistance > MAX_HAND_EAR_DISTANCE) {
            Log.d(TAG, "Hands not cupping ears. Left hand-ear distance: $leftHandEarDistance, Right hand-ear distance: $rightHandEarDistance")
            return false
        }

        Log.d(TAG, "Hands cupping ears. Left hand-ear distance: $leftHandEarDistance, Right hand-ear distance: $rightHandEarDistance")
        return true
    }

    private fun calculateDistance(landmark1: PoseLandmark, landmark2: PoseLandmark): Double {
        val dx = landmark1.position.x - landmark2.position.x
        val dy = landmark1.position.y - landmark2.position.y
        return Math.sqrt((dx * dx + dy * dy).toDouble())
    }
}
