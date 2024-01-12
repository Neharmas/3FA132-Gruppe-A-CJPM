package dev.hv.console;

import java.io.File;

import org.jdbi.v3.core.Jdbi;

public interface IConsole {
// ToDo´s 
// + Wie runne ich java programme aus der Console? => $ javac PathToFile.java; java PathToCompiledFile
// + Wie übergebe ich Java cl-arguments? => einfach im Terminal hinten anstellen und mit Leerzeichen trennen
// + Wie verwende ich in Java cl-arguments? => sind im String[] args der Mainmethode enthalten
// - Benötigte Daten per User-/Reading-/Customer-Api aufrufen
// - Davon erhaltene Daten erstmal in Json
// [- Json in 'Dict' parsen ]
// - Von Json mit libs in csv, xml & text-tabellenformat
	   // /** - <flag> = <description> */
	   // returnValue method();
	   // - help Bei jeglichem Error auch printen
	   /** -h = print Hilfetext + Versionsnummer des Runtime Environment*/
	   void help();

	   /** --delete = drop all tables [+ reset PK-Id counter]*/
	   void deleteAllTables();

	   /** export ... <tablename> = Export Tabelle 
	    * -o, --output=<fileout> = Name der Ausgabedatei*/
	   File exportTable();

	   /** import ... <tablename> = Export Tabelle 
	    * -i, --input=<fileout> = Name der Ingabedatei*/
	   void importTable(File inputfile);
	   
	   /** - <flag> = <description> */
}
