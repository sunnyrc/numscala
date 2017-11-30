package linearmodels

import breeze.linalg.{DenseMatrix, DenseVector, inv}

class LinearRegression {

  var ans: DenseVector[Double] = DenseVector[Double]()

  def fit(train: DenseMatrix[Double], labels: DenseVector[Double]): Unit ={
    val ones = DenseMatrix.ones[Double](labels.length, 1)

    // Pad the train
    val paddedTrain = DenseMatrix.horzcat(ones, train)

    // Create a Matrix with ones along the diagonal axis
    val diag = DenseMatrix.eye[Double](paddedTrain.cols)

    ans = inv(paddedTrain.t * paddedTrain + diag) * (paddedTrain.t * labels)
  }

  def predict(test: DenseMatrix[Double]): DenseVector[Double] = {
    val ones = DenseMatrix.ones[Double](test.rows, 1)
    val paddedtest = DenseMatrix.horzcat(ones, test)
    paddedtest * ans
  }
}
