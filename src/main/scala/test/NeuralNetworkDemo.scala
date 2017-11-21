package test

import nonlinear.NeuralNetwork
object NeuralNetworkDemo extends App{
  val (labels, train) = DigitLoader.load()
  // Predict class 9 cos can only perform Binary Classification
  val predLabel = 9
  val y = labels.map(l => if(l == predLabel) 1 else 0)
  val perceptron = new NeuralNetwork(train, labels, 2, 2)
//  perceptron.fit(train, y)
//  val yPred = perceptron.predict(train)
//  println(s"Accuracy: ${Utilities.accuracy(y, yPred)}")

}
