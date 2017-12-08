package test

import breeze.linalg.{DenseVector, sum}
import linearmodels.LogisticRegression
import utilities.Utilities.{logloss, train_test_split_double}

class LogisticRegressionDemo extends App{

  def getAxis: Seq[Double] = {
    val predLabel = "Iris-versicolor"

    val (labels, all_data) = IrisLoader.load()
    val y: DenseVector[Double] = labels.map(l => if (l == predLabel) 1.0 else 0.0)

    val (train, train_y, test, test_y) = train_test_split_double(all_data, y)

    // Made a new test cos it came with an extra row
    val test2 = test(1 until test.rows, ::)
    val logisticRegression = new LogisticRegression()
    logisticRegression.fit(train, train_y)

    val yPred: DenseVector[Double] = logisticRegression.predictProbability(test2)
    //  println(test_y.length, test2.rows)

    val loss: Double = sum(logloss(test_y, yPred)) / test_y.length
    println(s"Logloss : $loss")


    logisticRegression.ls
  }
}
