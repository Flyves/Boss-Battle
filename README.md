# RlBot FlyveController
An abstraction layer of the RlBot framework for controlling rocket league bots, in Java. 

## The idea
The purpose of this project is to try to solve one of the biggest problems for rocket league bots:
What inputs do we need to provide in order to reach a specific physic state at a specified moment in time?

## How to use
There are two classes that you need to be aware of. The first one is ```DesiredCarState```, and the second one is ```BotController```. As the names suggest, ```DesiredCarState``` is for specifying a future desired state, and ```BotController``` is where the actual computations are done.
You can find examples of these classes in the folder dedicated to that. 
