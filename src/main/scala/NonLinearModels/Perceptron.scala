package NonLinearModels

import breeze.linalg.{DenseMatrix, DenseVector}

// One layer, can only perform Binary Classifications
// Threshold should be almost always 0.5
class Perceptron(learning_rate: Double = 0.1, threshold: Double = 0.5) {

  var weights: DenseVector[Double] = DenseVector[Double]()

  def fit(train: DenseMatrix[Double], y: DenseVector[Int], maxEpoch: Int = 10000): Unit = {
    weights = DenseVector.zeros[Double](train.cols + 1)
    var epoch = 1

    val b = DenseMatrix.ones[Double](y.length, 1)
    // Padding
    val trainI = DenseMatrix.horzcat(b, train)
    var error = -1

    while(epoch <= maxEpoch && error != 0) {
      println(s"Epoch $epoch, error = $error")
      error = 0
      for(j <- 0 until trainI.rows - 1) {
        val yPred = if((trainI(j, ::) * weights) > threshold) 1 else 0
        val err = y(j) - yPred
        if (err != 0) {
          epoch += 1
          error += 1
          weights += (trainI(j, ::).inner * err.toDouble) * learning_rate
        }
      }
    }

    if(error == 0) println(s"Finished after $epoch epochs")
    else println(s"Max Epoch reached with $error error")
  }

  def predict(test: DenseMatrix[Double]): DenseVector[Int] = {
    val b = DenseMatrix.ones[Double](test.rows, 1)
    val testI = DenseMatrix.horzcat(b, test)

    (testI * weights).map(y => if(y > threshold) 1 else 0)
  }
}