package com.faithie.ipptapp.posedetector.repcounting

import android.util.Log
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseLandmark

class PushUpExercise : ExerciseType() {
    private val TAG = "PushUpExercise"
    override val name = "PushUp"
    companion object{
        const val PUSHUP_UP = "pushup_up"
        const val PUSHUP_DOWN = "pushup_down"
    }

    override val poseSequence = PoseSequence(
        listOf(PUSHUP_UP, PUSHUP_DOWN, PUSHUP_UP)
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
            PUSHUP_UP -> validatePushUpUp(pose)
            PUSHUP_DOWN -> validatePushUpDown(pose)
            else -> false
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

        if (leftShoulder == null || rightShoulder == null || leftHip == null ||
            rightHip == null || leftKnee == null || rightKnee == null ||
            leftElbow == null || rightElbow == null ||
            leftWrist == null || rightWrist == null) {
            // At least one required landmark is not detected
            return false
        }

        val leftBodyAngle = calculateAngle(leftShoulder, leftHip, leftKnee)
        val rightBodyAngle = calculateAngle(rightShoulder, rightHip, rightKnee)
        val leftArmAngle = calculateAngle(leftShoulder, leftElbow, leftWrist)
        val rightArmAngle = calculateAngle(rightShoulder, rightElbow, rightWrist)

        if ((leftBodyAngle in MIN_STRAIGHT_ANGLE..MAX_STRAIGHT_ANGLE
                    && rightBodyAngle in MIN_STRAIGHT_ANGLE..MAX_STRAIGHT_ANGLE
                    && leftArmAngle in MIN_STRAIGHT_ANGLE..MAX_STRAIGHT_ANGLE
                    && rightArmAngle in MIN_STRAIGHT_ANGLE..MAX_STRAIGHT_ANGLE)) {
            Log.d(TAG, "pushup_up pose validation success leftbodyangle: $leftBodyAngle, rightbodyangle: $rightBodyAngle, leftarmangle: $leftArmAngle, rightarmangle: $rightArmAngle")
        }

        if (!(leftBodyAngle in MIN_STRAIGHT_ANGLE..MAX_STRAIGHT_ANGLE
                    && rightBodyAngle in MIN_STRAIGHT_ANGLE..MAX_STRAIGHT_ANGLE
                    && leftArmAngle in MIN_STRAIGHT_ANGLE..MAX_STRAIGHT_ANGLE
                    && rightArmAngle in MIN_STRAIGHT_ANGLE..MAX_STRAIGHT_ANGLE)) {
            Log.d(TAG, "pushup_up pose validation failed leftbodyangle: $leftBodyAngle, rightbodyangle: $rightBodyAngle, leftarmangle: $leftArmAngle, rightarmangle: $rightArmAngle")
        }

        return (leftBodyAngle in MIN_STRAIGHT_ANGLE..MAX_STRAIGHT_ANGLE
                && rightBodyAngle in MIN_STRAIGHT_ANGLE..MAX_STRAIGHT_ANGLE
                && (leftArmAngle in MIN_STRAIGHT_ANGLE..MAX_STRAIGHT_ANGLE
                || rightArmAngle in MIN_STRAIGHT_ANGLE..MAX_STRAIGHT_ANGLE) )
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
            // At least one required landmark is not detected
            return false
        }

        val leftBodyAngle = calculateAngle(leftShoulder, leftHip, leftKnee)
        val rightBodyAngle = calculateAngle(rightShoulder, rightHip, rightKnee)
        val leftArmAngle = calculateAngle(leftShoulder, leftElbow, leftWrist)
        val rightArmAngle = calculateAngle(rightShoulder, rightElbow, rightWrist)

//        if (!(leftBodyAngle in MIN_STRAIGHT_ANGLE..MAX_STRAIGHT_ANGLE
//                    && rightBodyAngle in MIN_STRAIGHT_ANGLE..MAX_STRAIGHT_ANGLE
//                    && leftArmAngle in MIN_90DEG_ANGLE..MAX_90DEG_ANGLE
//                    && rightArmAngle in MIN_90DEG_ANGLE..MAX_90DEG_ANGLE)) {
//            Log.d(TAG, "pushup_down pose validation failed")
//        }

        if (( (leftBodyAngle in (MIN_STRAIGHT_ANGLE..MAX_STRAIGHT_ANGLE))
                    && (rightBodyAngle in (MIN_STRAIGHT_ANGLE..MAX_STRAIGHT_ANGLE))
                    && ((leftArmAngle <= MAX_90DEG_ANGLE) || (rightArmAngle <= MAX_90DEG_ANGLE))
                    )) {
            Log.d(TAG, "pushup_down pose validation success leftbodyangle: $leftBodyAngle, rightbodyangle: $rightBodyAngle, leftarmangle: $leftArmAngle, rightarmangle: $rightArmAngle")
        }

        if (!( (leftBodyAngle in (MIN_STRAIGHT_ANGLE..MAX_STRAIGHT_ANGLE))
                    && (rightBodyAngle in (MIN_STRAIGHT_ANGLE..MAX_STRAIGHT_ANGLE))
                    && ((leftArmAngle <= MAX_90DEG_ANGLE) || (rightArmAngle <= MAX_90DEG_ANGLE))
                    )) {
            Log.d(TAG, "pushup_down pose validation failed leftbodyangle: $leftBodyAngle, rightbodyangle: $rightBodyAngle, leftarmangle: $leftArmAngle, rightarmangle: $rightArmAngle")
        }

        return (( (leftBodyAngle in (MIN_STRAIGHT_ANGLE..MAX_STRAIGHT_ANGLE))
                && (rightBodyAngle in (MIN_STRAIGHT_ANGLE..MAX_STRAIGHT_ANGLE))
                && ((leftArmAngle <= MAX_90DEG_ANGLE) || (rightArmAngle <= MAX_90DEG_ANGLE))
                ))
    }
}
