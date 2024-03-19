# UML: Class Diagram

Class diagram: descrive l'architettura dei componenti
Sequence diagram: descrive il comportamento, il protocollo di comunicazione tra le classi

Blocco fondamentale: classe

## Struttura:

__Nome classe__
---
_Attributi_
(Di solito privati)

Sintassi attributi
    mod nome : Tipo

Modificatori di visibilità possibili
- \+ public
- \- private
- \# protected

---
_Metodi_

Sintassi

mod nomeMetodo(par1 : tipo1, par2 : tipo2) : tipoRitorno

## Frecce di collegamento

__Associazione__: indica che una classe è associata ad una o più istanze di un'altra classe
Può anche avere una direzione o bidirezionale

__Aggregazione__: una o più istanze di una classe sono parte di un'altra. Le enitità esistono in maniera
indipendente dalla classe da cui fanno altre
__Composizione__: una o più istanze di una classe sono parte, contenute in un'altra classe.
Le istanze non possono esistere indipendentemente dalla classe di cui fanno parte, vi è una dipendenza logica
__Ereditarietà__: freccia che indica che una classe estende un'altra (o implementa un'interfaccia)

## Prefissi nei nome classe:
- _abstract_ : classe astratta
- _interface_ : interfaccia

## Package

Rappresentati come cartelle coi nomi, freccie che collegano le dipendenze logiche

## Tools per scrivere l'UML
- StartUML (consigliato)
- PlantUML (è per diagrams-as-code ma attenzione, non è esattamente in linea da UML)