package com.faithie.ipptapp.model.posedetector.repcounting

import android.util.Log
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseLandmark
import kotlin.math.sqrt

class SitUpExercise : ExerciseType() {
    private val TAG = "SitUpExercise"
    var validationResults = emptyList<ValidationResult>()
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
        val leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER)
        val rightShoulder = pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER)
        val leftHip = pose.getPoseLandmark(PoseLandmark.LEFT_HIP)
        val rightHip = pose.getPoseLandmark(PoseLandmark.RIGHT_HIP)
        val leftAnkle = pose.getPoseLandmark(PoseLandmark.LEFT_ANKLE)
        val rightKnee = pose.getPoseLandmark(PoseLandmark.RIGHT_ANKLE)

        val leftEar = pose.getPoseLandmark(PoseLandmark.LEFT_EAR)
        val rightEar = pose.getPoseLandmark(PoseLandmark.RIGHT_EAR)
        val leftWrist = pose.getPoseLandmark(PoseLandmark.LEFT_INDEX)
        val rightWrist = pose.getPoseLandmark(PoseLandmark.RIGHT_INDEX)

        if (leftShoulder == null || rightShoulder == null || leftHip == null ||
            rightHip == null || leftAnkle == null || rightKnee == null ||
            leftEar == null || rightEar == null || leftWrist == null || rightWrist == null) {
            return false
        }

        val leftBodyAngle = calculateAngle(leftShoulder, leftHip, leftAnkle)
        val rightBodyAngle = calculateAngle(rightShoulder, rightHip, rightKnee)

        val leftHandEarDistance = calculateDistance(leftWrist, leftEar)
        val rightHandEarDistance = calculateDistance(rightWrist, rightEar)

        val isLeftBodyValid = leftBodyAngle >= MIN_90DEG_ANGLE
        val isRightBodyValid = rightBodyAngle >= MIN_90DEG_ANGLE

        val isLeftHandEarDistValid = leftHandEarDistance <= MAX_HAND_EAR_DISTANCE
        val isRightHandEarDistValid = rightHandEarDistance <= MAX_HAND_EAR_DISTANCE

        validationResults = listOf(
            ValidationResult(SITUP_UP, "leftBody", leftBodyAngle, isLeftBodyValid, leftHandEarDistance, isLeftHandEarDistValid),
            ValidationResult(SITUP_UP, "rightBody", rightBodyAngle, isRightBodyValid, rightHandEarDistance, isRightHandEarDistValid)
        )

        if (isLeftBodyValid && isRightBodyValid && isLeftHandEarDistValid && isRightHandEarDistValid) {
            Log.d(TAG, "situp_down SUCCESS leftBodyAngle: $leftBodyAngle, rightBodyAngle: $rightBodyAngle")
            return true
        } else {
            Log.d(TAG, "situp_down failed leftBodyAngle: $leftBodyAngle, rightBodyAngle: $rightBodyAngle")
            return false
        }
    }

    private fun validateSitUpMid(pose: Pose): Boolean {
        val leftEar = pose.getPoseLandmark(PoseLandmark.LEFT_EAR)
        val rightEar = pose.getPoseLandmark(PoseLandmark.RIGHT_EAR)
        val leftWrist = pose.getPoseLandmark(PoseLandmark.LEFT_INDEX)
        val rightWrist = pose.getPoseLandmark(PoseLandmark.RIGHT_INDEX)

        if (leftEar == null || rightEar == null || leftWrist == null || rightWrist == null) {
            return false
        }

        val leftHandEarDistance = calculateDistance(leftWrist, leftEar)
        val rightHandEarDistance = calculateDistance(rightWrist, rightEar)

        val isLeftHandEarDistValid = leftHandEarDistance <= MAX_HAND_EAR_DISTANCE
        val isRightHandEarDistValid = rightHandEarDistance <= MAX_HAND_EAR_DISTANCE

        validationResults = listOf(
            ValidationResult(SITUP_MID, location = "left hand-ear dist", handEarDist = leftHandEarDistance, validHandEarDist = isLeftHandEarDistValid),
            ValidationResult(SITUP_MID, location = "right hand-ear dist", handEarDist = rightHandEarDistance, validHandEarDist = isRightHandEarDistValid)
        )

        if (isLeftHandEarDistValid && isRightHandEarDistValid) {
            return true
        } else {
            return false
        }
    }

    private fun validateSitUpDown(pose: Pose): Boolean {
        val leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER)
        val rightShoulder = pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER)
        val leftHip = pose.getPoseLandmark(PoseLandmark.LEFT_HIP)
        val rightHip = pose.getPoseLandmark(PoseLandmark.RIGHT_HIP)
        val leftAnkle = pose.getPoseLandmark(PoseLandmark.LEFT_ANKLE)
        val rightKnee = pose.getPoseLandmark(PoseLandmark.RIGHT_ANKLE)

        val leftEar = pose.getPoseLandmark(PoseLandmark.LEFT_EAR)
        val rightEar = pose.getPoseLandmark(PoseLandmark.RIGHT_EAR)
        val leftWrist = pose.getPoseLandmark(PoseLandmark.LEFT_INDEX)
        val rightWrist = pose.getPoseLandmark(PoseLandmark.RIGHT_INDEX)

        if (leftShoulder == null || rightShoulder == null || leftHip == null ||
            rightHip == null || leftAnkle == null || rightKnee == null ||
            leftEar == null || rightEar == null || leftWrist == null || rightWrist == null) {
            return false
        }

        val leftBodyAngle = calculateAngle(leftShoulder, leftHip, leftAnkle)
        val rightBodyAngle = calculateAngle(rightShoulder, rightHip, rightKnee)

        val leftHandEarDistance = calculateDistance(leftWrist, leftEar)
        val rightHandEarDistance = calculateDistance(rightWrist, rightEar)

        val isLeftBodyValid = leftBodyAngle >= MIN_STRAIGHT_ANGLE
        val isRightBodyValid = rightBodyAngle >= MIN_STRAIGHT_ANGLE

        val isLeftHandEarDistValid = leftHandEarDistance <= MAX_HAND_EAR_DISTANCE
        val isRightHandEarDistValid = rightHandEarDistance <= MAX_HAND_EAR_DISTANCE

        validationResults = listOf(
            ValidationResult(SITUP_DOWN, "leftBody", leftBodyAngle, isLeftBodyValid, leftHandEarDistance, isLeftHandEarDistValid),
            ValidationResult(SITUP_DOWN, "rightBody", rightBodyAngle, isRightBodyValid, rightHandEarDistance, isRightHandEarDistValid)
        )

        if (isLeftBodyValid && isRightBodyValid && isLeftHandEarDistValid && isRightHandEarDistValid) {
            Log.d(TAG, "situp_down SUCCESS leftBodyAngle: $leftBodyAngle, rightBodyAngle: $rightBodyAngle")
            return true
        } else {
            Log.d(TAG, "situp_down failed leftBodyAngle: $leftBodyAngle, rightBodyAngle: $rightBodyAngle")
            return false
        }
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

    private fun calculateDistance(landmark1: PoseLandmark, landmark2: PoseLandmark): Float {
        val dx = landmark1.position.x - landmark2.position.x
        val dy = landmark1.position.y - landmark2.position.y
        return sqrt((dx * dx + dy * dy).toDouble()).toFloat()
    }
}
