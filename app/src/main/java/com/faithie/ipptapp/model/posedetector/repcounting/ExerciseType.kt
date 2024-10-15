package com.faithie.ipptapp.model.posedetector.repcounting

import android.media.AudioManager
import android.media.ToneGenerator
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseLandmark

abstract class ExerciseType {
    abstract val name: String
    abstract val poseSequence: PoseSequence
    open var numReps: Int = 0
    companion object {
        const val MIN_STRAIGHT_ANGLE = 155.0
        const val MAX_STRAIGHT_ANGLE = 180.0

        const val MIN_90DEG_ANGLE = 60.0
        const val MAX_90DEG_ANGLE = 110.0
    }
    abstract fun validateSequence(currentPose: Pose, currentClassification: String): Int
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
