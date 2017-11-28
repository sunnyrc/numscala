package test

import breeze.linalg.{DenseMatrix, DenseVector, Transpose}
import breeze.numerics.ceil
import nonlinear.Perceptron
import org.sameersingh.scalaplot.jfreegraph.JFGraphPlotter
import utilities.Utilities
import org.sameersingh.scalaplot.{MemXYSeries, XYChart, XYData}

object PerceptronDemo extends App{
  val (labels, all_data): (DenseVector[Int], DenseMatrix[Double]) = DigitLoader.load()

  // Predict class 9 cos can only perform Binary Classifications
  val predLabel = 9
  val y: DenseVector[Int] = labels.map(l => if(l == predLabel) 1 else 0)
  val (train, train_y, test, test_y) = Utilities.train_test_split(all_data,y)

  val perceptron = new Perceptron(learning_rate = 0.3)
  perceptron.fit(train, train_y)
  val yPred = perceptron.predict(test)
  println(s"Accuracy: ${Utilities.accuracy(test_y, yPred)}")

  val yy: Seq[Double] = perceptron.ls
  val x = yy.indices.map(_.toDouble)

  val series = new MemXYSeries(x, yy, "Error")
  val data = new XYData(series)
  val chart = new XYChart("Epoch vs Error", data)
  chart.showLegend = true
  val plotter = new JFGraphPlotter(chart)
  plotter.gui()
}
