package utilities

import breeze.linalg.DenseVector

object Utilities {

  // e = value and i = index
  // Want to check if e equal to predicted value
  def accuracy(y: DenseVector[Int], yPred: DenseVector[Int]): Double =
    y.toArray.zipWithIndex.count {
      case (e, i) => e == yPred(i)
    } / y.length.toDouble
}
