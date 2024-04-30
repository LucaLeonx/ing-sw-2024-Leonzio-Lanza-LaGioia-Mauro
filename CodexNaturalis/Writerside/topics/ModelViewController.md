# Model View Controller

Pattern Architetturale.
Componenti fatti spesso in package separati

## Model
- Rappresenta i dati. Ha metodi per accedere ai dati e modificarli
- Notifica i cambiamenti di stato alla View (pattern Observer)

## Controller
- Riceve gli eventi dalla View e modifica di conseguenza il Model
- Sceglie quali dati far vedere nella View

Utile fare una macchina a stati, ed usare il pattern State per gestirlo.


## View
- Mostra all'utente lo stato dell'applicazione
- Genera gli eventi (utente) da passare al controller
- Il controller non deve mai intervenire sugli elementi grafici.
- Espone al controller metodi d'interfaccia generici (ShowMessage(), ShowWinner()...)

La View può osservare direttamente il Model o farlo tramite il controller
    - Pro: più semplice da implementare
    - Contro: la View non riflette completamente il Model (anche a causa di bug nel controller)

Si può usare il pattern Observer:
- View osserva il Model (o il Controller), per eventuali modifiche dello stato


#
