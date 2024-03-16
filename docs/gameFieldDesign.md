# Proposta di design per la classe Gamefield

Come aveva proposto Giovanni,
è possibile rappresentare le carte come tasselli
in una matrice. In questo modo, risulta semplice determinare la posizione
di carte ed angoli e muoversi tra di essi.


|   |    |   |    |   |
|---|----|---|----|---|
| a |    | b |    |   |
|   | C1 |   |    |   |
| d |    | c |    | e |
|   |    |   | C2 |   |
|   |    | g |    | f |
|   |    |   |    |   |
|   |    |   |    |   |


Memorizzare una matrice dinamica per contenere tutte le carte è 
complicate e computazionalmente dispendioso. Si può però procedere
in un altro modo: è sufficiente memorizzare solo gli
elementi presenti nella matrice, identificandoli con la loro 
posizione.

## Le celle

Per definire la mappa, utilizziamo quindi le seguenti classi:

__Cell__: rappresenta una cella della matrice. Ogni cella è
contraddistinta dalle coordinate cartesiane (x,y), che sono 
indicate da valori interi relativi.

Da questa classe ereditano due classi, che sono quelle che utilizzeremo
principalmente:
__CardCell__: rappresenta una cella occupata da una carta. 
Le informazioni aggiuntive che contiene sono: 
- Il riferimento alla Card che sta occupando tale cella
- Il lato da cui è girata la carta (fronte/retro)

__AngleCell__: rappresenta una cella occupata da un angolo di una carta.
Le informazioni aggiuntive che contiene sono:
- Il simbolo visibile in quell'angolo
- La carta a cui appartiene tale angolo. Si noti che, anche se ci sono
  più carte attaccate a tale angolo, il riferimento è alla carta 
  cui appartiene al simbolo

Valgono i seguenti vincoli sui valori della posizione per i CardCell e
gli AngleCell:
- La carta iniziale ha posizione (0,0)
- Gli angoli della carta iniziale hanno posizione (1,-1), (1,1), (-1,-1), (1,1)
- Gli angoli della carta iniziale hanno posizione (1,-1), (1,1), (-1,-1), (1,1)
  (si tratta solo di una regola d'esempio)

|         |         |        |
|---------|---------|--------|
| (-1,1)  |         | (1,1)  |
|         | IC(0,0) |        |
| (-1,-1) |         | (1,-1) |

- Tutte le CardCell possono avere solo coordinate pari, nella forma
  (2i, 2j), i, j in Z
- Tutte le AngleCell possono avere solo coordinate dispari, nella forma
  (2i+1, 2j+1)
- Data una CardCell, le posizioni degli angoli adiacenti, che possono
contenere i suoi simboli o quelli delle altre carte attaccate ad essa,
sono le seguenti:

|            |         |            |
|------------|---------|------------|
| (i-1, j+1) |         | (i+1, j+1) |
|            | C(i, j) |            |
| (i-1, j-1) |         | (i+1, j-1) |

- Di conseguenza, è immediato muoversi tra le carte 
  che si trovano ai diversi angoli (sommando/sottraendo 2 alle coordinate)

## La mappa

A questo punto, gli elementi principali della nostra classe saranno
i seguenti 3 Set:

Set<CardCell> cards: lista di tutte le carte nel campo di gioco
Set<AngleCell> angles: lista degli angoli delle carte, sia liberi,
cioé a cui è possibile attaccare delle carte, sia già collegati da due carte
Set<CardCell> availableCells: lista di tutte le celle in cui è possibile
inserire una nuova carta (attaccandola a quelle già presenti sul campo)

Tutte le celle sono contraddistinte (ai fini dei metodi equals() e hashCode())
utilizzando le coordinate. Introduciamo quindi il vincolo che, 
per ogni Cell nei tre insiemi, non ne esistono altre, con le stesse coordinate,
all'interno dei 3 insiemi.

## Implementazione metodi

Per analizzare le implementazioni delle funzionalità e la loro complessità
computazionale, indichiamo con n il numero di carte presenti sulla mappa.
Sappiamo che, per ogni carta aggiunta, il numero di angoli complessivo
(liberi e non) aumenta al più di 3 (dato che la nuova carta va a coprire
un altro angolo libero già presente); lo stesso avviene per il numero di posizioni
libere. Pertanto, il numero di celle
complessive da gestire nella matrice è circa 7n. 
Complessità spaziale della rappresentazione: O(n)

Assumiamo che, inizialmente, l'unica carta in campo sia quella iniziale
e che tutti i suoi angoli siano liberi.

- Aggiungere una nuova carta: addCard(card : Card, position: (x,y), side: front/back)
  Aggiunge una carta attaccandola nella posizione libera indicata

  Le azioni da fare sono:
  1. Controllare che la carta sia effettivamente inserita in una delle 
    posizioni libere di availableCells.
  2. Aggiungere i quattro angoli della carta ad angles.
    Se un angolo è già presente nella stessa posizione (è di un'altra carta),
    si aggiorna il simbolo mostrato e la carta cui si riferisce.
  3. Si aggiungono in availableCells le nuove posizioni disponibili per attaccare le carte.
    Si tratta delle posizioni nella forma (2i ± 2, 2j ± 2), che non sono già occupate
    da altre carte e il cui angolo corrispondente non è nascosto. 
    In effetti, availableCells serve solo per il controllo al punto 1,
    forse si può sostituire con un opportuno controllo a runtime; ma avere le posizioni
    libere disponibili è comodo per poterle mostrare al giocatore.

  Complessità temporale: O(1)

- Contare il numero di simboli presenti sul campo:
  - Scorrere la lista delle carte ed estrarre i simboli al centro
  - Scorrere la lista degli angoli ed estrarre i simboli
  
    Complessità temporale: O(n)
    Si può rendere O(1) se si salvano in memoria dei contatori per ogni tipo di simbolo,
    aggiornati ogni volta che si aggiungono delle carte

- Controllare i pattern formati da carte del colore:
   - Si controlla il pattern a partire da una carta (es. quella più a sinistra) 
   Basta cercare una carta del colore indicato e muoversi nelle posizioni corrispondenti
   - Bisogna fare questo per ogni carta
   - Bisogna evitare di contare carte più volte.
    
   Complessità: O(n)

## Giudizio complessivo

Pro:
- L'implementazione dei metodi di analisi delle proprietà della mappa 
  è semplice ed (abbastanza) efficiente.
- Non ci sono particolari sprechi di memoria nella rappresentazione

Contro:
- L'analisi della mappa richiede di conoscere diversi invarianti e
  vincoli della rappresentazione interna. Si deve provare ad 
  incapsularla opportortunamente per mitigare questo problema