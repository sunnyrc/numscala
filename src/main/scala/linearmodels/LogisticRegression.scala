package linearmodels

import breeze.linalg.{DenseMatrix, DenseVector, sum}
import breeze.numerics.{abs, exp}
import utilities.Utilities.logloss

import scala.collection.mutable.ListBuffer

class LogisticRegression {

  var ans: DenseVector[Double] = DenseVector[Double]()

  val MAX: Double = 200.0
  val MIN: Double = -200.0

  var ls = new ListBuffer[Double]()

  def thetha(weights: DenseVector[Double], x: DenseMatrix[Double]): DenseVector[Double] = {
    val ones = DenseVector.ones[Double](x.rows)

    // Prevent overflow cos of Double's precision range
    val notOverflow = -(x * weights).map(v =>
      if (v > MAX) MAX
      else if(v < MIN) MIN
      else v
    )

    ones / (exp(notOverflow) + 1.0)
  }

  def cost(x: DenseVector[Double], y: DenseVector[Double], pred: DenseVector[Double]): Double = (-1.0/y.length) * sum(logloss(y, pred)) +  sum(x * x.t)

  def fit(train: DenseMatrix[Double], y: DenseVector[Double], learning_rate: Double = 0.1, ep: Double = 0.00001, maxEpoch: Int = 5000): Unit = {

    var weights = DenseVector.zeros[Double](train.cols + 1)
    val ones = DenseMatrix.ones[Double](y.length, 1)
    val paddedTrain = DenseMatrix.horzcat(ones, train)


    val m = train.rows
    var pred = thetha(weights, paddedTrain)
    var err = pred - y
    var e = cost(weights, y, pred)

    var old_e = 100.0
    var epoch = 0

    // while difference between old error and new error is greater than ep given  (i.e progress)
    while(abs(old_e - e) > ep && epoch <= maxEpoch) {
      epoch += 1
      old_e = e

      // element wise minus
      weights = weights :- (((paddedTrain.t * err) / m.toDouble) + weights) * learning_rate
      pred = thetha(weights, paddedTrain)
      err =  pred - y
      e = cost(weights, y, pred)
      ls+=e
    }

    ans = weights
  }

  def predict(test: DenseMatrix[Double]): DenseVector[Double] = {
    val ones = DenseMatrix.ones[Double](test.rows, 1)
    val paddedTest = DenseMatrix.horzcat(ones, test)
    val pred = thetha(ans, paddedTest)
    pred.map(x => if(x > .5) 1.0 else .0)
  }

}
