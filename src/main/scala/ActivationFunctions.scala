import breeze.generic._

object sigmoid extends UFunc with MappingUFunc{
  implicit object implDouble extends Impl [Double, Double]{
    def apply(x: Double) = 1/(1 + scala.math.exp(-x))
  }
}

object dSigmoid extends UFunc with MappingUFunc{
  implicit object implDouble extends Impl [Double, Double]{
    def apply(x: Double) = {
      val c = scala.math.exp(-x)
      c/((1+c) * (1 + c))
    }
  }
}

object tanh extends UFunc with MappingUFunc{
  implicit object implDouble extends Impl [Double, Double] {
    def apply(x: Double) = tanh(-x)
  }
}

object dtanh extends UFunc with MappingUFunc{
  implicit object implDouble extends Impl [Double, Double] {
    def apply(x: Double) = {
      val c = tanh(x)
      1 - x*x
    }
  }
}

object ReLU extends UFunc with MappingUFunc{
  implicit object implDouble extends Impl [Double, Double] {
    def apply(x: Double) = {
      if (x < 0) 0 else x
    }
  }
}

object dReLU extends UFunc with MappingUFunc{
  implicit object implDouble extends Impl [Double, Double] {
    def apply(x: Double) = {
      if (x < 0) 0 else 1
    }
  }
}
