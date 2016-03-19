# Graphics-App
A vector graphics drawing application using the AWT and Swing Java libraries. 
It was created as an excercise for basic computer graphics algorithms. 
As such, it is by no way optimized, and uses only the drawLine function of the Swing library in order to draw points.
Lines and circles are drawn using Bresenham's algorithm, and all other shapes use this implementation.
##Structure
The application is structured into two packages: 
  * shapes 
  * cgproject (the main package).
The Drawing system works by instantiating the selected shape class with the appropriate parameters,
and adding it into a list.
In each draw call of the Canvas, its Graphics context object is passed to every shape object on the list, 
calling their draw method.
