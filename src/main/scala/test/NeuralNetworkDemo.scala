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
    val neuralNetwork = new NeuralNetwork(name = "BigDigit", all_data, labels, hiddenLayers = 2, nodesPerLayer = 25)
      neuralNetwork.train(maxEpoch = 1, read_weights = true)
//    neuralNetwork.train(maxEpoch = 100)
    val yPred = neuralNetwork.predict(test)
    val yy: Seq[Double] = neuralNetwork.ls
    val x = yy.indices.map(_.toDouble)
    // send x, yy to sky
    println("Accuracy: " + Utilities.accuracy(test_y, DenseVector(yPred: _*)))

    (yy, Utilities.accuracy(test_y, DenseVector(yPred: _*)))
  }
}
