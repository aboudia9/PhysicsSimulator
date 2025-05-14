# PhysicsSimulator
Physics Simulator for kinematics, electricity and magnetism

Dependencies:
  JavaFX library - 23.0.2 (or more recent version)
  JBox2D - 2.2.1.1 (or more recent version)

How it works:
  
  The physics simulator is equipped to simulate basic collisions, and uses gravity to exemplify
  concepts like momentum, applied forces, projectile motion, and Newton's second law.

  You can spawn objects into the canvas area and throw them around with mouse clicking and dragging. 
  The walls and ceiling are equipped with hit boxes, allowing the objects to stay within the canvas 
  area.

Hot Keys and Control Panel:
  
  'P' key - pause simulation
  space bar - pause/resume simulation
  'R' key - reset simulation

  1. Slider under gravity - changes the factor of gravity (in m/s/s)
      a) must select "modify gravity" to confirm changes
      b) reset gravity button to scale back to 9.8 m/s/s
  
  2. Slider under objects - changes the size of the object (in meters)
      a) slider must be scaled prior to spawning new object
      b) slider is centered around 1 m for objects initially being spawned
  
  3. Color Drop-down - changes object to one of four colors
      a) Blue, Green, Red, Orange
  
  4. Shape Drop-down - changes object to one of three shapes
      a) Circle, Square, Triangle
  
  5. 'Add Object' - adds selected shape of selected size to canvas

   **NOTE: gravity scaling affects all objects already on canvas
       not just the one currently being added.
  
