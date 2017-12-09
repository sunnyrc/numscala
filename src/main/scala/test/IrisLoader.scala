package test
import breeze.linalg.{DenseMatrix, DenseVector}
import scala.io.Source
import scala.util.Try

object IrisLoader {

    val featureNames: (String, String, String, String) = ("sepal length (cm)",
                                                          "sepal width (cm)",
                                                          "petal length (cm)",
                                                          "petal width (cm)")

    def getDouble(v: => String, default: Double = .0): Double = Try(v.toDouble).getOrElse(default)

    def getString(v: => String, default: String = "NA"): String = Try(v).getOrElse(default)

    /**
      * Load file
      * @return
      */
    def load(): (DenseVector[String], DenseMatrix[Double])  = {
        val src = Source.fromURL(getClass.getResource("iris.csv"))

        val iter = src.getLines().map(_.split(","))

        val features = iter
                        .map(l => (getString(l(4)), (getDouble(l(0)), getDouble(l(1)), getDouble(l(2)), getDouble(l(3)))))
                        .toList

        (DenseVector(features.map(_._1):_*), DenseMatrix(features.map(_._2):_*))
    }
}
