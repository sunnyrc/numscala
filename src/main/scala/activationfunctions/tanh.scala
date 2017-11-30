package activationfunctions

import breeze.generic.{MappingUFunc, UFunc}

/**
  * Created by sanch on 28-Nov-17.
  */
object tanh extends UFunc with MappingUFunc{
  implicit object implDouble extends Impl [Double, Double] {
    def apply(x: Double): Double = (2.0 / (1 + math.exp(-(2*x)))) - 1
  }
}