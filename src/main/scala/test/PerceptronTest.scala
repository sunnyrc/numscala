package test

import breeze.linalg.{DenseMatrix, DenseVector}
import breeze.numerics.ceil
import nonlinear.Perceptron
import org.sameersingh.scalaplot.jfreegraph.JFGraphPlotter
import utilities.Utilities
import org.sameersingh.scalaplot.{MemXYSeries, XYChart, XYData}

object PerceptronTest extends App{
  val (labels, all_data): (DenseVector[Int], DenseMatrix[Double]) = DigitLoader.load()
//  val instances_train = ceil(all_data.size * 0.8)
//  val instances_test = all_data.size - instances_train
//  println(instances_test, instances_train)
//  val train = all_data(instances_train, ::)
//  val test = all_data(instances_test, ::)

  // Predict class 9 cos can only perform Binary Classifications
  val predLabel = 9
  val y = labels.map(l => if(l == predLabel) 1 else 0)
  val perceptron = new Perceptron()
  perceptron.fit(all_data, y)
  println(all_data(1,::))
  val yPred = perceptron.predict(all_data)
  println(s"Accuracy: ${Utilities.accuracy(y, yPred)}")
  val x = (0 until 488).map(_.toDouble)
  val yy: Seq[Double] = perceptron.ls
  val series = new MemXYSeries(x, yy, "Error")
  val data = new XYData(series)
  val chart = new XYChart("Epoch vs Error", data)
  chart.showLegend = true
  val plotter = new JFGraphPlotter(chart)
  plotter.gui()
}
