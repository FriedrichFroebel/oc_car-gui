#Version 1.6 - 2016-05-21
* Dateidialog zur Auswahl einer GPX-Route in eigene Funktion ausgelagert
* Schreibfehler bei Namen von Radiobuttons behoben
* Auswahl der Cachearten häufiger speichern
* neue Version externer Bibliotheken

#Version 1.5 - 2016-02-21
* Compiler-Warnungen entfernt
* versehentliche Exception bei Aufruf der Cachesuche via Enter-Taste entfernt

#Version 1.4 - 2016-01-01
* beim Laden einer GPX-Route werden jetzt keine Start- und Zielkoordinaten mehr abgerufen, was bisher zu Abstürzen führte
* Eingabeparameter werden nun (umfangreicher) geprüft: leere Strings werden nicht gespeichert, D-/T-Bereich wird nun via Gleitkommaparsing geprüft
* Abstand zwischen den Wegpunkten beim Auslesen von KML-/GPX-Dateien nun via Variable änderbar (jeder X. Wegpunkt wird gespeichert)

#Version 1.3 - 2015-12-16
* E-Mail-Versand kann nun via Checkbox aktiviert (und natürlich auch deaktiviert) werden, ohne damit das Passwort und dessen eventuelle Speicherung zu beeinflussen

#Version 1.2 - 2015-12-14
* optionale Passwortspeicherung via Checkbox möglich

#Version 1.1 - 2015-12-14
* Feld- und Framegrößen erhöht, da teilweise Beschriftungen nicht lesbar
* Fortschrittsbalken während der Cachesuche
* externe JAR-Dateien nicht mehr als solche eingebunden, sondern in Form von Class-Dateien

#Version 1.0 - 2015-12-11
* Erste Version der Javaversion
