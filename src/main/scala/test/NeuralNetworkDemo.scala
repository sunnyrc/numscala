package test

import breeze.linalg.DenseVector
import nonlinear.NeuralNetwork
import utilities.Utilities
object NeuralNetworkDemo extends App{
  val (labels, all_data) = BigDigit.load()

  val (train, train_y, test, test_y) = Utilities.train_test_split(all_data,labels)

  // training set doesnt have one of each
//  println(train_y.toArray.toSet)

  /**
    * 2 Hidden Layers
    * 25 Nodes per layer
    */
  val neuralNetwork = new NeuralNetwork(all_data, labels, hiddenLayers = 2, nodesPerLayer = 25)
  neuralNetwork.train()
  val yPred = neuralNetwork.predict(all_data)
  println("Accuracy: " + Utilities.accuracy(labels, DenseVector(yPred:_*)))

}
