package test

import breeze.linalg.{DenseMatrix, DenseVector, Transpose}
import breeze.numerics.ceil
import nonlinear.Perceptron
import org.sameersingh.scalaplot.jfreegraph.JFGraphPlotter
import utilities.Utilities
import org.sameersingh.scalaplot.{MemXYSeries, XYChart, XYData}

object PerceptronTest extends App{
  val (labels, all_data): (DenseVector[Int], DenseMatrix[Double]) = DigitLoader.load()
  val instances_train: Int = ceil(all_data.rows * 0.8).toInt
  val instances_test: Int = all_data.rows - instances_train
  val train: DenseMatrix[Double] = all_data(0 to instances_train, ::)
  val test: DenseMatrix[Double] = all_data(instances_train until all_data.rows, ::)


  // Predict class 9 cos can only perform Binary Classifications
  val predLabel = 9
  val y = labels.map(l => if(l == predLabel) 1 else 0)
  val train_y = y.slice(0, ceil(y.length * 0.8).toInt + 1)
  val test_y = y.slice(ceil(y.length * 0.8).toInt, y.length - 1)

  val perceptron = new Perceptron()
  perceptron.fit(train, train_y)
  val yPred = perceptron.predict(test)
  println(s"Accuracy: ${Utilities.accuracy(test_y, yPred)}")
  val x = (0 until 421).map(_.toDouble)
  val yy: Seq[Double] = perceptron.ls
  val series = new MemXYSeries(x, yy, "Error")
  val data = new XYData(series)
  val chart = new XYChart("Epoch vs Error", data)
  chart.showLegend = true
  val plotter = new JFGraphPlotter(chart)
  plotter.gui()
}
