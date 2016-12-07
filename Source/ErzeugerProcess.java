import desmoj.core.simulator.*;

// verwaltet Queues
public class ErzeugerProcess extends SimProcess 
{
    private KreuzungModel Kreuzung;
    private double rnd1;
    private double rnd2;
    
    // Konstruktor
	// Par 1: Modellzugehoerigkeit
	// Par 2: Name des Ereignisses
	// Par 3: show in trace?
    public ErzeugerProcess (Model owner, String name, boolean showInTrace) {
	   super(owner, name, showInTrace);
	   Kreuzung = (KreuzungModel) owner;
    }
    
    // Aktionen, die bei Aktivierung dieses Prozesses ausgefuehrt werden
    public void lifeCycle() 
    {
        while (true) 
        {
            // neues Auto erzeugen
            AutoEntity neuesAuto = new AutoEntity (Kreuzung, "Auto", true);

            // Autos auf Warteschlangen aufteilen
            // 36% Queue Nord
            // 47% Queue Sued
            // 17% Queue JH-Strae
            rnd1 = Math.random();
            if (rnd1 < 0.37) {
            	Kreuzung.autoQueueNord.insert(neuesAuto);
            	sendTraceNote("Wartende Autos Nord: " + 
            		Kreuzung.autoQueueNord.length());
            }
            else if (rnd1 >= 0.37 && rnd1 < 0.84) {
            	Kreuzung.autoQueueSued.insert(neuesAuto);
                sendTraceNote("Wartende Autos Sued: " + 
                    Kreuzung.autoQueueSued.length());
            }
            else { //50% Linksabbieger, 50% Rechtsabbieger
            	if (rnd2 < 0.5)
            		neuesAuto.setPriority(1);
            	else neuesAuto.setPriority(0);
            	Kreuzung.autoQueueJHS.insert(neuesAuto);
            	sendTraceNote("Wartende Autos JHS: " + 
            			Kreuzung.autoQueueJHS.length());
            }
            
            // Prozess deaktivieren bis naechstes Auto erzeugt werden soll
            // Verdoppelt man die Wartezeit bis zum naechsten Auto, deaktiviert man die Stosszeit
            // zB hold(new TimeSpan(Kreuzung.getAutoAnkunftsZeit()*2));
            hold(new TimeSpan(Kreuzung.getAutoAnkunftsZeit()));
        }
    }
}
