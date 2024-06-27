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

Nella cartella CodexNaturalis/deliverables/final/jar sono disponibili i JAR eseguibili del progetto:
- Server.jar contiene l'eseguibile per il server
- TUI-Client.jar contiene l'eseguibile per il client, con interfaccia testuale
- GUI-Client.jar contiene l'eseguibile per il client, con interfaccia grafica


Per prima cosa, è necessario avviare l'eseguibile del server

<code> java -jar path_to_project/CodexNaturalis/deliverables/final/jar/Server.jar
</code>

Su Windows potrebbe essere necessario aggiungere java.exe al nelle environment variables di sistema, oppure lanciare il camando sostituendo java con java.exe

A questo punto, i client presenti sulla stessa rete potranno collegarsi per giocare

### Avvio del gioco - Interfaccia testuale(TUI)

Avendo a disposizione il JAR eseguibile corrispondente,
1) Settare la configurazione UTF-8 del terminale:
- Per MacOS, non è necessario fare nulla
- Per Windows (se non si ha già impostato di default UTF-8)
    - Da Command prompt digitare: <code>chcp 65001</code>
    - Da Powershell digitare: <code>[Console]::OutputEncoding = [System.Text.Encoding]::UTF8</code>
- Per Linux: Vedi sezione **"Impostare UTF-8 su Linux"** subito dopo, se è necessario

Nota: Per Linux è consigliato di scaricare il package <code>noto-fonts-emoji</code> usando i comandi di download della vostra distro

Il terminale integrato di Intellij e molti altri utilizzano in automatico l'encoding UTF-8 per
l'esecuzione dei jar. Si può però specificare manualmente con:

Da Powershell:

<code>java.exe "-Dfile.encoding=UTF-8" "-Dsun.stdout.encoding=UTF-8" "-Dsun.stderr.encoding=UTF-8" -jar path_to_jar/TUI-Client.jar</code>

Da cmd:

<code>java.exe -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8 -jar path_to_jar/TUI-Client.jar</code>

Chiaramente se non si ha impostato nelle environment variables del pc java bisogna specificare il path completo assoulto per arrivare
alla cartella bin contenente java.exe. esempio col mio con i path:

Sia per Windows che per Linux, se le opportune environment variables non sono impostate,
è necessario specificare il path assoluto all'eseguibile java.exe (o java).
Ad esempio:

<code> C:\Users\smaur\.jdks\openjdk-21.0.2\bin\java.exe -jar C:\Users\smaur\IdeaProjects\ing-sw-2024-Leonzio-Lanza-LaGioia-Mauro\CodexNaturalis\out\artifacts\TUI\TUI-Client.jar</code>

NB: Potrebbe essere utile leggere la USER GUIDE [USER GUIDE TUI.md](deliverables%2Ffinal%2Fuml%2Fview%2FUSER%20GUIDE%20TUI.md) posta in deliverables/final/uml/view
per avere una migliore comprensione del modo in cui le carte sono state stilizzate tramite emoji supportate da jdk 11 che ha introdotto
UNICODE 10.0.0 [Unicode docs](https://docs.oracle.com/en/java/javase/11/intl/internationalization-enhancements1.html#GUID-D0AF5316-F01C-4A3A-A3CA-7875C3D34601)


### Avvio del gioco - Interfaccia grafica

Avendo a disposizione il JAR eseguibile corrispondente, usare il comando:

<code>java -jar ./GUI-Client.jar</code> su Linux e MacOS;

<code>java.exe -jar ./GUI-Client.jar</code> su Windows

Avendo le stesse accortezze riguardo alle variabili d'ambiente

# Opzioni da riga di comando

Durante l'avvio degli eseguibili, è possibile utilizzare le seguenti opzioni da riga di comando:

- <code>-Rp</code>, <code>--rmi-port</code> : permettono di cambiare la porta utilizzata per il collegamento Java RMI (default 1099)
- <code>-Sp</code>, <code>--socket-port</code> : permette di cambiare la porta utilizzata per il collegamento via Socket (default 20000)

Impostazioni solo client (TUI, GUI)
- <code>-H</code>, <code>--host</code> : permette di specificare l'indirizzo su cui si trova il server di gioco (default localhost)

Impostazioni solo server:

 <code>-Tshort</code>, <code>--timeout-short</code> : Avvia il server impostando dei timeout molto brevi per le operazioni di gioco
- Timeout impostati (in secondi): 
- Cancellazione utente registrato in assenza di login: 60
- Tempo per la scelta del setup: 25
- Tempo per decisione sulla mossa: 25
- Tempo per uscire da una partita al suo termine: 60

<code>-Tlong</code> <code>--timeout-long</code> : Avvia il server impostando dei timeout ragionevolmente lunghi per le operazioni di gioco

- Timeout impostati (in secondi):
- Cancellazione utente registrato in assenza di login: 600
- Tempo per la scelta del setup: 180
- Tempo per decisione sulla mossa: 180
- Tempo per uscire da una partita al suo termine: 300

## Impostare UTF 8 su Linux:
Per impostare UTF-8 come standard sul terminale di Linux, puoi seguire questi passaggi:

1. Verifica la configurazione locale attuale:
   Apri il terminale e digita:
   <code>locale</code>

Questo comando mostrerà le impostazioni locali attuali.
Se le impostazioni non sono in UTF-8, continua con i passaggi successivi.

2. Elenca tutte le locali disponibili:
   Digita: <code>locale -a</code>

Questo comando elencherà tutte le locali disponibili sul tuo sistema.
Cerca una locale UTF-8, ad esempio en_US.UTF-8 o it_IT.UTF-8.

3. Genera la locale UTF-8:
   Se la tua locale desiderata non è presente, puoi generarla. Ad esempio, per generare it_IT.UTF-8, digita:
   <code>sudo locale-gen it_IT.UTF-8</code>

Poi aggiorna l’elenco delle locali generate:
<code>sudo dpkg-reconfigure locales</code>

4. Imposta la locale predefinita:
   Modifica il file ´/etc/default/locale´ per impostare la locale predefinita.

Puoi usare un editor di testo come nano o vim. Ad esempio, con nano:
<code>sudo nano /etc/default/locale</code>

Assicurati che il file contenga le seguenti righe, sostituendo it_IT.UTF-8 con la locale che preferisci:
>plaintext
>
>LANG=it_IT.UTF-8
>
>LANGUAGE=it_IT:it
>
>LC_ALL=it_IT.UTF-8

Riavvia la sessione del terminale:
Dopo aver salvato e chiuso il file, chiudi e riapri il terminale per applicare le modifiche.

Verifica la nuova configurazione locale:
Digita di nuovo: <code>locale</code>

Ora dovresti vedere che tutte le impostazioni locali sono configurate su UTF-8.

Facendo questi passaggi, dovresti aver impostato UTF-8 come standard sul tuo terminale Linux.

## Autori

- Giovanni La Gioia
- Stefano Lanza
- Luca Leonzio
- Simone Mauro




