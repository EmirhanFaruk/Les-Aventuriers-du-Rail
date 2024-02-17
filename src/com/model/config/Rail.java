package com.model.config;

public class Rail extends Case {
	public enum Content { BLEU, VIOLET , MARRON , NOIR , VERT , JAUNE , ROUGE , BLANC , JOKER , JOKERETOILEE }
    private Content initialContent;
    private boolean occuper;
	
    public Rail(int x, int y, Content c) {
		super(x, y);
		this.initialContent = c;
	}
    
    public Content getInitialContent() {
        return initialContent;
    }

    public boolean getOccuper() {
        return occuper;
    }
    
    public void setOccuper(boolean o) {
        this.occuper = o;
    }
}