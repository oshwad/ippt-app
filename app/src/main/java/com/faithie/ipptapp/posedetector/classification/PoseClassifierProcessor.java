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

import android.content.Context;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.WorkerThread;

import com.google.common.base.Preconditions;
import com.google.mlkit.vision.pose.Pose;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Accepts a stream of {@link Pose} for classification and Rep counting.
 */
public class PoseClassifierProcessor {
  private static final String TAG = "PoseClassifierProcessor";
  private static final String POSE_SAMPLES_FILE = "fitness_pose_samples.csv";
  private RepetitionCounter pushupRepCounter;
  private RepetitionCounter situpRepCounter;
  private ExerciseType currentExercise;

  // Specify classes for which we want rep counting.
  // These are the labels in the given {@code POSE_SAMPLES_FILE}. You can set your own class labels
  // for your pose samples.
  private static final String PUSHUPS_CLASS = "pushups_down";
  private static final String SITUPS_CLASS = "situps_up";

  private final boolean isStreamMode;

  private EMASmoothing emaSmoothing;
  private PoseClassifier poseClassifier;

  @WorkerThread
  public PoseClassifierProcessor(Context context, boolean isStreamMode, ExerciseType exerciseType) {
    Log.d(TAG, "PoseClassifierProcessor constructor");
    Preconditions.checkState(Looper.myLooper() != Looper.getMainLooper());
    Log.d(TAG, "Preconditions checked");
    this.currentExercise = exerciseType;
    this.isStreamMode = isStreamMode;
    if (isStreamMode) {
      emaSmoothing = new EMASmoothing();
    }
    loadPoseSamples(context);
    Log.d(TAG, "Pose samples loaded");
    pushupRepCounter = new RepetitionCounter(ExerciseType.PUSHUP);
    situpRepCounter = new RepetitionCounter(ExerciseType.SITUP);
  }

  private void loadPoseSamples(Context context) {
    List<PoseSample> poseSamples = new ArrayList<>();
    try {
//      BufferedReader reader = new BufferedReader(
//          new InputStreamReader(context.getAssets().open(POSE_SAMPLES_FILE)));
      File csvFile = new File(context.getExternalFilesDir(null), "poses.csv");
      BufferedReader reader = new BufferedReader(new FileReader(csvFile));
      String csvLine = reader.readLine();
      while (csvLine != null) {
        // If line is not a valid {@link PoseSample}, we'll get null and skip adding to the list.
        PoseSample poseSample = PoseSample.getPoseSample(csvLine, ",");
        if (poseSample != null) {
          poseSamples.add(poseSample);
        }
        csvLine = reader.readLine();
      }
    } catch (IOException e) {
      Log.e(TAG, "Error when loading pose samples.\n" + e);
    }
    poseClassifier = new PoseClassifier(poseSamples);
  }

  /**
   * Given a new {@link Pose} input, returns a list of formatted {@link String}s with Pose
   * classification results.
   *
   * <p>Currently it returns up to 2 strings as following:
   * 0: PoseClass : X reps
   * 1: PoseClass : [0.0-1.0] confidence
   */
  @WorkerThread
  public int getPoseResult(Pose pose) {
//    Preconditions.checkState(Looper.myLooper() != Looper.getMainLooper());
    ClassificationResult classification = poseClassifier.classify(pose);

    RepetitionCounter currentRepCounter = (currentExercise == ExerciseType.PUSHUP)
            ? pushupRepCounter : situpRepCounter;

    // Update {@link RepetitionCounter}s if {@code isStreamMode}.
    if (isStreamMode) {
      // Feed pose to smoothing even if no pose found.
      classification = emaSmoothing.getSmoothedResult(classification);

      // Return early without updating repCounter if no pose found.
      if (pose.getAllPoseLandmarks().isEmpty()) {
        return currentRepCounter.getNumRepeats();
      }

      int repsBefore = currentRepCounter.getNumRepeats();
      int repsAfter = currentRepCounter.addClassificationResult(classification, pose);

      if (repsAfter > repsBefore) {
        // Play a fun beep when rep counter updates.
        ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
        tg.startTone(ToneGenerator.TONE_PROP_BEEP);
      }

      return repsAfter; // Return the current number of reps
    }

    // Add maxConfidence class of current frame to result if pose is found.
//    if (!pose.getAllPoseLandmarks().isEmpty()) {
//      String maxConfidenceClass = classification.getMaxConfidenceClass();
//      String maxConfidenceClassResult = String.format(
//          Locale.US,
//          "%s : %.2f confidence",
//          maxConfidenceClass,
//          classification.getClassConfidence(maxConfidenceClass)
//              / poseClassifier.confidenceRange());
//      result.add(maxConfidenceClassResult);
//    }

    return 0; // Default case, if it's not stream mode or other conditions
  }


}
