package config;

//3 cas possibles :
//1) Case Rail : false ou true, false, "", false, false ou true, Choisissez une couleur
//2) Case Ville ou Gare : false, true, nom_de_la_ville, false ou true, false, NOTHING
//3) Case Paysage : false, false, "", false, false, NOTHING
public record Case(Case CelluleSuperposable, boolean ville, String nomVille, boolean gare, boolean rempli, Content initialContent) {
    public enum Content {NOTHING, BLEU, VIOLET, MARRON, NOIR, VERT, JAUNE, NOIRE, ROUGE}
    
    public String getNomVille(String) {
    	return nomVille;
    }
    
    public boolean unPaysage() {
    	return !ville && initialContent == Content.NOTHING;
    }
    
    public boolean unChemin() {
    	return !ville && aEncoreDeLaPlace();
    }
    
    public boolean uneVille() {
    	return ville;
    }
    
    public boolean uneGare() {
    	return ville && gare;
    }

    public boolean aUneCelluleSuperposable() {
        return CelluleSuperposable != null;
    }
    
    public boolean aEncoreDeLaPlace() {
        return rempli;
    }
    
}
