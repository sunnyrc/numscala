//package nonlinear
//
//import breeze.generic.{MappingUFunc, UFunc}
//import breeze.linalg.{Axis, DenseMatrix, DenseVector, max, sum}
//import breeze.numerics.{log, pow}
//import breeze.stats.distributions.Rand
//
//class NeuralNetwork(train: DenseMatrix[Double], labels: DenseVector[Int], hiddenLayers: Int, nodesPerLayer: Int, e: Double = 1e-5, func: String = "sigmoid") {
//
//  val inputSize: Int = train.cols
//  val outputSize: Int = labels.length
//  val y: DenseMatrix[Double] = binarizeData(labels)
//
//  // Pad the training data (Thanks Aj Piti)
//  val trainPadded: DenseMatrix[Double] = DenseMatrix.horzcat(DenseMatrix.ones[Double](train.rows, 1), train)
//
//  /*
//  To check gradients but don't think we need it
//   */
//  //  val LAYER_CHK = 0
//  //  val NUM_NODES_CHK = 3
//  //  val NUM_WEIGHTS_CHK = 60
//  //  val epsilon = 1e-6
//
//  var weights: Array[DenseMatrix[Double]] = (0 to hiddenLayers).map(initLayers).toArray
//
//  object sigmoid extends UFunc with MappingUFunc {
//    implicit object implDouble extends Impl [Double, Double]{
//      def apply(x: Double): Double = 1/(1 + scala.math.exp(-x))
//    }
//  }
//
//  object ReLU extends UFunc with MappingUFunc{
//    implicit object implDouble extends Impl [Double, Double] {
//      def apply(x: Double) = {
//        if (x < 0) 0 else x
//      }
//    }
//  }
//
//  // Initialize all the layers
//  // Layer 0 being the first hidden layer so we use inputSize
//  // Last layer needs to match the output size (label Set)
//  // Any other layer we use the same nodesPerLayer + 1
//  def initLayers(n: Int): DenseMatrix[Double] = n match {
//    case 0 => DenseMatrix.rand(nodesPerLayer, inputSize + 1, rand = Rand.gaussian)
//    case `hiddenLayers` => DenseMatrix.rand(outputSize, nodesPerLayer + 1, rand = Rand.gaussian)
//    case _ => DenseMatrix.rand(nodesPerLayer, nodesPerLayer + 1, rand = Rand.gaussian)
//  }
//
//  def binarizeData(xs: DenseVector[Int]): DenseMatrix[Double] = {
//    val ys = DenseMatrix.zeros[Double](xs.length, outputSize)
//    xs.data.zipWithIndex.foreach {
//      case (x, i) => ys.update(i, x, 1.0) // try changing to x - 1
//    }
//    ys
//  }
//
//  def chooseFunction(x: Double) ={
//    if (func == "sigmoid") sigmoid(x)
//    else if (func == "relu") ReLU(x)
//  }
//
//  // Log Loss function
//  def loss(y: DenseVector[Double], yPred: DenseVector[Double]): DenseVector[Double] = {
//    (-y :* log(yPred)) :- ((-y + 1.0) :* log(-yPred + 1.0))
//  }
//
//  def h(x: DenseVector[Double], weights: Array[DenseMatrix[Double]], vecs: List[DenseVector[Double]] = List()): (DenseVector[Double], List[DenseVector[Double]])= {
//    if(weights.isEmpty) (x(1 to -1), vecs)
//    else {
//      val a = sigmoid(weights.head * x).toArray
//      h(DenseVector(1.0 +: a), weights.tail, vecs :+ x(1 to -1))
//    }
//  }
//
//  def h(x: DenseMatrix[Double], weights: Array[DenseMatrix[Double]]): DenseMatrix[Double]= {
//    if(weights.isEmpty) x(1 to -1, ::).t
//    else {
//      var a: DenseMatrix[Double] =
//        if(weights.length == weights.length) sigmoid(weights.head * x.t)
//      else {
//        sigmoid(weights.head * x)
//      }
//      a = DenseMatrix.vertcat[Double](DenseMatrix.ones[Double](1, a.cols), a)
//      h(a, weights.tail)
//    }
//  }
//  def makeZeroMatrix(matrix: Array[DenseMatrix[Double]]): Array[DenseMatrix[Double]] ={
//    matrix.map(m => DenseMatrix.zeros[Double](m.rows, m.cols))
//  }
//
//  def cost(x: DenseMatrix[Double], thetas: Array[DenseMatrix[Double]], y: DenseMatrix[Double]): Double = {
//    val actualCost = (1.0/x.rows) * sum(
//      for {i <- 0 until  x.rows - 1
//           yPred = h(x(i, ::).inner, thetas)._1
//           yi = y(i, ::).inner
//      }
//        yield sum(loss(yi, yPred)))
//    actualCost
//  }
//
//  def sigmoidGrad(pred: DenseVector[Double]): DenseVector[Double] = pred :* (-pred + 1.0)
//
//  def backpropagate(train: DenseMatrix[Double], xs: DenseMatrix[Double], weights: Array[DenseMatrix[Double]]): Array[DenseMatrix[Double]] = {
//
//    val mat = makeZeroMatrix(weights)
//    mat.foreach(f => f(::,0) := 1.0)
//
//    for (i <- 0 until train.rows){
//      // get actual predictions
//      var (pred, vecs) = h(train(i, ::).inner, weights)
//      var diff = pred - xs(i, ::).inner
//
//      // reverse loop
//      for (j <- weights.length - 1 to 0 by - 1){
//        pred = vecs(j)
//
//        // Formula
//        mat(j)(::, 0) := mat(j)(::,0) :* diff
//        mat(j)(::, 1 to -1) := mat(j)(::, 1 to - 1) + (diff * pred.t)
//
//        diff = (weights(j).t(1 to -1, ::) * diff) :* sigmoidGrad(pred)
//      }
//    }
//    mat.map(_ :/ train.rows.toDouble)
//  }
//
//  def train(learning_rate: Double = 0.1, maxEpoch: Int = 1000) = {
//    var cost = cost(trainPadded, weights, y)
//
//    var epoch = 0
//
//    println(s"Cost at the start: $cost")
//    while (epoch < maxEpoch){
//      val mat = backpropagate(trainPadded, y, weights)
//
//
//      for (l <- 0 until weights.length - 1) {
//        weights(l)(::, 0) := mat(l)(::, 0)
//        weights(l)(::, 1 to -1) := weights(l)(::, 1 to -1) - mat(l)(::, 1 to -1) * learning_rate
//      }
//
//      cost = cost(trainPadded, weights, y)
//      println(s"cost $cost at epoch $epoch")
//
//      if (epoch % 100 == 0){
//        val yPred = predict(train)
//        println(s"Accuracy at epoch $epoch: " + utilities.Utilities.accuracy(labels, DenseVector(yPred:_*)))
//      }
//      epoch+=1
//    }
//
//    def predict(test: DenseMatrix[Double]): List[Int] = {
//      // pad the test
//      val paddedTest = DenseMatrix.horzcat(DenseMatrix.ones(test.rows, 1), test)
//      val yPred = h(paddedTest, weights)
//
//      val predictions =
//        for { i <- 0 until yPred.rows
//        each = yPred(i, ::).inner
//        } yield  each.findAll(_ == max(each)).head
//
//      predictions.map(_ + 1).toList
//    }
//
//  }
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//}