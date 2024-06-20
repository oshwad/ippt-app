package com.faithie.ipptapp.feature_posedetector.poseclassification

import com.google.mlkit.vision.common.PointF3D
import kotlin.math.absoluteValue

//import com.google.mlkit.vision.common.PointF3D
//import kotlin.math.absoluteValue
//
///**
// * Utility methods for operations on [PointF3D].
// */
//object Utils { // object: singleton class
//
//    fun add(a: PointF3D, b: PointF3D): PointF3D {
//        return PointF3D.from(a.x + b.x, a.y + b.y, a.z + b.z)
//    }
//
//    fun subtract(b: PointF3D, a: PointF3D): PointF3D {
//        return PointF3D.from(a.x - b.x, a.y - b.y, a.z - b.z)
//    }
//
//    fun multiply(a: PointF3D, multiple: Float): PointF3D {
//        return PointF3D.from(a.x * multiple, a.y * multiple, a.z * multiple)
//    }
//
//    fun multiply(a: PointF3D, multiple: PointF3D): PointF3D {
//        return PointF3D.from(a.x * multiple.x, a.y * multiple.y, a.z * multiple.z)
//    }
//
//    fun average(a: PointF3D, b: PointF3D): PointF3D {
//        return PointF3D.from((a.x + b.x) * 0.5f, (a.y + b.y) * 0.5f, (a.z + b.z) * 0.5f)
//    }
//
//    fun l2Norm2D(point: PointF3D): Float {
//        return kotlin.math.hypot(point.x.toDouble(), point.y.toDouble()).toFloat()
//    }
//
//    fun maxAbs(point: PointF3D): Float {
//        return maxOf(point.x.absoluteValue, point.y.absoluteValue, point.z.absoluteValue)
//    }
//
//    fun sumAbs(point: PointF3D): Float {
//        return point.x.absoluteValue + point.y.absoluteValue + point.z.absoluteValue
//    }
//
//    fun addAll(pointsList: MutableList<PointF3D>, p: PointF3D) {
//        val iterator = pointsList.listIterator()
//        while (iterator.hasNext()) {
//            iterator.set(add(iterator.next(), p))
//        }
//    }
//
//    fun subtractAll(p: PointF3D, pointsList: MutableList<PointF3D>) {
//        val iterator = pointsList.listIterator()
//        while (iterator.hasNext()) {
//            iterator.set(subtract(p, iterator.next()))
//        }
//    }
//
//    fun multiplyAll(pointsList: MutableList<PointF3D>, multiple: Float) {
//        val iterator = pointsList.listIterator()
//        while (iterator.hasNext()) {
//            iterator.set(multiply(iterator.next(), multiple))
//        }
//    }
//
//    fun multiplyAll(pointsList: MutableList<PointF3D>, multiple: PointF3D) {
//        val iterator = pointsList.listIterator()
//        while (iterator.hasNext()) {
//            iterator.set(multiply(iterator.next(), multiple))
//        }
//    }
//}


/**
 * Utility methods for operations on [PointF3D].
 */
object Utils {
    fun add(a: PointF3D, b: PointF3D): PointF3D {
        return PointF3D.from(a.x + b.x, a.y + b.y, a.z + b.z)
    }

    fun subtract(b: PointF3D, a: PointF3D): PointF3D {
        return PointF3D.from(a.x - b.x, a.y - b.y, a.z - b.z)
    }

    fun multiply(a: PointF3D, multiple: Float): PointF3D {
        return PointF3D.from(a.x * multiple, a.y * multiple, a.z * multiple)
    }

    fun multiply(a: PointF3D, multiple: PointF3D): PointF3D {
        return PointF3D.from(
            a.x * multiple.x, a.y * multiple.y, a.z * multiple.z
        )
    }

    fun average(a: PointF3D, b: PointF3D): PointF3D {
        return PointF3D.from(
            (a.x + b.x) * 0.5f, (a.y + b.y) * 0.5f, (a.z + b.z) * 0.5f
        )
    }

    fun l2Norm2D(point: PointF3D): Float {
        return Math.hypot(point.x.toDouble(), point.y.toDouble()).toFloat()
    }

    fun maxAbs(point: PointF3D): Float {
        return maxOf(point.x.absoluteValue, point.y.absoluteValue, point.z.absoluteValue)
    }

    fun sumAbs(point: PointF3D): Float {
        return Math.abs(point.x) + Math.abs(point.y) + Math.abs(point.z)
    }

    fun addAll(pointsList: MutableList<PointF3D>, p: PointF3D) {
        val iterator = pointsList.listIterator()
        while (iterator.hasNext()) {
            iterator.set(add(iterator.next(), p))
        }
    }

    fun subtractAll(p: PointF3D, pointsList: MutableList<PointF3D>) {
        val iterator = pointsList.listIterator()
        while (iterator.hasNext()) {
            iterator.set(subtract(p, iterator.next()))
        }
    }

    fun multiplyAll(pointsList: MutableList<PointF3D>, multiple: Float) {
        val iterator = pointsList.listIterator()
        while (iterator.hasNext()) {
            iterator.set(multiply(iterator.next(), multiple))
        }
    }

    fun multiplyAll(pointsList: MutableList<PointF3D>, multiple: PointF3D) {
        val iterator = pointsList.listIterator()
        while (iterator.hasNext()) {
            iterator.set(multiply(iterator.next(), multiple))
        }
    }
}