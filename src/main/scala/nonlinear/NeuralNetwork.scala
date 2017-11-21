package nonlinear

import breeze.generic.{MappingUFunc, UFunc}
import breeze.linalg.{Axis, DenseMatrix, DenseVector, sum}
import breeze.numerics.{log, pow}
import breeze.stats.distributions.Rand

class NeuralNetwork(train: DenseMatrix[Double], labels: DenseVector[Int], hiddenLayers: Int, nodesPerLayer: Int, e: Double = 1e-5, func: String = "sigmoid") {

  val inputSize: Int = train.cols
  val outputSize: Int = labels.length
  val y: DenseMatrix[Double] = binarizeData(labels)

  // Pad the training data (Thanks Aj Piti)
  val trainPadded: DenseMatrix[Double] = DenseMatrix.horzcat(DenseMatrix.ones[Double](train.rows, 1), train)

  /*
  To check gradients but don't think we need it
   */
  //  val LAYER_CHK = 0
  //  val NUM_NODES_CHK = 3
  //  val NUM_WEIGHTS_CHK = 60
  //  val epsilon = 1e-6

  var weights: Array[DenseMatrix[Double]] = (0 to hiddenLayers).map(initLayers).toArray

  object sigmoid extends UFunc with MappingUFunc {
    implicit object implDouble extends Impl [Double, Double]{
      def apply(x: Double): Double = 1/(1 + scala.math.exp(-x))
    }
  }

  object ReLU extends UFunc with MappingUFunc{
    implicit object implDouble extends Impl [Double, Double] {
      def apply(x: Double) = {
        if (x < 0) 0 else x
      }
    }
  }

  // Initialize all the layers
  // Layer 0 being the first hidden layer so we use inputSize
  // Last layer needs to match the output size (label Set)
  // Any other layer we use the same nodesPerLayer + 1
  def initLayers(n: Int): DenseMatrix[Double] = n match {
    case 0 => DenseMatrix.rand(nodesPerLayer, inputSize + 1, rand = Rand.gaussian)
    case `hiddenLayers` => DenseMatrix.rand(outputSize, nodesPerLayer + 1, rand = Rand.gaussian)
    case _ => DenseMatrix.rand(nodesPerLayer, nodesPerLayer + 1, rand = Rand.gaussian)
  }

  def binarizeData(xs: DenseVector[Int]): DenseMatrix[Double] = {
    val ys = DenseMatrix.zeros[Double](xs.length, outputSize)
    xs.data.zipWithIndex.foreach {
      case (x, i) => ys.update(i, x, 1.0) // try changing to x - 1
    }
    ys
  }

//  def chooseFunction ={
//    if (func == "sigmoid") _ => sigmoid(_)
//    else if (func == "relu") ReLU
//  }

  // Log Loss function
  def loss(y: DenseVector[Double], yPred: DenseVector[Double]): DenseVector[Double] = {
    (-y :* log(yPred)) :- ((-y + 1.0) :* log(-yPred + 1.0))
  }

  def h(x: DenseVector[Double], thetas: Array[DenseMatrix[Double]], vecs: List[DenseVector[Double]] = List()): (DenseVector[Double], List[DenseVector[Double]])= {
    if(thetas.isEmpty) (x(1 to -1), vecs)
    else {
      val a = sigmoid(thetas.head * x).toArray
      h(DenseVector(1.0 +: a), thetas.tail, vecs :+ x(1 to -1))
    }
  }

  def h(x: DenseMatrix[Double], thetas: Array[DenseMatrix[Double]]): DenseMatrix[Double]= {
    if(thetas.isEmpty) x(1 to -1, ::).t
    else {
      var a: DenseMatrix[Double] =
        if(thetas.length == weights.length) sigmoid(thetas.head * x.t)
      else {
        sigmoid(thetas.head * x)
      }
      a = DenseMatrix.vertcat[Double](DenseMatrix.ones[Double](1, a.cols), a)
      h(a, thetas.tail)
    }
  }

  def cost(x: DenseMatrix[Double], thetas: Array[DenseMatrix[Double]], y: DenseMatrix[Double]): Double = {
    val j = (1.0/x.rows) * sum(
      for {i <- 0 until  x.rows - 1
           yPred = h(x(i, ::).inner, thetas)._1
           yi = y(i, ::).inner
      }
        yield sum(loss(yi, yPred)))
    j
  }

}