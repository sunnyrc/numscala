import breeze.generic.{MappingUFunc, UFunc}


object ReLU extends UFunc with MappingUFunc{

  def apply(x: Double) = {
    if (x < 0) 0 else x
  }
}
