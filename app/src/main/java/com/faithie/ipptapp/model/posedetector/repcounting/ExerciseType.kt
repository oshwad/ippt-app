package com.faithie.ipptapp.model.posedetector.repcounting

import android.media.AudioManager
import android.media.ToneGenerator
import android.util.Log
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseLandmark

abstract class ExerciseType {
    val tag = "ExerciseType"
    abstract val name: String
    abstract val poseSequence: PoseSequence
    open var numReps: Int = 0

    private var frameCount = 0 // Initialize a frame counter

    fun onNewFrame(currentPose: Pose, currentClassification: String): Int {
        frameCount++ // Increment the frame counter on each new frame

        // Call the validation sequence, passing the frame count
        return validateSequence(currentPose, currentClassification)
    }

    private var lastAdvanceFrameCount = -1 // Track the last frame where the sequence advanced
    private val cooldownFrames = 5 // Number of frames to wait before checking for a reset again
    open fun validateSequence(currentPose: Pose, currentClassification: String): Int {
        Log.d(tag, "current index: ${poseSequence.currentIndex}, currentClassification: $currentClassification")

        // Check if it's been long enough since the last advancement before allowing resets
        if (poseSequence.currentIndex > 0 && poseSequence.isFirstPose(currentClassification)) {
            if (frameCount - lastAdvanceFrameCount > cooldownFrames) {
                Log.d(tag, "resetting to first pose in sequence")
                poseSequence.currentIndex = 0 // Reset sequence to the first pose
            }
        }

        // Continue with normal sequence validation
        if (poseSequence.isNextPoseValid(currentClassification)) {
            if (validateCurrentStage(currentPose, currentClassification)) { // Check if the current stage is valid
                poseSequence.advance() // Advance the sequence if valid
                lastAdvanceFrameCount = frameCount // Update the last advancement frame count

                if (poseSequence.isCompleted()) {
                    numReps++
                    playBeep()
                    poseSequence.currentIndex = 0
                }
                return numReps
            }
//            else {
//                // Reset sequence if ANY of the validation stages fail
//                if (frameCount - lastAdvanceFrameCount > cooldownFrames) {
//                    poseSequence.currentIndex = 0 // Reset sequence to the first pose
//                }
//            }
        }

        return numReps
    }

    abstract fun validateCurrentStage(pose: Pose, currentPose: String): Boolean

    protected open fun calculateAngle(
        firstPoint: PoseLandmark,
        middlePoint: PoseLandmark,
        lastPoint: PoseLandmark
    ): Float {
        var result = Math.toDegrees(
            Math.atan2(
                (lastPoint.position.y - middlePoint.position.y).toDouble(),
                (
                        lastPoint.position.x - middlePoint.position.x).toDouble()
            )
                    - Math.atan2(
                (firstPoint.position.y - middlePoint.position.y).toDouble(),
                (
                        firstPoint.position.x - middlePoint.position.x).toDouble()
            )
        )
        result = Math.abs(result)
        if (result > 180) {
            result = 360.0 - result
        }
        return result.toFloat()
    }

    protected open fun playBeep(){
        val tg = ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100)
        tg.startTone(ToneGenerator.TONE_PROP_BEEP)
    }

    open fun resetReps() {
        numReps = 0
    }

    open fun getExerciseName(): String {
        return name
    }
}
