import desmoj.core.simulator.*;

// verwaltet wartende Autos
public class ControllerProcess extends SimProcess 
{
    private KreuzungModel Kreuzung;
    private AutoEntity auto, auto1, auto2;
    private int count;

    // Konstruktor
	// Par 1: Modellzugehoerigkeit
	// Par 2: Name des Ereignisses
	// Par 3: show in trace?
    public ControllerProcess (Model owner, String name, boolean showInTrace) {
        super(owner, name, showInTrace);
        Kreuzung = (KreuzungModel) owner;
    }
    
    //Aktionen, die bei Aktivierung dieses Prozesses ausgefuehrt werden
    public void lifeCycle() 
    {
        while (true)
        {
            // JHS nicht leer?
    		if (!Kreuzung.autoQueueJHS.isEmpty()) {
    			auto = Kreuzung.autoQueueJHS.first();
    			if (auto.getPriority()==1) { // Linksabbieger?
    				// Nord und Sued leer?
    				if (Kreuzung.autoQueueNord.isEmpty() && Kreuzung.autoQueueSued.isEmpty()) {
    					// Auto in JHS queren Kreuzung
    					Kreuzung.autoQueueJHS.remove(auto);
    					sendTraceNote("Auto verlaesst Kreuzung von JHS linksabbieger.");
    					// Durchfahrtszeit
    					hold(new TimeSpan(Kreuzung.getFahrtZeit()));
    					count=1;
    				}
    			}
    			else { // Rechtsabbieger?
    				if (Kreuzung.autoQueueSued.isEmpty()) {
    					// Auto in JHS queren Kreuzung
    					Kreuzung.autoQueueJHS.remove(auto);
    					sendTraceNote("Auto verlaesst Kreuzung von JHS rechtsabbieger.");
    					// Durchfahrtszeit
    					hold(new TimeSpan(Kreuzung.getFahrtZeit()));
    					count=1;
    				}
    			}
    		}
            
            // Autos von Nord und Sued queren Kreuzung
            if (count!=1) {
            	if (!Kreuzung.autoQueueNord.isEmpty()) {
            		auto1 = Kreuzung.autoQueueNord.first();
            		Kreuzung.autoQueueNord.remove(auto1);
            		sendTraceNote("Auto verlaesst Kreuzung von Nord.");
            	}
            	if (!Kreuzung.autoQueueSued.isEmpty()) {
            		auto2 = Kreuzung.autoQueueSued.first();
            		Kreuzung.autoQueueSued.remove(auto2);
            		sendTraceNote("Auto verlaesst Kreuzung von Sued.");
            	}
            	// Durchfahrtszeit
                hold(new TimeSpan(Kreuzung.getFahrtZeit()));
            }
            count=0;
            
            if (isInterrupted()) {
            	// Einsatzfahrzeug
    			sendTraceNote("interrupted by Police :)");
    			// clear interrupt code
    			clearInterruptCode();
    			// stop (Durchfahrtszeit Einsatzwagen)
    			hold(new TimeSpan(0.05));
    		}
        }
    }
}
