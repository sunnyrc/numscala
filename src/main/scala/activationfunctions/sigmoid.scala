package activationfunctions

import breeze.generic.{MappingUFunc, UFunc}

/**
  * Created by sanch on 28-Nov-17.
  */
object sigmoid extends UFunc with MappingUFunc{

  implicit object implDouble extends Impl [Double, Double]{
    def apply(x: Double) = 1/(1 + scala.math.exp(-x))
  }
}
