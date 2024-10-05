/*
 * Copyright 2020 Google LLC. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.faithie.ipptapp.posedetector.classification;

import android.util.Log;

import com.google.mlkit.vision.pose.Pose;
import com.google.mlkit.vision.pose.PoseLandmark;

/**
 * Counts reps for the give class.
 */
public class RepetitionCounter {
  private static final String TAG = "RepetitionCounter";
  // These thresholds can be tuned in conjunction with the Top K values in {@link PoseClassifier}.
  // The default Top K value is 10 so the range here is [0-10].
  private static final float DEFAULT_ENTER_THRESHOLD = 6f;
  private static final float DEFAULT_EXIT_THRESHOLD = 4f;

//  private final String className;
  private final ExerciseType exerciseType;
  private final float enterThreshold;
  private final float exitThreshold;

  private int numRepeats;
  private boolean poseEntered;

  public RepetitionCounter(ExerciseType exerciseType) {
    this(exerciseType, DEFAULT_ENTER_THRESHOLD, DEFAULT_EXIT_THRESHOLD);
  }

  public RepetitionCounter(ExerciseType exerciseType, float enterThreshold, float exitThreshold) {
    this.exerciseType = exerciseType;
    this.enterThreshold = enterThreshold;
    this.exitThreshold = exitThreshold;
    numRepeats = 0;
    poseEntered = false;
  }

  /**
   * Adds a new Pose classification result and updates reps for given class.
   *
   * @param classificationResult {link ClassificationResult} of class to confidence values.
   * @return number of reps.
   */
  public int addClassificationResult(ClassificationResult classificationResult, Pose pose) {
    float poseConfidence = classificationResult.getClassConfidence(exerciseType.name());

    if (!poseEntered) {
      poseEntered = poseConfidence > enterThreshold;
      return numRepeats;
    }

    if (poseConfidence < exitThreshold) {
      if (exerciseType.isPoseValid(pose)) {
        numRepeats++;
      } else {
        Log.d(TAG, "Incorrect posture detected. No count");
      }
      poseEntered = false;
    }

    return numRepeats;
  }

  public ExerciseType getExerciseType() {
    return exerciseType;
  }

  public int getNumRepeats() {
    return numRepeats;
  }
}
