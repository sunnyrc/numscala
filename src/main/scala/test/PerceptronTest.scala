package test

import nonlinear.Perceptron
import org.sameersingh.scalaplot.jfreegraph.JFGraphPlotter
import utilities.Utilities
import org.sameersingh.scalaplot.{MemXYSeries, XYChart, XYData, XYDataImplicits}

object PerceptronTest extends App{
  val (labels, train) = DigitLoader.load()
  // Predict class 9 cos can only perform Binary Classifications
  val predLabel = 9
  val y = labels.map(l => if(l == predLabel) 1 else 0)
  val perceptron = new Perceptron()
  perceptron.fit(train, y)
  val yPred = perceptron.predict(train)
  println(s"Accuracy: ${Utilities.accuracy(y, yPred)}")
  val x = (0 until 488).map(_.toDouble)
  val yy: Seq[Double] = perceptron.ls
//  println(x.length, yy.length)
  val series = new MemXYSeries(x, yy, "Error")
  val data = new XYData(series)
  val chart = new XYChart("Epoch vs Error", data)
  chart.showLegend = true
  val plotter = new JFGraphPlotter(chart)
  plotter.gui()
}
