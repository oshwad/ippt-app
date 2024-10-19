package com.faithie.ipptapp.model.posedetector.classification;

import android.content.Context;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.WorkerThread;

import com.faithie.ipptapp.model.posedetector.repcounting.SitUpExercise;
import com.faithie.ipptapp.model.posedetector.repcounting.ExerciseType;
import com.faithie.ipptapp.model.posedetector.repcounting.PushUpExercise;
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
  private final boolean isStreamMode;
  private EMASmoothing emaSmoothing;
  private PoseClassifier pushUpClassifier;
  private PoseClassifier sitUpClassifier;
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

    // Load classifiers for each exercise
    pushUpClassifier = loadPoseClassifier(context, "pushup_poses.csv");
    sitUpClassifier = loadPoseClassifier(context, "situp_poses.csv");
  }

  /**
   * Load pose samples from a CSV file and create a PoseClassifier.
   */
  private PoseClassifier loadPoseClassifier(Context context, String fileName) {
    List<PoseSample> poseSamples = new ArrayList<>();
    try {
      File csvFile = new File(context.getExternalFilesDir(null), fileName);
      BufferedReader reader = new BufferedReader(new FileReader(csvFile));
      Log.d(TAG, fileName + " file loaded");
      String csvLine = reader.readLine();
      while (csvLine != null) {
        PoseSample poseSample = PoseSample.getPoseSample(csvLine, ",");
        if (poseSample != null) {
          poseSamples.add(poseSample);
        }
        csvLine = reader.readLine();
      }
      reader.close();
    } catch (IOException e) {
      Log.e(TAG, "Error when loading pose samples from " + fileName + ".\n" + e);
    }
    return new PoseClassifier(poseSamples);
  }

  @WorkerThread
  public String getClassifiedPose(Pose pose, ExerciseType exerciseType) {
    PoseClassifier selectedClassifier;

    // Use the appropriate classifier based on exercise type
    if (exerciseType instanceof PushUpExercise) {
      selectedClassifier = pushUpClassifier;
    } else if (exerciseType instanceof SitUpExercise) {
      selectedClassifier = sitUpClassifier;
    } else {
      return "Unknown exercise type in " + TAG;
    }

    // Classify the pose
    ClassificationResult classification = selectedClassifier.classify(pose, exerciseType);

    if (isStreamMode) {
      classification = emaSmoothing.getSmoothedResult(classification);
      return classification.getMaxConfidenceClass();
    }

    return "unknown at " + TAG; // Default case
  }

  public void resetReps() {
    pushUpExercise.resetReps();
    sitUpExercise.resetReps();
  }
}
