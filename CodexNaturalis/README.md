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
1) settare la configurazione UTF-8:
- Per MAC OS passaggio non necessario, skippare al punto 2
- Per Windows 
  - Da command prompt digitare: chcp 65001 e premere invio
  - Da powershell digitare: [Console]::OutputEncoding = [System.Text.Encoding]::UTF8 e premere invio
- Per Linux andare alla fine del README. 

digitare sul terminale il comando per aprire il programma facendo attenzione di specificare l'encoding in UTF-8:

\java.exe -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8 -jar client_tui.jar

Se dovesse dare problemi la specifica dell'encoding toglierla. 

### Avvio del gioco - Interfaccia testuale

Avendo a disposizione il JAR eseguibile corrispondente, usare il comando:

...


## Autori

- Giovanni La Gioia
- Stefano Lanza
- Luca Leonzio
- Simone Mauro

## PASSAGGI PER IMPOSTARE UTF-8 su LINUX: 
Per impostare UTF-8 come standard sul terminale di Linux, puoi seguire questi passaggi:

Verifica la configurazione locale attuale:
Apri il terminale e digita:


locale
Questo comando mostrerà le impostazioni locali attuali. Se le impostazioni non sono in UTF-8, continua con i passaggi successivi.

Elenca tutte le locali disponibili:
Digita:


locale -a
Questo comando elencherà tutte le locali disponibili sul tuo sistema. Cerca una locale UTF-8, ad esempio en_US.UTF-8 o it_IT.UTF-8.

Genera la locale UTF-8:
Se la tua locale desiderata non è presente, puoi generarla. Ad esempio, per generare it_IT.UTF-8, digita:


sudo locale-gen it_IT.UTF-8
Poi aggiorna l’elenco delle locali generate:


sudo dpkg-reconfigure locales
Imposta la locale predefinita:
Modifica il file /etc/default/locale per impostare la locale predefinita. Puoi usare un editor di testo come nano o vim. Ad esempio, con nano:

bash
Copy code
sudo nano /etc/default/locale
Assicurati che il file contenga le seguenti righe, sostituendo it_IT.UTF-8 con la locale che preferisci:

plaintext

LANG=it_IT.UTF-8
LANGUAGE=it_IT:it
LC_ALL=it_IT.UTF-8
Riavvia la sessione del terminale:
Dopo aver salvato e chiuso il file, chiudi e riapri il terminale per applicare le modifiche.

Verifica la nuova configurazione locale:
Digita di nuovo:


locale
Ora dovresti vedere che tutte le impostazioni locali sono configurate su UTF-8.

Facendo questi passaggi, dovresti aver impostato UTF-8 come standard sul tuo terminale Linux.






