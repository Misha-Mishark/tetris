Tetris
======

This is my first complete Java Game using the awesome library named Slick.

Requirements
------------

This game basically requires only Java to run on your system and has only been tested on Windows.
I don't believe the game will work on other platforms.

How to play
-----------

To play the game, simply unzip the folder and open the JAR file.
Make sure all the files that came in the zip folder stay together or the game will not start.

Objectives
----------

For those who don't know the game (shame on you), the objective is simple:

- Stack the pieces on top of each other in a way to create full lines
- Once a line is full it will be removed from the "pit" and will gave you a score bonus
- Try to make combos: double, tripple or quadruple (also known as Tetris) for another score boost
- When you reach the score milestone you will level up
- As you advance in the levels the pieces will drop faster (i made the speed a bit reasonable for inexperienced players in the first levels but it DOES get fast)

In my implementation of the game (not sure if this is also in the offical game too):

- There is a multiplier that is, surprise, multiplied by each bonus you get
- The multiplier is at least the level number you are at, so if you are at level 2, the multiplier is at least 2
- Each time you get a combo (doubles or more), the multiplier is increased by one
- So if you keep getting combos in a row, the multiplier will boost your score !

Hint: You can see where the current piece will end up with the dark "ghost" piece that is shown in the pit. This "ghost" will disappear if the piece is near where it is supposed to land.

Controls
--------

- Left and right arrow keys will move the current piece to the left or to the right
- The up arrow key rotates the current piece
- The down arrow key slowly moves the piece down
- And pressing space will drop the piece

Credits
-------

- I have coded all the game by myself, i don't provide source code in the repository but if you need hints on how to build a tetris clone feel free to contact me.
- I also did all the graphics myself, except for the grey background which is a basic abstract image i found on google images.
- The sounds are also from this wonderful resources site: http://www.sounds-resource.com/ so the credits go to the original uploader 

Have fun! I hope you enjoy it!