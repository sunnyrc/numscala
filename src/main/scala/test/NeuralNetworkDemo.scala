package test

import breeze.linalg.DenseVector
import nonlinear.NeuralNetwork
import utilities.Utilities
class NeuralNetworkDemo extends App{

  def getAxis: (Seq[Double], Double) = {
    val (labels, all_data) = BigDigit.load()
    val (train, train_y, test, test_y) = Utilities.train_test_split(all_data, labels)


    /**
      * 2 Hidden Layers
      * 25 Nodes per layer
      */
    // Remove the name when training a new batch please!
    val neuralNetwork = new NeuralNetwork(name = "BigDigit", all_data, labels, hiddenLayers = 2, nodesPerLayer = 25)
    // Comment this line out to start training a new model
      neuralNetwork.train(maxEpoch = 1, read_weights = true)
//    neuralNetwork.train(maxEpoch = 100, learning_rate = 0.9)
    val yPred = neuralNetwork.predict(test)
    val yy: Seq[Double] = neuralNetwork.ls

    (yy, Utilities.accuracy(test_y, DenseVector(yPred: _*)))
  }
}
