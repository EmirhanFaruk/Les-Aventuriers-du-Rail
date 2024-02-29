package com.model.config;

public class Paysage extends Case {

	public Paysage(int x, int y) {
		super(x, y);
	}

	@Override
	public boolean estUneCaseGare() {
		return false;
	}

}
