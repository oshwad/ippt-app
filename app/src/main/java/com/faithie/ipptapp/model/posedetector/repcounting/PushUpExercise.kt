package com.faithie.ipptapp.model.posedetector.repcounting

import android.util.Log
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseLandmark

class PushUpExercise : ExerciseType() {
    private val TAG = "PushUpExercise"
    var validationResults = emptyList<ValidationResult>()
    override val name = "PushUp"
    companion object{
        const val PUSHUP_DOWN = "pushup_down"
        const val PUSHUP_MID = "pushup_mid"
        const val PUSHUP_UP = "pushup_up"
        const val minStraightBodyAngle = 170.0
        const val maxStraightAngle = 180.0
        const val max90degAngle = 90.0
        const val minStraightAngle = 160.0
    }

    override val poseSequence = PoseSequence(
        listOf(PUSHUP_DOWN, PUSHUP_MID, PUSHUP_UP)
    )

    override fun validateCurrentStage(pose: Pose, currentPose: String): Boolean {
        return when (currentPose) {
            PUSHUP_DOWN -> validatePushUpDown(pose)
            PUSHUP_MID -> validatePushUpMid(pose)
            PUSHUP_UP -> validatePushUpUp(pose)
            else -> false
        }
    }

    private fun validatePushUpDown(pose: Pose): Boolean {
        val leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER)
        val rightShoulder = pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER)
        val leftHip = pose.getPoseLandmark(PoseLandmark.LEFT_HIP)
        val rightHip = pose.getPoseLandmark(PoseLandmark.RIGHT_HIP)
        val leftKnee = pose.getPoseLandmark(PoseLandmark.LEFT_KNEE)
        val rightKnee = pose.getPoseLandmark(PoseLandmark.RIGHT_KNEE)
        val leftElbow = pose.getPoseLandmark(PoseLandmark.LEFT_ELBOW)
        val rightElbow = pose.getPoseLandmark(PoseLandmark.RIGHT_ELBOW)
        val leftWrist = pose.getPoseLandmark(PoseLandmark.LEFT_WRIST)
        val rightWrist = pose.getPoseLandmark(PoseLandmark.RIGHT_WRIST)

        if (leftShoulder == null || rightShoulder == null || leftHip == null ||
            rightHip == null || leftKnee == null || rightKnee == null ||
            leftElbow == null || rightElbow == null ||
            leftWrist == null || rightWrist == null) {
            return false
        }

        val leftBodyAngle = calculateAngle(leftShoulder, leftHip, leftKnee)
        val rightBodyAngle = calculateAngle(rightShoulder, rightHip, rightKnee)
        val leftArmAngle = calculateAngle(leftShoulder, leftElbow, leftWrist)
        val rightArmAngle = calculateAngle(rightShoulder, rightElbow, rightWrist)

        val isLeftBodyValid = leftBodyAngle in minStraightBodyAngle..maxStraightAngle
        val isRightBodyValid = rightBodyAngle in minStraightBodyAngle..maxStraightAngle
        val isLeftArmValid = leftArmAngle <= max90degAngle
        val isRightArmValid = rightArmAngle <= max90degAngle

        validationResults = listOf(
            ValidationResult(PUSHUP_DOWN,"leftBody", leftBodyAngle, isLeftBodyValid),
            ValidationResult(PUSHUP_DOWN,"rightBody", rightBodyAngle, isRightBodyValid),
            ValidationResult(PUSHUP_DOWN,"leftArm", leftArmAngle, isLeftArmValid),
            ValidationResult(PUSHUP_DOWN,"rightArm", rightArmAngle, isRightArmValid)
        )

        if (leftBodyAngle in minStraightBodyAngle..maxStraightAngle
            && rightBodyAngle in minStraightBodyAngle..maxStraightAngle
            && ((leftArmAngle <= max90degAngle) || (rightArmAngle <= max90degAngle))) {
            Log.d(TAG, "pushup_down SUCCESS leftbodyangle: $leftBodyAngle, rightbodyangle: $rightBodyAngle, leftarmangle: $leftArmAngle, rightarmangle: $rightArmAngle")
            return true
        } else {
            Log.d(TAG, "pushup_down failed leftbodyangle: $leftBodyAngle, rightbodyangle: $rightBodyAngle, leftarmangle: $leftArmAngle, rightarmangle: $rightArmAngle")
            return false
        }
    }

    private fun validatePushUpMid(pose: Pose): Boolean {
        val leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER)
        val rightShoulder = pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER)
        val leftHip = pose.getPoseLandmark(PoseLandmark.LEFT_HIP)
        val rightHip = pose.getPoseLandmark(PoseLandmark.RIGHT_HIP)
        val leftKnee = pose.getPoseLandmark(PoseLandmark.LEFT_KNEE)
        val rightKnee = pose.getPoseLandmark(PoseLandmark.RIGHT_KNEE)

        if (leftShoulder == null || rightShoulder == null || leftHip == null ||
            rightHip == null || leftKnee == null || rightKnee == null) {
            return false
        }

        val leftBodyAngle = calculateAngle(leftShoulder, leftHip, leftKnee)
        val rightBodyAngle = calculateAngle(rightShoulder, rightHip, rightKnee)

        val isLeftBodyValid = leftBodyAngle in minStraightBodyAngle..maxStraightAngle
        val isRightBodyValid = rightBodyAngle in minStraightBodyAngle..maxStraightAngle

        validationResults = listOf(
            ValidationResult(PUSHUP_MID,"leftBody", leftBodyAngle, isLeftBodyValid),
            ValidationResult(PUSHUP_MID,"rightBody", rightBodyAngle, isRightBodyValid),
        )

        if (leftBodyAngle in minStraightAngle..maxStraightAngle &&
            rightBodyAngle in minStraightAngle..maxStraightAngle) {
            Log.d(TAG, "pushup_mid SUCCESS leftBodyAngle: $leftBodyAngle, rightBodyAngle: $rightBodyAngle")
            return true
        } else {
            Log.d(TAG, "pushup_mid failed leftBodyAngle: $leftBodyAngle, rightBodyAngle: $rightBodyAngle")
            return false
        }
    }

    private fun validatePushUpUp(pose: Pose): Boolean {
        val leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER)
        val rightShoulder = pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER)
        val leftHip = pose.getPoseLandmark(PoseLandmark.LEFT_HIP)
        val rightHip = pose.getPoseLandmark(PoseLandmark.RIGHT_HIP)
        val leftKnee = pose.getPoseLandmark(PoseLandmark.LEFT_KNEE)
        val rightKnee = pose.getPoseLandmark(PoseLandmark.RIGHT_KNEE)
        val leftElbow = pose.getPoseLandmark(PoseLandmark.LEFT_ELBOW)
        val rightElbow = pose.getPoseLandmark(PoseLandmark.RIGHT_ELBOW)
        val leftWrist = pose.getPoseLandmark(PoseLandmark.LEFT_WRIST)
        val rightWrist = pose.getPoseLandmark(PoseLandmark.RIGHT_WRIST)

        if ((leftShoulder == null || leftHip == null || leftKnee == null ||
            leftElbow == null || leftWrist == null) ||
            (rightShoulder == null || rightHip == null ||  rightKnee == null ||
            rightElbow == null || rightWrist == null)) {
            return false
        }

        val leftBodyAngle = calculateAngle(leftShoulder, leftHip, leftKnee)
        val rightBodyAngle = calculateAngle(rightShoulder, rightHip, rightKnee)
        val leftArmAngle = calculateAngle(leftShoulder, leftElbow, leftWrist)
        val rightArmAngle = calculateAngle(rightShoulder, rightElbow, rightWrist)

        val isLeftBodyValid = leftBodyAngle in minStraightBodyAngle..maxStraightAngle
        val isRightBodyValid = rightBodyAngle in minStraightBodyAngle..maxStraightAngle
        val isLeftArmValid = leftArmAngle >= minStraightAngle
        val isRightArmValid = rightArmAngle >= minStraightAngle

        validationResults = listOf(
            ValidationResult(PUSHUP_UP,"leftBody", leftBodyAngle, isLeftBodyValid),
            ValidationResult(PUSHUP_UP,"rightBody", rightBodyAngle, isRightBodyValid),
            ValidationResult(PUSHUP_UP,"leftArm", leftArmAngle, isLeftArmValid),
            ValidationResult(PUSHUP_UP,"rightArm", rightArmAngle, isRightArmValid)
        )

        if ((leftBodyAngle in minStraightBodyAngle..maxStraightAngle
            && rightBodyAngle in minStraightBodyAngle..maxStraightAngle
            && (leftArmAngle >= minStraightAngle
            || rightArmAngle >= minStraightAngle))) {
            Log.d(TAG, "pushup_up SUCCESS leftbodyangle: $leftBodyAngle, rightbodyangle: $rightBodyAngle, leftarmangle: $leftArmAngle, rightarmangle: $rightArmAngle")
            return true
        } else {
            Log.d(TAG, "pushup_up failed leftbodyangle: $leftBodyAngle, rightbodyangle: $rightBodyAngle, leftarmangle: $leftArmAngle, rightarmangle: $rightArmAngle")
            return false
        }
    }
}
