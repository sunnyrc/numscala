package test

import breeze.linalg.{DenseVector, sum}
import linearmodels.LogisticRegression
import utilities.Utilities.{logloss, train_test_split_double}

class LogisticRegressionDemo extends App{

  def getAxis: Seq[Double] = {
    val targetLabel = "Iris-setosa"

    val (labels, all_data) = IrisLoader.load()
    val y: DenseVector[Double] = labels.map(l => if (l == targetLabel) 1.0 else 0.0)

    val (train, train_y, test, test_y) = train_test_split_double(all_data, y)

    val logisticRegression = new LogisticRegression()
    logisticRegression.fit(train, train_y)

    logisticRegression.ls
  }
}
