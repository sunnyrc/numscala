package nonlinear

import breeze.linalg.{DenseMatrix, DenseVector}

import scala.collection.mutable.ListBuffer

/**
  * One layer, can only perform Binary Classifications
  * Threshold should be almost always 0.5
  * @param learning_rate
  * @param threshold
  */
class Perceptron(learning_rate: Double = 0.1, threshold: Double = 0.5) {

  var weights: DenseVector[Double] = DenseVector[Double]()
  var ls = new ListBuffer[Double]()

  def fit(train: DenseMatrix[Double], y: DenseVector[Int], maxEpoch: Int = 10000): Unit = {

    weights = DenseVector.zeros[Double](train.cols + 1)
    var epoch = 1

    val ones = DenseMatrix.ones[Double](y.length, 1)

    // Padding
    val paddedTrain = DenseMatrix.horzcat(ones, train)
    var error = -1

    while(epoch <= maxEpoch && error != 0) {

      error = 0
      for(i <- 0 until paddedTrain.rows - 1) {

        val pred = if((paddedTrain(i, ::) * weights) > threshold) 1 else 0
        val err = y(i) - pred
        if (err != 0) {
          epoch += 1
          error += 1
          weights += (paddedTrain(i, ::).inner * err.toDouble) * learning_rate
          ls+=error
        }
      }
    }
    if(error == 0) println(s"$epoch epochs later.")
    else println(s"Max Epoch reached with $error error")
  }

  def predict(test: DenseMatrix[Double]): DenseVector[Int] = {
    val ones = DenseMatrix.ones[Double](test.rows, 1)
    val paddedTest = DenseMatrix.horzcat(ones, test)

    (paddedTest * weights).map(y => if(y > threshold) 1 else 0)
  }
}