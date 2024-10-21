package com.faithie.ipptapp.model.posedetector.repcounting

data class PoseSequence(val poses: List<String>) {
    var currentIndex: Int = 0

    // checks whether the given current pose matches the next expected pose in the sequence
    fun isNextPoseValid(currentPose: String): Boolean {
        if (currentIndex < poses.size) {
            return poses[currentIndex] == currentPose
        }
        return false
    }

    fun advance() {
        if (currentIndex < poses.size) {
            currentIndex++
        }
    }

    fun isCompleted(): Boolean {
        return currentIndex >= poses.size
    }

    fun isFirstPose(classification: String): Boolean {
        return poses[0] == classification
//        return currentIndex == 0 && poses[currentIndex] == classification
    }
}
