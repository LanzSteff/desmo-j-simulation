import desmoj.core.dist.*;
import desmoj.core.simulator.*;

public class KreuzungModel extends Model
{
	//Simulationszeit
    public int time = 120; // Stosszeit 2h
    
	// Zufallszahlengenerator fuer Autoankuenfte
	private RealDistNormal autoAnkunftsZeit;

    // liefert eine Zufallszahl fuer Autoankunftszeit
    public double getAutoAnkunftsZeit() {
	   return autoAnkunftsZeit.sample();
    }
	
    // Zufallszahlengenerator zur Ermittlung der Durchfahrtszeit
	private RealDistUniform fahrtZeit;

    // liefert eine Zufallszahl fuer Fahrtzeit
    public double getFahrtZeit() {
        return fahrtZeit.sample();
    }
    
    // Zufallszahlengenerator zur Ermittlung der Einsatzzeit fuer Einsatzfahrzeuge
    private RealDistUniform einsatzZeit;
    
    // liefert eine Zufallszahl fuer Einsatzzeit fuer Einsatzfahrzeuge
    public double getEinsatzZeit() {
    	return einsatzZeit.sample();
    }
    
    // unterbricht Controller, wenn Erzeuger wieder taetig wird
    protected InterruptCode Einsatzwagen;
    
    // 2 Einsatzfahrzeuge am Tag
    public EinsatzEvent neuerEinsatzwagen1, neuerEinsatzwagen2;
    
    // Erzeuger
    public ErzeugerProcess neuerErzeuger;
    
    // Controller
    public ControllerProcess neuerContr;
    
	/*************************************
    * Warteschlange fuer wartende Autos
    * jedes Auto kommt zuerst hier hinein
    **************************************/
    protected Queue<AutoEntity> autoQueueNord;
    protected Queue<AutoEntity> autoQueueSued;
    protected Queue<AutoEntity> autoQueueJHS;
	
    // Konstruktor
    public KreuzungModel(Model owner, String name, boolean showInReport, 
                            boolean showIntrace) {
    	super(owner, name, showInReport, showIntrace);
    }

    // Kurzbeschreibung des Modells
    public String description() {
    	return "Simulation fuer eine Kreuzung";
    }	

    public TimeSpan maxWaitJHS() {
    	return this.autoQueueJHS.maxWaitTime();
    }
    
    public TimeSpan averageWaitJHS() {
    	return this.autoQueueJHS.averageWaitTime();
    }
    
    public int maxLengthJHS() {
    	return this.autoQueueJHS.maxLength();
    }

    // Initialisierung der benutzten DESMO-J Infrastruktur
	public void init() {
		
    	// Generator fuer Ankunftszeiten initialisieren (Stosszeit)
    	// Par 1: Modellzugehoerigkeit
    	// Par 2: Name des Generators
    	// Par 3: mittlere Zeitdauer in Minuten zwischen Autoankuenften
    	// Par 4: show in report?
    	// Par 5: show in trace?
		autoAnkunftsZeit = 
			new RealDistNormal(this, "Ankunftszeitintervall", 0.12, 0.09, true, true);

    	// keine negativen Ankunftszeitintervalle
    	autoAnkunftsZeit.setNonNegative(true);

    	// Generator fuer Durchfahrtszeit initialisieren
    	// Par 1: Modellzugehoerigkeit
    	// Par 2: Name des Generators
    	// Par 3: minimale Fahrtzeit in Minuten (5sec)
    	// Par 4: maximale Fahrtzeit in Minuten (10sec)
    	// Par 5: show in report?
    	// Par 6: show in trace?
        fahrtZeit = 
            new RealDistUniform(this, "Durchfahrtszeit", 0.083, 0.18, true, true);
        
        // Generator fuer Einsatzzeit initialisieren
        // Par 1: Modellzugehoerigkeit
    	// Par 2: Name des Generators
    	// Par 3: minimale Fahrtzeit in Minuten (5sec)
    	// Par 4: maximale Fahrtzeit in Minuten (10sec)
    	// Par 5: show in report?
    	// Par 6: show in trace?
        einsatzZeit =
        	new RealDistUniform(this, "Einsatzzeit", 0.0, this.time, true, true);
        
        // initialise interrupt code
        Einsatzwagen = new InterruptCode("Achtung Einsatzwagen!");
        
        neuerErzeuger = new ErzeugerProcess(this, "Erzeuger", true);
        
        neuerContr = new ControllerProcess(this, "Controller", true);

    	// Warteschlange fuer Autos initialisieren
    	// Par 1: Modellzugehoerigkeit
    	// Par 2: Name der Warteschlange
    	// Par 3: show in report?
    	// Par 4: show in trace?
       	autoQueueNord = new Queue<AutoEntity>(this, "Nord-Warteschlange",true, true);
        autoQueueSued = new Queue<AutoEntity>(this, "Sued-Warteschlange",true, true);
        autoQueueJHS = new Queue<AutoEntity>(this, "JHS-Warteschlange",true, true);
	}
	
    // erste Ereignisse eintragen fuer Simulationsbeginn
    public void doInitialSchedules() {

        // Erzeugerprozess starten
        neuerErzeuger.activate(new TimeSpan(0.0));
        // Controllerprozess starten
        neuerContr.activate(new TimeSpan(0.0));
        
        // Einsatzwagen1 losschicken
        neuerEinsatzwagen1 = new EinsatzEvent(this, "Einsatzwagen", true);
		neuerEinsatzwagen1.schedule(new SimTime(this.getEinsatzZeit()));
		// Einsatzwagen2 losschicken
		neuerEinsatzwagen2 = new EinsatzEvent(this, "Einsatzwagen", true);
		neuerEinsatzwagen2.schedule(new SimTime(this.getEinsatzZeit()));
    }
}
