package test

import breeze.linalg.sum
import breeze.numerics.pow
import linearmodels.LinearRegression

class LinearRegressionDemo extends App{

  def getError: Double = {
    val (_, train) = IrisLoader.load()
    val linearRegression = new LinearRegression()
    val X = train(::, 1 to 3)

    val y = train(::, 0)

    linearRegression.fit(X, y)

    val pred = linearRegression.predict(X)

    val error = sum(pow(pred - y, 2)) / y.length

    error
  }
}
