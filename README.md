# go-game
Das Programm ist ein terminalbasiertes Go-Spiel.

> :warning:
  Es wird die Installation von dem java-basierten Build-Werkzeug [Apache Ant](https://ant.apache.org) vorrausgesetzt.
  
---

### Funktionen

* Wahlweise können Spieler oder Computer starten 
* Züge werden angezeigt
* Gefangene Steine werden angezeigt
* Spielbrett hat 9x9 Felder
* Undo-Funktion
* Nur gültige Züge erlaubt (entsprechend der Go-Spielregeln)
* Am Schluss wird der Sieger und das Endergenbis angezeigt

---

### Ausführen und Testen

1. Download oder Klonen des repo: `git clone https://github.com/patricktinz/go-game`
2. Navigieren Sie in den Unterordner: `cd go-game/trunk`
3. Bauen des Programms: $ ant  
3.1 Ausführen des Programms: $ ant run  
3.2 Testen des Programmcodes: $ ant test   
Das Programm nutzt das JUnit Framework zum Testen des Programmcodes.   
Hierzu muss ein Ordner libs im lokalen Unterverzeichnis trunk erstellt werden.  
In den Ordner libs muss schließlich die aktuelle Version des JUnit Framework kopiert werden.  
Das Programm wurde mit dieser [JUnit Version](https://github.com/junit-team/junit4/wiki/Download-and-Install) getestet.

---

### Download

[Download](https://github.com/patricktinz/go-game/archive/master.zip)

---

### Copyright und Lizenzen

[GNU GENERAL PUBLIC LICENSE](/LICENSE)
