package test

import breeze.linalg.{DenseMatrix, DenseVector}
import nonlinear.Perceptron
import utilities.Utilities

class PerceptronDemo extends App{

  def getAxis: (Seq[Double], Double) = {
    val (labels, all_data): (DenseVector[Int], DenseMatrix[Double]) = DigitLoader.load()


    val targetLabel = 7
    val y: DenseVector[Int] = labels.map(l => if (l == targetLabel) 1 else 0)
    val (train, train_y, test, test_y) = Utilities.train_test_split(all_data, y)

    val perceptron = new Perceptron(learning_rate = 0.3)
    perceptron.fit(train, train_y)
    val pred = perceptron.predict(test)

    val yy: Seq[Double] = perceptron.ls

    (yy, Utilities.accuracy(test_y, pred))
  }
}
