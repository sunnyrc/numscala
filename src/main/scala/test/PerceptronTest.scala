package test

import NonLinearModels.Perceptron
import utilities.Utilities

object PerceptronTest extends App{
  val (labels, train) = DigitLoader.load()
  // Predict class 9 cos can only perform Binary Classifications
  val predLabel = 9
  val y = labels.map(l => if(l == predLabel) 1 else 0)
  val perceptron = new Perceptron()
  perceptron.fit(train, y)
  val yPred = perceptron.predict(train)
  println(s"Accuracy: ${Utilities.accuracy(y, yPred)}")
}
