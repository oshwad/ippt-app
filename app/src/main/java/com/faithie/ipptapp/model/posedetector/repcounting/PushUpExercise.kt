package com.faithie.ipptapp.model.posedetector.repcounting

import android.util.Log
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseLandmark

class PushUpExercise : ExerciseType() {
    private val TAG = "PushUpExercise"
    override val name = "PushUp"
    companion object{
        const val PUSHUP_DOWN = "pushup_down"
        const val PUSHUP_MID = "pushup_mid"
        const val PUSHUP_UP = "pushup_up"
        const val MIN_STRAIGHT_BODY_ANGLE = 170.0
    }

    override val poseSequence = PoseSequence(
        listOf(PUSHUP_DOWN, PUSHUP_MID, PUSHUP_UP)
    )

    override val MIN_STRAIGHT_ANGLE = 160.0
    override val MAX_90DEG_ANGLE = 90.0

    override fun validateCurrentStage(pose: Pose, currentPose: String): Boolean {
        return when (currentPose) {
            PUSHUP_DOWN -> validatePushUpDown(pose)
            PUSHUP_MID -> validatePushUpMid(pose)
            PUSHUP_UP -> validatePushUpUp(pose)
            else -> false
        }
    }

    var validationResults = emptyList<ValidationResult>()

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

        val isLeftBodyValid = leftBodyAngle in MIN_STRAIGHT_BODY_ANGLE..MAX_STRAIGHT_ANGLE
        val isRightBodyValid = rightBodyAngle in MIN_STRAIGHT_BODY_ANGLE..MAX_STRAIGHT_ANGLE
        val isLeftArmValid = leftArmAngle <= MAX_90DEG_ANGLE
        val isRightArmValid = rightArmAngle <= MAX_90DEG_ANGLE

        validationResults = listOf(
            ValidationResult(PUSHUP_DOWN,"leftBody", leftBodyAngle, isLeftBodyValid),
            ValidationResult(PUSHUP_DOWN,"rightBody", rightBodyAngle, isRightBodyValid),
            ValidationResult(PUSHUP_DOWN,"leftArm", leftArmAngle, isLeftArmValid),
            ValidationResult(PUSHUP_DOWN,"rightArm", rightArmAngle, isRightArmValid)
        )
        Log.d(TAG,"$validationResults")

        if (leftBodyAngle in MIN_STRAIGHT_BODY_ANGLE..MAX_STRAIGHT_ANGLE
            && rightBodyAngle in MIN_STRAIGHT_BODY_ANGLE..MAX_STRAIGHT_ANGLE
            && ((leftArmAngle <= MAX_90DEG_ANGLE) || (rightArmAngle <= MAX_90DEG_ANGLE))) {
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

        val isLeftBodyValid = leftBodyAngle in MIN_STRAIGHT_BODY_ANGLE..MAX_STRAIGHT_ANGLE
        val isRightBodyValid = rightBodyAngle in MIN_STRAIGHT_BODY_ANGLE..MAX_STRAIGHT_ANGLE

        validationResults = listOf(
            ValidationResult(PUSHUP_MID,"leftBody", leftBodyAngle, isLeftBodyValid),
            ValidationResult(PUSHUP_MID,"rightBody", rightBodyAngle, isRightBodyValid),
        )
        Log.d(TAG,"$validationResults")

        if (leftBodyAngle in MIN_STRAIGHT_ANGLE..MAX_STRAIGHT_ANGLE &&
            rightBodyAngle in MIN_STRAIGHT_ANGLE..MAX_STRAIGHT_ANGLE) {
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

        val isLeftBodyValid = leftBodyAngle in MIN_STRAIGHT_BODY_ANGLE..MAX_STRAIGHT_ANGLE
        val isRightBodyValid = rightBodyAngle in MIN_STRAIGHT_BODY_ANGLE..MAX_STRAIGHT_ANGLE
        val isLeftArmValid = leftArmAngle >= MIN_STRAIGHT_ANGLE
        val isRightArmValid = rightArmAngle >= MIN_STRAIGHT_ANGLE

        validationResults = listOf(
            ValidationResult(PUSHUP_UP,"leftBody", leftBodyAngle, isLeftBodyValid),
            ValidationResult(PUSHUP_UP,"rightBody", rightBodyAngle, isRightBodyValid),
            ValidationResult(PUSHUP_UP,"leftArm", leftArmAngle, isLeftArmValid),
            ValidationResult(PUSHUP_UP,"rightArm", rightArmAngle, isRightArmValid)
        )
        Log.d(TAG,"$validationResults")

        if ((leftBodyAngle in MIN_STRAIGHT_BODY_ANGLE..MAX_STRAIGHT_ANGLE
            && rightBodyAngle in MIN_STRAIGHT_BODY_ANGLE..MAX_STRAIGHT_ANGLE
            && (leftArmAngle >= MIN_STRAIGHT_ANGLE
            || rightArmAngle >= MIN_STRAIGHT_ANGLE))) {
            Log.d(TAG, "pushup_up SUCCESS leftbodyangle: $leftBodyAngle, rightbodyangle: $rightBodyAngle, leftarmangle: $leftArmAngle, rightarmangle: $rightArmAngle")
            return true
        } else {
            Log.d(TAG, "pushup_up failed leftbodyangle: $leftBodyAngle, rightbodyangle: $rightBodyAngle, leftarmangle: $leftArmAngle, rightarmangle: $rightArmAngle")
            return false
        }
    }
}
