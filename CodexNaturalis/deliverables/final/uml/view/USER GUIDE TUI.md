**USER GUIDE TUI** 

WE USED THE FOLLOWING EMOJI TO BUILD TUI:

🍄: \uD83C\uDF44     FUNGI
🐺: \uD83D\uDC3A     ANIMAL
🦋: \uD83E\uDD8B     BUTTERFLY
🪴: \uD83C\uDF3F     PLANT
📝: \uD83D\uDCDD     MANUSCRIPT
🪶: \uD83E\uDEB6     QUILL
🫙: \uD83E\uDED9     INKWELL
⚪: \u2B1C           BLANK
🟥: \uD83D\uDFE5     BACKGROUND OR HIDDEN ANGLE OF A RED CARD
🟩: \uD83D\uDFE9     BACKGROUND OR HIDDEN ANGLE OF A GREEN CARD
🟦: \uD83D\uDFE6     BACKGROUND OR HIDDEN ANGLE OF A BLUE CARD
🟪: \uD83D\uDFEA     BACKGROUND OR HIDDEN ANGLE OF A PURPLE CARD
🟫: \uD83D\uDFEB      BACKGROUND OR HIDDEN ANGLE OF THE INITIAL CARD
🟨: \uD83D\uDFE8     SPECIAL COLOR FOR GOLDEN CARD IN THE DECK
⚫: \u2B1B           TABLE AND BACKGROUND OF OBJECTIVE CARD
🔵: \uD83D\uDD35     STARTING POINT OF A BLUE PLAYER
🟢: \uD83D\uDFE2     STARTING POINT OF A GREEN PLAYER
🟡: \uD83D\uDFE1     STARTING POINT OF A YELLOW PLAYER
🔴: \uD83D\uDD34     STARTING POINT OF A RED PLAYER

◰ : \u25F0            SYMBOL TO SIGNAL THE FACT YOU ARE GETTING POINT BASED ON ANGLES COVERED
❕:\u2755            SYMBOL USED TO SAY FOR EACH " | " IN A GOLD CARD.

-----------------------------------------------------------------------------------------

Example of the front of a card in play:
⚪🟥📝
🟥🟥🟥
🍄🟥🍄

Example of the back of a card in play:
⚪🟥⚪
🟥🍄🟥
⚪🟥⚪

The initial cards are the only one that could potentially have more than one symbol in the middle
so instead of that symbol we cover it with a dot of the colour of the player and beneath we simply
write out what in the map would be underneath of it.

An example of a map of green player:
⚪🟥⚪⚫⚪🟥📝
🟥🍄🟥⚫🟥🟥🟥
⚪🟥⚪🟫🍄🟥🍄
⚫⚫🟫🟢🟫⚫⚫
⚫⚫🪴🟫⚪⚫⚫

🟢: 🍄🦋🪴


For the hand and the decks we need to be a little bit more specific. In particular while when the card are played we are 
only interested in the symbol that are displayed when they are in hand we would also need to know if that card gives point
and which requirement it needs to satisfy. Of course it is impossible to display all this information in a 3*3 grid so we 
opted to have a bigger image for the hand and the card that are played opting for a 5*3 grid. 

Examples of a card in hand or to draw:

normal example
⚪🟪3 🟪📝
🟪🟪🟪🟪🟪
🟪🦋🦋🦋🟪
example of card 78. the three butterfly below represents the recquirements. on the angle instead the angle symbol we want

example with max number of symbol as requirements
⚪🟥5 🟥⚪
🟥🍄🍄🟥🟥
🟥🍄🍄🍄🟥
example of card 50. as you can see the recquirements are always in the bottom of the card. This admittedly could create some confusion with
the angle symbol below but enlarging even more the cards for us was not the best solution. 

example with conditional points (1 points for every quill) 
⚪ 1❕🪶🟦
🟦🟦🟦🟦🟦
🪶🐺🐺🍄⚪
card 63


example with conditional point (2 points for every angle that was covered) {small problem of alignment}
⚪ 2❕◰ 🟩
🟩🟩🪴🟩🟩
⚪🪴🪴🍄⚪
card 56




example of back and front of a resource card
⚪🟥🟥🟥🟥
🟥🟥🟥🟥🟥
🍄🟥🟥🟥🍄
img card id 3

⚪🟥🟥🟥⚪
🟥🟥🍄🟥🟥
⚪🟥🟥🟥⚪

example of objective cards:
⚫⚫⚫⚫ 3
⚫🪶📝🫙⚫
⚫⚫⚫⚫⚫
example of card 99

⚫⚫🟥⚫ 3
⚫⚫🟥⚫⚫         
⚫⚫⚫🟩⚫   
example of card 91

⚫🟪⚫⚫ 2
⚫⚫🟪⚫⚫
⚫⚫⚫🟪⚫
example of card 90

⚫⚫⚫🟥 2
⚫⚫🟥⚫⚫
⚫🟥⚫⚫⚫
example of card 87



-----------------------------------------------------------------------------------------

final TUI:

your map:
⚪🟥⚪⚫⚪🟥📝
🟥🍄🟥⚫🟥🟥🟥
⚪🟥⚪🟨🍄🟥🍄
⚫⚫🟨🟢🟨⚫⚫
⚫⚫🪴🟨⚪⚫⚫

🟢: 🍄🦋🪴

Card to draw:

decks :
resource            gold
⚪🟥🟥🟥⚪         ⚪🟪🟨🟪⚪
🟥🟥🍄🟥🟥         🟪🟨🦋🟨🟪
⚪🟥🟥🟥⚪         ⚪🟪🟨🟪⚪

cards on the table:
          
⚪🟥🟥🟥📝         ⚪🟪3 🟪📝
🟥🟥🟥🟥🟥         🟪🟪🟪🟪🟪  
🍄🟥🟥🟥🍄         🟪🦋🦋🦋🟪

⚪🟦1 🟦⚪         ⚪ 2❕◰ 🟦 
🟦🟦🟦🟦🟦         🟦🟦🟦🟦🟦
🫙🟦🟦🟦🟦         🪶🐺🐺🍄⚪

common objectives:
⚫⚫🟥⚫ 3         ⚫🟪⚫⚫ 2
⚫⚫🟥⚫⚫         ⚫⚫🟪⚫⚫
⚫⚫⚫🟩⚫         ⚫⚫⚫🟪⚫


your hand:
⚪🟥 5🟥⚪         ⚪🟩🟩🟩🟩         🟪🟪🟪🟪⚪         ⚫⚫⚫⚫ 3 
🟥🍄🍄🟥🟥         🟩🟩🟩🟩🟩         🟪🟪🟪🟪🟪         ⚫🪶📝🫙⚫
🟥🍄🍄🍄🟥         🪴🟩🟩🟩🪴         🦋🟪🟪🟪🦋         ⚫⚫⚫⚫⚫


------------------------------------------------------------------------------------


Every emoji alligned perfectly with the other.
Test for correct alignment:
🍄   🍄   🍄   🍄   🍄   🍄   🍄   🍄   🍄   🍄   🍄   🍄   🍄   🍄   🍄   🍄
🐺   🐺   🐺   🐺   🐺   🐺   🐺   🐺   🐺   🐺   🐺   🐺   🐺   🐺   🐺   🐺
🦋   🦋   🦋   🦋   🦋   🦋   🦋   🦋   🦋   🦋   🦋   🦋   🦋   🦋   🦋   🦋
🪴   🪴   🪴   🪴   🪴   🪴   🪴   🪴   🪴   🪴   🪴   🪴   🪴   🪴   🪴   🪴
📝   📝   📝   📝   📝   📝   📝   📝   📝   📝   📝   📝   📝   📝   📝   📝
🫙   🫙   🫙   🫙   🫙   🫙   🫙   🫙   🫙   🫙   🫙   🫙   🫙   🫙   🫙   🫙
⚫   ⚫   ⚫   ⚫   ⚫   ⚫   ⚫   ⚫   ⚫   ⚫   ⚫   ⚫   ⚫   ⚫   ⚫   ⚫
🔴   🔴   🔴   🔴   🔴   🔴   🔴   🔴   🔴   🔴   🔴   🔴   🔴   🔴   🔴   🔴


problem: following symbol aligns poorly. it could be still fine I guess since it would not be on the map.
So the disallignment is not too much of a problem
◰   ◰   ◰   ◰   ◰   ◰   ◰   ◰   ◰   ◰   ◰   ◰   ◰   ◰   ◰   ◰   ◰   ◰   

**moreover the number in the intellij Terminal doesn't occupy exactly half of the space occupied by an emoji. 
Since in a real terminal this is the case (unimodal character) we didn't bother about this little issue.** 

