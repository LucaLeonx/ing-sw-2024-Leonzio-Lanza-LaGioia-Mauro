Benvenuto su CodexNaturalis!

Scegli un'azione da fare
1. Login
2. Registrati

--- 
1  
Immetti nome utente: 
Immetti Password:
I

-----

2
immetti nome utente:
Immetti Password:
Conferma Password: 

------

- Così è molto complicato
ID      NOME       CREATORE   Partecipanti     GIOCATORI 
000101  Partita1    smaur      3/4              gio, luca, steve

- Meglio così

ID: 000101
NOME: Partita1
CREATORE: smaur
PARTECIPANTI: 3/4
GIOCATORI: gio, luca, steve

inserisci idPartita: 

----

CREA NUOVA PARTITA: 

Inserire Nome: 
Inserire Numero partecipanti richiesti: 

---

Attendi giocatori... 
Giocatori attuali 3/4:
    -gio
    -luca
    -checco

----

Partita iniziata

------------------------------------------------------------------------------
WE USED THE FOLLOWING EMOJI TO BUILD TUI: 

🍄: \uD83C\uDF44     FUNGI
🐺: \uD83D\uDC3A     ANIMAL
🦋: \uD83E\uDD8B     BUTTERFLY
🪴: \uD83C\uDF3F     PLANT
📝: \uD83D\uDCDD     MANUSCRIPT
🪶: \uD83E\uDEB6     QUILL
🫙: \uD83E\uDED9     INKWELL
⬜: \u2B1C           BLANK
🟥: \uD83D\uDFE5     BACKGROUND OR HIDDEN ANGLE OF A RED CARD
🟩: \uD83D\uDFE9     BACKGROUND OR HIDDEN ANGLE OF A GREEN CARD
🟦: \uD83D\uDFE6     BACKGROUND OR HIDDEN ANGLE OF A BLUE CARD
🟪: \uD83D\uDFEA     BACKGROUND OR HIDDEN ANGLE OF A PURPLE CARD
🟨: \uD83D\uDFE8     BACKGROUND OR HIDDEN ANGLE OF THE INITIAL CARD
⬛: \u2B1B           TABLE
🔵: \uD83D\uDD35     STARTING POINT OF A BLUE PLAYER
🟢: \uD83D\uDFE2     STARTING POINT OF A GREEN PLAYER
🟡: \uD83D\uDFE1     STARTING POINT OF A YELLOW PLAYER
🔴: \uD83D\uDD34     STARTING POINT OF A RED PLAYER

Every emoji alligned perfectly with the other. 
Test for correct alignment: 
🍄   🍄   🍄   🍄   🍄   🍄   🍄   🍄   🍄   🍄   🍄   🍄   🍄   🍄   🍄   🍄
🐺   🐺   🐺   🐺   🐺   🐺   🐺   🐺   🐺   🐺   🐺   🐺   🐺   🐺   🐺   🐺
🦋   🦋   🦋   🦋   🦋   🦋   🦋   🦋   🦋   🦋   🦋   🦋   🦋   🦋   🦋   🦋
🪴   🪴   🪴   🪴   🪴   🪴   🪴   🪴   🪴   🪴   🪴   🪴   🪴   🪴   🪴   🪴
📝   📝   📝   📝   📝   📝   📝   📝   📝   📝   📝   📝   📝   📝   📝   📝
🫙   🫙   🫙   🫙   🫙   🫙   🫙   🫙   🫙   🫙   🫙   🫙   🫙   🫙   🫙   🫙
⬛   ⬛   ⬛   ⬛   ⬛   ⬛   ⬛   ⬛   ⬛   ⬛   ⬛   ⬛   ⬛   ⬛   ⬛   ⬛
🔴   🔴   🔴   🔴   🔴   🔴   🔴   🔴   🔴   🔴   🔴   🔴   🔴   🔴   🔴   🔴


-----------------------------------------------------------------------------------------

Example of the front of a card:
⬜🟥📝
🟥🟥🟥
🍄🟥🍄

Example of the back of a card:
⬜🟥⬜
🟥🍄🟥
⬜🟥⬜

The initial cards are the only one that could potentially have more than one symbol in the middle
so instead of that symbol we cover it with a dot of the colour of the player and beneath we simply
write out what in the map would be underneath of it.

An example of a map of green player:
⬜🟥⬜⬛⬜🟥📝
🟥🍄🟥⬛🟥🟥🟥
⬜🟥⬜🟨🍄🟥🍄
⬛⬛🟨🟢🟨⬛⬛
⬛⬛🪴🟨⬜⬛⬛

🟢: 🍄🦋🪴

Example of an hand:
⬜🟥📝  ⬜🟩🟩  🟦🟦⬜
🟥🟥🟥  🟩🟩🟩  🟦🟦🟦
🍄🟥🍄  🪴🟩🪴  🫙🟦🟦
-----------------------------------------------------------------------------------------

final TUI:

your map:
⬜🟥⬜⬛⬜🟥📝
🟥🍄🟥⬛🟥🟥🟥
⬜🟥⬜🟨🍄🟥🍄
⬛⬛🟨🟢🟨⬛⬛
⬛⬛🪴🟨⬜⬛⬛

🟢: 🍄🦋🪴

Card to draw:

decks:
resource         gold
⬜🟥⬜          ⬜🟪⬜
🟥🍄🟥          🟪🦋🟪
⬜🟥⬜          ⬜🟪⬜

cards down:
resource         gold
⬜🟥📝          ⬜🟩🟩  
🟥🟥🟥          🟩🟩🟩  
🍄🟥🍄          🪴🟩🪴

🟦🟦⬜          ⬜🟪🟪  
🟦🟦🟦          🟪🟪🟪
🫙🟦🟦          🦋🟪🦋

common objectives:
- 3 green diagonal
- 1 block green and up right red


your hand:
⬜🟥📝  ⬜🟩🟩  🟦🟦⬜
🟥🟥🟥  🟩🟩🟩  🟦🟦🟦
🍄🟥🍄  🪴🟩🪴  🫙🟦🟦

secret goal:
- 3 quill

------------------------------------------------------------------------------------

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
to get to corresponding position of the matrix.  



Side-note: 
I've checked in my IDE and we don't have a max number of emoji before automatically changing line if we have a matrix.   
(it simply opens a scrollbar below). However the normal length of 80/120 chars should be more than enough to display all maps. 
If some problems arise we can always reduce the char size in the terminal.  

---------------------------------------------------------------------------------------------


