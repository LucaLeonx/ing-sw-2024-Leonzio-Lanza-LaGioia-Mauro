# Codex Naturalis

Implementazione digitale dell'[omonimo gioco da tavolo](https://boardgamegeek.com/boardgame/314503/codex-naturalis) 
sviluppata per il progetto di Ingegneria del Software

## Caratteristiche supportate

- Implementazione del gioco completo (regole complete)
- Connessione client-server disponibile sia con Socket 
  che Java RMI
- Interfaccia testuale (TUI) e grafica (GUI)
- Gestione di partite multiple in contemporanea sullo stesso server
- Gestione delle disconnessioni dei giocatori durante le partite

## Avvio del gioco

Nella cartella out/artifacts sono disponibili i JAR eseguibili del progetto:
- /server_jar contiene l'eseguibile per il server
- /client_tui_jar contiene l'eseguibile per il client, con interfaccia testuale
- /client_gui_jar contiene l'eseguibile per il client, con interfaccia grafica


Per prima cosa, è necessario avviare l'eseguibile del server

´java ./server.jar´

A questo punto, i client presenti sulla stessa rete potranno collegarsi per giocare

### Avvio del gioco - Interfaccia testuale

Avendo a disposizione il JAR eseguibile corrispondente, 
digitare sul terminale il comando:

\java.exe -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8 -jar client_tui.jar

### Avvio del gioco - Interfaccia testuale

Avendo a disposizione il JAR eseguibile corrispondente, usare il comando:

...


## Autori

- Giovanni La Gioia
- Stefano Lanza
- Luca Leonzio
- Simone Mauro

