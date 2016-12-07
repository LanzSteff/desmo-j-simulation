import desmoj.core.simulator.Experiment;
import java.io.*;

import desmoj.core.simulator.TimeInstant;

public class SimuKreuzung 
{
    // Hauptmethode, zustaendig fuer
    // - Experiment instantieren
    // - Modell instantieren
    // - Modell mit Experiment verbinden
    //   - Einstellungen fuer Simulation und Ergebnisberichte
    //   - Simulation starten
    //   - Kriterium fuer Simulationsende aufstellen
    //   - Reports initialisieren
    //   - aufraeumen, abschliessen   
    public static void main(java.lang.String[] args) {

    	// neues Experiment erzeugen
    	Experiment kreuzungExperiment = 
            new Experiment("JHSKreuzung");
 
        // neues Modell erzeugen
        KreuzungModel Kreuzung = 
            new KreuzungModel(null, "Kreuzung Modell", true, true);  

        // Modell mit Experiment verbinden
        Kreuzung.connectToExperiment(kreuzungExperiment);

        // Intervall fuer trace/debug
        kreuzungExperiment.tracePeriod(new TimeInstant(0.0), new TimeInstant(60));
        kreuzungExperiment.debugPeriod(new TimeInstant(0.0), new TimeInstant(60) );

        // Ende der Simulation setzen
        // -> time-Einstellung in KreuzungModel
        kreuzungExperiment.stop(new TimeInstant(Kreuzung.time));

        // Experiment zur Zeit 0.0 starten
        kreuzungExperiment.start(); 
        
        // Report generieren
        kreuzungExperiment.report();

        // Ausgabekanaele schliessen, allfaellige threads beenden
        kreuzungExperiment.finish();
        
        // Konsolenausgabe
        System.out.println();
        System.out.println("Maximale Wartezeit: " + Kreuzung.maxWaitJHS() + " min");
        System.out.println("Durchschnittliche Wartezeit: " + Kreuzung.averageWaitJHS() + " min");
        System.out.println("Maximale Anzahl wartender Autos: " + Kreuzung.maxLengthJHS());
        
        //schreiben der Ergebnisse in eine CSV-Datei
        try 
	    {
	    	File file = new File("simresult.csv");
	    	FileWriter out = new FileWriter(file, true);
	    	String line = "";

			line = "Maximale Wartezeit: " + "," + Kreuzung.maxWaitJHS() + "," + " min" + "\n"; out.write(line);
			line = "Durchschnittliche Wartezeit: " + "," + Kreuzung.averageWaitJHS() + "," + " min" + "\n"; out.write(line);
	        line = "Maximale Anzahl wartender Autos: " + "," + Kreuzung.maxLengthJHS() + "\n\n"; out.write(line);
	        out.close();
	        
	        System.out.println("\nDie Ergebnisse wurden erfolgreich in die datei simresult.csv geschrieben.\n\n");
		}
	    catch(Exception e)
	    {
	    	System.out.println("\nFehler beim Schreiben der CSV-Datei - Programmende\n\n");
	    }
	
    }
}