package linearmodels

import breeze.linalg.{DenseMatrix, DenseVector, sum}
import breeze.numerics.{abs, exp}
import utilities.Utilities.logloss

class LogisticRegression {

  var ans: DenseVector[Double] = DenseVector[Double]()

  val MAX: Double = 20.0
  val MIN: Double = -20.0

  def thetha(weights: DenseVector[Double], x: DenseMatrix[Double]): DenseVector[Double] = {
    val ones = DenseVector.ones[Double](x.rows)

    // Prevent overflow cos of Double's precision range
    val z0 = -(x * weights).map(v =>
      if (v > MAX) MAX
      else if(v < MIN) MIN
      else v
    )

    ones / (exp(z0) + 1.0)
  }

  def cost(t1: DenseVector[Double], y: DenseVector[Double], yPred: DenseVector[Double]): Double = {
    val m = y.length
    (-1.0/m) * sum(logloss(y, yPred)) +  sum(t1 * t1.t)
  }

  def fit(train: DenseMatrix[Double], y: DenseVector[Double], learning_rate: Double = .1, ep: Double = 0.001, maxIter: Int = 10000): Unit = {

    var weights = DenseVector.zeros[Double](train.cols + 1)
    val ones = DenseMatrix.ones[Double](y.length, 1)
    val paddedTrain = DenseMatrix.horzcat(ones, train)


    val m = train.rows
    var yPred = thetha(weights, paddedTrain)
    var err = yPred - y
    var e = cost(weights, y, yPred)
    var J = 100.0
    var nIter = 0

    // while difference between old error and new error is greater than ep given  (i.e progress)
    while(abs(J - e) > ep && nIter <= maxIter) {
      nIter += 1
      J = e
      // that weird sign is element wise multiply
      val reg = weights
      // that weird sign is element wise minus
      weights = weights :- (((paddedTrain.t * err) / m.toDouble) + reg) * learning_rate
      yPred = thetha(weights, paddedTrain)
      err =  yPred - y
      e = cost(weights, y, yPred)
    }

    println(s"Converged in $nIter iterations")
    ans = weights
  }

  def predictProbability(test: DenseMatrix[Double]): DenseVector[Double] = {
    val b1 = DenseMatrix.ones[Double](test.rows, 1)
    val testI = DenseMatrix.horzcat(b1, test)
    thetha(ans, testI)
  }

  def predict(test: DenseMatrix[Double]): DenseVector[Double] = {
    val yPred = predictProbability(test)
    yPred.map(x => if(x > .5) 1.0 else .0)
  }

}
