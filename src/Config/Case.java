public record Case(Case CelluleSuperposable, boolean ville, boolean gare, int nbDePlace, Content initialContent) {
    public enum Content {NOTHING, BLEU, VIOLET, MARRON, NOIR, VERT, JAUNE, NOIRE, ROUGE}
    
    public boolean unPaysage() {
    	return initialContent == Content.NOTHING;
    }
    
    public boolean unChemin() {
    	return !ville && nbDePlace > 0;
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
        return nbDePlace != 0;
    }
    
}
