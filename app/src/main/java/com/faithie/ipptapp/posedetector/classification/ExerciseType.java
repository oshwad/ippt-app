package com.faithie.ipptapp.posedetector.classification;

import com.google.mlkit.vision.pose.Pose;
import com.google.mlkit.vision.pose.PoseLandmark;

public enum ExerciseType {
    PUSHUP {
        @Override
        public boolean isPoseValid(Pose pose) {
            // Logic for push-up pose validation
            PoseLandmark leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER);
            PoseLandmark rightShoulder = pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER);
            PoseLandmark leftHip = pose.getPoseLandmark(PoseLandmark.LEFT_HIP);
            PoseLandmark rightHip = pose.getPoseLandmark(PoseLandmark.RIGHT_HIP);
            PoseLandmark leftKnee = pose.getPoseLandmark(PoseLandmark.LEFT_KNEE);
            PoseLandmark rightKnee = pose.getPoseLandmark(PoseLandmark.RIGHT_KNEE);

            float leftAngle = calculateAngle(leftShoulder, leftHip, leftKnee);
            float rightAngle = calculateAngle(rightShoulder, rightHip, rightKnee);

            // Incorrect posture e.g., sagging or raising hips
            return (leftAngle >= 160 && leftAngle <= 180) && (rightAngle >= 160 && rightAngle <= 180);
        }
    },
    SITUP {
        @Override
        public boolean isPoseValid(Pose pose) {
            // Logic for sit-up pose validation
            PoseLandmark leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER);
            PoseLandmark rightShoulder = pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER);
            PoseLandmark leftHip = pose.getPoseLandmark(PoseLandmark.LEFT_HIP);
            PoseLandmark rightHip = pose.getPoseLandmark(PoseLandmark.RIGHT_HIP);
            PoseLandmark leftHeel = pose.getPoseLandmark(PoseLandmark.LEFT_HEEL);
            PoseLandmark rightHeel = pose.getPoseLandmark(PoseLandmark.RIGHT_HEEL);

            float leftAngle = calculateAngle(leftShoulder, leftHip, leftHeel);
            float rightAngle = calculateAngle(rightShoulder, rightHip, rightHeel);

            return (leftAngle >= 85 && leftAngle <= 95) && (rightAngle >= 85 && rightAngle <= 95);
        }
    };

    // Abstract method to be implemented by each exercise type
    public abstract boolean isPoseValid(Pose pose);

    // Angle calculation method shared by all exercises
    protected float calculateAngle(PoseLandmark firstPoint, PoseLandmark middlePoint, PoseLandmark lastPoint) {
        double result = Math.toDegrees(
                Math.atan2(lastPoint.getPosition().y - middlePoint.getPosition().y,
                        lastPoint.getPosition().x - middlePoint.getPosition().x)
                        - Math.atan2(firstPoint.getPosition().y - middlePoint.getPosition().y,
                        firstPoint.getPosition().x - middlePoint.getPosition().x)
        );
        result = Math.abs(result);
        if (result > 180) {
            result = 360.0 - result;
        }
        return (float) result;
    }
}
