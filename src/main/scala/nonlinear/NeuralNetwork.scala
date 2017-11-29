package nonlinear

import java.io.File

import activationfunctions.{sigmoid, softplus, tanh}
import breeze.linalg.{Axis, DenseMatrix, DenseVector, csvread, csvwrite, max, sum}
import breeze.numerics.{log, pow}
import breeze.stats.distributions.Rand

import scala.collection.mutable.ListBuffer

class NeuralNetwork(name: String = "test",train: DenseMatrix[Double], labels: DenseVector[Int], hiddenLayers: Int, nodesPerLayer: Int, func: String = "sigmoid" ,e: Double = 1e-5) {

  val inputSize: Int = train.cols
  val outputSize: Int = labels.toArray.toSet.size
  val y: DenseMatrix[Double] = binarizeData(labels)

  var ls = new ListBuffer[Double]()

  // Pad the training data (Thanks Aj Piti)
  val trainPadded: DenseMatrix[Double] = DenseMatrix.horzcat(DenseMatrix.ones[Double](train.rows, 1), train)

  var weights: Array[DenseMatrix[Double]] = (0 to hiddenLayers).map(initLayers).toArray

  // Initialize all the layers
  // Layer 0 being the first hidden layer so we use inputSize
  // Last layer needs to match the output size (label Set)
  // Any other layer we use the same nodesPerLayer + 1
  def initLayers(n: Int): DenseMatrix[Double] = n match {
    case 0 => DenseMatrix.rand(nodesPerLayer, inputSize + 1, rand = Rand.gaussian)
    case `hiddenLayers` => DenseMatrix.rand(outputSize, nodesPerLayer + 1, rand = Rand.gaussian)
    case _ => DenseMatrix.rand(nodesPerLayer, nodesPerLayer + 1, rand = Rand.gaussian)
  }

  // Put one at the label column in that row
  def binarizeData(xs: DenseVector[Int]): DenseMatrix[Double] = {
    val ys = DenseMatrix.zeros[Double](xs.length, outputSize)
    xs.data.zipWithIndex.foreach {
      case (x, i) => ys.update(i, x - 1, 1.0)
    }
    ys
  }


  def chooseFunction(x: DenseVector[Double]): DenseVector[Double] = {
    if (func == "sigmoid") sigmoid(x)
    else if (func == "softplus") softplus(x)
    else if (func == "tanh") tanh(x)
    else throw new Exception
  }

  def chooseFunction(x: DenseMatrix[Double]): DenseMatrix[Double] = {
    if (func == "sigmoid") sigmoid(x)
    else if (func == "softplus") softplus(x)
    else if (func == "tanh") tanh(x)
    else throw new Exception
  }

  // Log Loss function
  def loss(y: DenseVector[Double], yPred: DenseVector[Double]): DenseVector[Double] = {
    (-y :* log(yPred)) :- ((-y + 1.0) :* log(-yPred + 1.0))
  }

  // Passing the layers through activation function
  def thetha(x: DenseVector[Double], thetas: Array[DenseMatrix[Double]], vecs: List[DenseVector[Double]] = List()): (DenseVector[Double], List[DenseVector[Double]]) = {
    if (thetas.isEmpty) (x(1 to -1), vecs)
    else {
      val a: Array[Double] = chooseFunction(thetas.head * x).toArray
      thetha(DenseVector(1.0 +: a), thetas.tail, vecs :+ x(1 to -1))
    }
  }

  // Passing the layers through activation function
  def thetha(x: DenseMatrix[Double], thetas: Array[DenseMatrix[Double]]): DenseMatrix[Double] = {
    if (thetas.isEmpty) x(1 to -1, ::).t
    else {
      var pad: DenseMatrix[Double] =
        if (thetas.length == weights.length) {
          chooseFunction(thetas.head * x.t)
        }
        else {
          chooseFunction(thetas.head * x)
        }
      pad = DenseMatrix.vertcat[Double](DenseMatrix.ones[Double](1, pad.cols), pad)
      thetha(pad, thetas.tail)
    }
  }

  def makeZeroMatrix(matrix: Array[DenseMatrix[Double]]): Array[DenseMatrix[Double]] = {
    matrix.map(m => DenseMatrix.zeros[Double](m.rows, m.cols))
  }

  def cost(x: DenseMatrix[Double], thetas: Array[DenseMatrix[Double]], y: DenseMatrix[Double], regularize: Boolean = true, lambda: Double = 0.8): Double = {
    val actualCost = (1.0 / x.rows) * sum(
      for {i <- 0 until x.rows - 1
           yPred = thetha(x(i, ::).inner, thetas)._1
           yi = y(i, ::).inner
      }
        yield sum(loss(yi, yPred)))

    if (regularize) {
      actualCost + thetas.map(t => sum(sum(pow(t(::, 1 to -1), 2), Axis._0))).sum * (lambda / (2.0 * x.rows))
    }
    else actualCost
  }

  def sigmoidGrad(x: DenseVector[Double]): DenseVector[Double] = x :* (-x + 1.0)

  def backpropagate(train: DenseMatrix[Double], xs: DenseMatrix[Double], weights: Array[DenseMatrix[Double]]): Array[DenseMatrix[Double]] = {

    val mat = makeZeroMatrix(weights)
    mat.foreach(f => f(::, 0) := 1.0)

    for (i <- 0 until train.rows) {

      // get actual predictions
      var (pred, vecs) = thetha(train(i, ::).inner, weights)
      var diff = pred - xs(i, ::).inner

      // reverse loop
      for (j <- weights.length - 1 to 0 by -1) {
        pred = vecs(j)

        // Formula
        mat(j)(::, 0) := mat(j)(::, 0) :* diff
        mat(j)(::, 1 to -1) := mat(j)(::, 1 to -1) + (diff * pred.t)

        diff = (weights(j).t(1 to -1, ::) * diff) :* sigmoidGrad(pred)
      }
    }
    mat.map(_ :/ train.rows.toDouble)
  }

  def train(learning_rate: Double = 0.8, maxEpoch: Int = 1000, lambda: Double = 0.05, read_weights: Boolean = false): Unit = {

    if (read_weights) weights = readWeights(name)

    var cost_ = cost(trainPadded, weights, y, lambda = lambda)

    var epoch = 0

    println(s"Cost at the start: $cost_")
    while (epoch < maxEpoch) {
      val mat = backpropagate(trainPadded, y, weights)

      ls+=cost_

      for (l <- 0 until weights.length - 1) {
        weights(l)(::, 0) := mat(l)(::, 0)
        weights(l)(::, 1 to -1) := weights(l)(::, 1 to -1) - mat(l)(::, 1 to -1) * learning_rate
      }


      cost_ = cost(trainPadded, weights, y, lambda = lambda)
      println(s"cost $cost_ at epoch $epoch")

      if (epoch % 100 == 0) {
        val y: List[Int] = predict(train)
        println(s"Accuracy at epoch $epoch: " + utilities.Utilities.accuracy(labels, DenseVector(y: _*)))
        saveWeights(weights)
      }
      epoch += 1
    }

    saveWeights(weights)
  }

  def saveWeights(weights: Array[DenseMatrix[Double]]): Unit ={
    weights.zipWithIndex.foreach{
      case (ws, i) =>  csvwrite(new File(s"C:/Users/Administrator/IdeaProjects/numscala/src/main/scala/test/models/$name-theta${i+1}.csv"), ws)
    }
  }

  def readWeights(name: String): Array[DenseMatrix[Double]] ={
    (0 to hiddenLayers).map(i => csvread(new File(s"C:/Users/Administrator/IdeaProjects/numscala/src/main/scala/test//models/$name-theta${i+1}.csv"))).toArray
  }


  def predict(test: DenseMatrix[Double]): List[Int] = {
    // pad the test
    val paddedTest = DenseMatrix.horzcat(DenseMatrix.ones[Double](test.rows, 1), test)
    val yPred = thetha(paddedTest, weights)

    val predictions =
      for {i <- 0 until yPred.rows
           each = yPred(i, ::).inner
      } yield each.findAll(_ == max(each)).head

    predictions.map(_ + 1).toList
  }

}