package test

import breeze.linalg.sum
import breeze.numerics.pow
import linearmodels.LinearRegression

class LinearRegressionDemo extends App{

  def getError: Double = {
    val (labels, train) = IrisLoader.load()
    val clf = new LinearRegression()
    val X = train(::, 1 to 3)

    // Predict sepal length
    val y = train(::, 0)

    clf.fit(X, y)

    var yPred = clf.predict(X)

    var error = sum(pow(yPred - y, 2)) / y.length
    println(s"Standard Error: $error")

    error
  }
}
