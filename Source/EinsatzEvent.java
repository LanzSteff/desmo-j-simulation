import desmoj.core.simulator.*;

// erzeugt Einsatzfahrzeuge
public class EinsatzEvent extends ExternalEvent {
	
	private KreuzungModel Kreuzung;
	
	public EinsatzEvent (Model owner, String name, boolean showInTrace) {
		   super(owner, name, showInTrace);

		   Kreuzung = (KreuzungModel) owner;
	}
	
	public void eventRoutine() {
		
		Kreuzung.neuerContr.interrupt(Kreuzung.Einsatzwagen);
		
	}
}