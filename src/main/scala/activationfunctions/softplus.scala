package activationfunctions

import breeze.generic.{MappingUFunc, UFunc}
import breeze.numerics.{exp, log}

object softplus extends UFunc with MappingUFunc{
  implicit object implDouble extends Impl [Double, Double] {
    def apply(a:Double) = log(1+exp(a))
  }
}
