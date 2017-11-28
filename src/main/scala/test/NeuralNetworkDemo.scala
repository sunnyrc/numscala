package test

import breeze.linalg.DenseVector
import nonlinear.NeuralNetwork
import utilities.Utilities
object NeuralNetworkDemo extends App{
  val (labels, all_data) = BigDigit.load()
  val (train, train_y, test, test_y) = Utilities.train_test_split(all_data,labels)

  // training set doesnt have one of each

  /**
    * 2 Hidden Layers
    * 25 Nodes per layer
    */
  val neuralNetwork = new NeuralNetwork(name = "BigDigit",all_data, labels, hiddenLayers = 2, nodesPerLayer = 25, func = "sigmoid")
  neuralNetwork.train(maxEpoch = 10, read_weights = true)
//  neuralNetwork.train(maxEpoch = 500)
  val yPred = neuralNetwork.predict(test)
  val yy: Seq[Double] = neuralNetwork.ls
  val x = (0 until 1).map(_.toDouble)
  // send x, yy to sky
  println("Accuracy: " + Utilities.accuracy(test_y, DenseVector(yPred:_*)))

}
