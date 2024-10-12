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

import com.faithie.ipptapp.posedetector.repcounting.ExerciseType;
import com.faithie.ipptapp.posedetector.repcounting.PushUpExercise;
import com.faithie.ipptapp.posedetector.repcounting.SitUpExercise;
import com.google.common.base.Preconditions;
import com.google.mlkit.vision.pose.Pose;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Accepts a stream of {@link Pose} for classification and Rep counting.
 */
public class PoseClassifierProcessor {
  private static final String TAG = "PoseClassifierProcessor";
  private static final String POSE_SAMPLES_FILE = "poses.csv";
  private final boolean isStreamMode;
  private EMASmoothing emaSmoothing;
  private PoseClassifier poseClassifier;
  public PushUpExercise pushUpExercise;
  public SitUpExercise sitUpExercise;

  @WorkerThread
  public PoseClassifierProcessor(Context context, boolean isStreamMode) {
    Log.d(TAG, "PoseClassifierProcessor constructor");
    Preconditions.checkState(Looper.myLooper() != Looper.getMainLooper());
    this.isStreamMode = isStreamMode;
    if (isStreamMode) {
      emaSmoothing = new EMASmoothing();
    }
    pushUpExercise = new PushUpExercise();
    sitUpExercise = new SitUpExercise();
    loadPoseSamples(context);
    Log.d(TAG, "Pose samples loaded");
  }

  private void loadPoseSamples(Context context) {
    List<PoseSample> poseSamples = new ArrayList<>();
    try {
//      BufferedReader reader = new BufferedReader(
//          new InputStreamReader(context.getAssets().open(POSE_SAMPLES_FILE)));
      File csvFile = new File(context.getExternalFilesDir(null), POSE_SAMPLES_FILE);
      BufferedReader reader = new BufferedReader(new FileReader(csvFile));
      Log.d(TAG, "poses.csv file loaded");
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

  @WorkerThread
  public String getClassifiedPose(Pose pose, ExerciseType exerciseType) {
//    Log.d(TAG, "classifying pose");
    ClassificationResult classification = poseClassifier.classify(pose, exerciseType);

    if (isStreamMode) {
      // Feed pose to smoothing even if no pose found.
      classification = emaSmoothing.getSmoothedResult(classification);

      return classification.getMaxConfidenceClass();
    }

    return "unknown at " + TAG; // Default case, if it's not stream mode or other conditions
  }

  public void resetReps() {
    pushUpExercise.resetReps();
    sitUpExercise.resetReps();
  }
}
