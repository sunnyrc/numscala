# Numscala
The aim was to make an easy to access machine learning library for Scala similar to scikit.

To Run
```
Run Main class in java/numscala/ui/Main.java
```

There are two sections to numscala. One is written in Scala and the other in Java for demo purposes.

- Scala
  - Activation Functions - Contains sigmoid, softplus and tanh function.
  - Linear Models - Houses Linear and Logistic Regression.
  - NonLinear Models - Houses Perceptron and Neural Network.
  - Test - Houses all the datasets, their respective loaders and the Demo classes for each model.
  - Utilities - Contains one file which consists of functions like Accuracy, Log Loss and Splitting the Training and Testing set. 
  
- Java
  - Scala Intepreter - Contains service to convert Seq<Object> and Object from invoking Scala functions to Java's List<Double> and Double
  - MenuUI - Handles selection of model to train
  - NonGraphableUI - Handles GUI for Linear Regression, this model can't produce a graph
  - NonLinearModelUI - Handles GUI for Logistic Regression, Perceptron and Neural Network
  - View mode - Allows for more ease in observing a graph
  - Hover Mode - Allows to navigate the graph at the cost of it looking funky (The number of data points is reduced by a factor of 2 to counteract this problem)
