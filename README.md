Opencaching.de - Caches entlang einer Route (GUI-Version)
===

Download des lauffähigen Programms (JAR-Datei) unter ***Releases*** (https://github.com/FriedrichFroebel/oc_car-gui/releases)

Dies ist eine Version des oc_car-Skriptes in Java mit grafischer Oberfläche.

Dabei habe ich mich so weit wie möglich an das Original gehalten, musste aber im Endeffekt das meiste komplett neu schreiben, da z.B. ein Einbinden von GPSBabel etc. nicht so einfach möglich ist.

Dadurch entfällt die Notwendigkeit der Installation zusätzlicher Software. Zudem sollte das Programm auch ohne Anpassungen auf den unterschiedlichen Plattformen laufen.

Anders als im Original wird zum Speichern der Konfiguration eine XML-Datei genutzt. Aus Sicherheitsgründen wird auch das E-Mail-Passwort nicht mehr in der Konfigurationsdatei gelagert, sondern muss bei gewünschtem E-Mail-Versand in ein entsprechendes Feld eingetragen werden.

Der vom Programm standardmäßig verwendete Ausgabepfad befindet sich im Homeverzeichnis im Ordner "occar".

Fehler gefunden? Probleme?
---
Das Programm ist sicherlich nicht fehlerfrei.

Sollte das Programm nicht das gewünschte Ergebnis bringen, dann könnt Ihr entweder einen Issue hier auf Github erstellen oder mich per persönlicher Opencaching-Nachricht kontaktieren.

Bitte beschreibt in einem solchen Fall Euer Problem möglichst genau, um es eingrenzen zu können. Eine Hilfe ist mir dabei die eingebaute erweiterte Ausgabe auf der Kommandozeile, die aktiviert wird, sobald beim Start der JAR-Datei via Kommandozeile hinter dem Dateinamen nach einem Leerzeichen ein beliebiger Buchstabe geschrieben wird (also beispielsweise "java -jar oc_car-gui.jar d", wenn Ihr Euch im Pfad des Programms befindet). Auch die Eingabe-/Ausgabedateien und die Konfiguration helfen bei der Fehlersuche.

**Hinweis: Der E-Mail-Versand aus dem Programm heraus ist aktuell nur mit einem GMail-Konto getestet worden.**

Mithelfen?
---
Externe Beiträge zum Programm sind immer gern gesehen und helfen dabei, auch neue Funktionen zu ermöglichen. Dazu reicht im Normalfall ein Pull Request mit einer kurzen Beschreibung der Veränderung.

Damit alle Funktionen in Eurer Entwicklungsumgebung laufen, benötigt Ihr allerdings noch zwei zusätzliche (externe) JAR-Dateien, die als Bibliothek eingebunden werden müssen (in Eclipse: "Configure Build Path" - "Add external JARs"):
* http://commons.apache.org/proper/commons-io/download_io.cgi (commons-io-2.4-bin)
* https://java.net/projects/javamail/downloads (JavaMail API 1.5.4)

Andere Versionen
---
* Bash-Version (Linux): https://github.com/kabegeo/oc_car (Original)
* Bash-Version (Linux): https://github.com/FriedrichFroebel/oc_car (Fork mit Fehlerbehebungen)
* Bash-Version (Windows): https://github.com/FriedrichFroebel/oc_car-Windows (Programmpfade werden angepasst)
