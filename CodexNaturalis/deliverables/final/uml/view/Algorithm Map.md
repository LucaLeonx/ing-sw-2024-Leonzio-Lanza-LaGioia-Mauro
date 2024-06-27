Algorithm in a nutshell to build the map:
1) first count all the rows and column and create a matrix of strings with that many column and row
2) initialize the matrix with all black square
3) From the map of the card color the up down, left and right cells of the color of the card
4) add the symbol in the corresponding coordinate in the matrix.
   (in case we get an hidden angle we should get to the top card to color properly)
5) We add the starting symbol in (0,0) of the color of the player.
6) we print out the Map Matrix
7) We print out what's underneath the starting symbol.

PS: The cells in our model translate 1 to 1 to the cell in the TUI.
Obviously there is going to be a displacement because we need to keep track of the fact that we have to map the position minX
and minY to the (0,0) position of the matrix. in order to do that we simply add abs(minX) to the x of the map and abs(minY) to the Y
to get to corresponding position of the matrix.  (of course after having updated the min and the Max to the center of the card not the edge)

PS2: Position number (8,8) in our matrix should be up right while in a matrix of course would be  down right. to fix this we need to put a minus
symbol each time we consider the y of the matrix. In order to fix this we start reading not from the top to the bottom but bottom up.


Side-note:
I've checked in my IDE and we don't have a max number of emoji before automatically changing line if we have a matrix.   
(it simply opens a scrollbar below). However the normal length of 80/120 chars should be more than enough to display all maps.
If some problems arise we can always reduce the char size in the terminal.  Algorithm in a nutshell to build the map:
