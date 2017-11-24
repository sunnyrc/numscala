package linearmodels

import breeze.linalg.{DenseMatrix, DenseVector, inv}

class LinearRegression {

  var ans: DenseVector[Double] = DenseVector[Double]()

  def fit(train: DenseMatrix[Double], labels: DenseVector[Double], C: Double = 1.0): Unit ={
    val ones = DenseMatrix.ones[Double](labels.length, 1)

    // Pad the train
    val paddedTrain = DenseMatrix.horzcat(ones, train)
    // Create a Matrix with ones along the diagonal axis

    val diag = DenseMatrix.eye[Double](paddedTrain.cols)

    val lambda = 1 / C
    val times = diag * lambda

    ans = inv(paddedTrain.t * paddedTrain + times) * (paddedTrain.t * labels)
  }

  def predict(test: DenseMatrix[Double]): DenseVector[Double] = {
    val ones = DenseMatrix.ones[Double](test.rows, 1)
    val paddedtest = DenseMatrix.horzcat(ones, test)
    paddedtest * ans
  }
}
