# Model View Controller


## Controller



## View

- Mostrare le info dei giocatori (nickname, score, colore)
- Mostra la mano del giocatore
- Mostrare menu di scelta delle carte (es. scelta carta da giocare)
- Mostrare le mappe di gioco (GameField)
- Mostra colore e carte da pescare 
- Mostra turno (chi è il giocatore che sta giocando)



## Model

- Turno di gioco
- Giocatore corrente
- Fase di gioco
  - Pattern State:
  
  Stato
  - Setup Game (
    - setup deck, 
    - carte in gioco:
    - scelta ordine
      - obbiettivi comuni 
      - carte oro 
      - carte risorsa 
  )
  - Setup Player che volendo può essere raggruppato con il setupGame in futuro ( 
    - carte in mano, 
    - scelta obbiettivo segreto,
    - scelta lato della carta iniziale potremmo usare due bool per capire se entrambe le scelte sono state fatte )
      sulla carta obbiettivo e sul side della carta iniziale. )
  - Midgame (diviso in gioco carta, valuto punti, pesco carta)
  - PlayLastTurn (diviso in gioco carta, valuto punti, pesco carta)
  - EvaluateScore (
    - Conta i punti derivanti dalle carte obbiettivo (per ogni giocatore)
    - Decreta il vincitore
  )


  Transizioni:
  - Inizio --> Setup
  - Setup Player --> Setup Player, se ci sono ancora giocatori per cui farlo
  - Setup Player --> Midgame
  - Quando abbiamo finito un turno e siamo arrivati alla fine 
    della lista di player verifico se un player ha totalizzato più di 20 pt 
    o se tutti i deck sono finiti e in caso passo allo stato PlayLastTurn
  - 
