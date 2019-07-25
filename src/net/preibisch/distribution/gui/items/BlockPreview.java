package net.preibisch.distribution.gui.items;

import java.awt.Rectangle;

public class BlockPreview extends Object {
	private Rectangle area;
	private Rectangle mainArrea;
	private int status;
	public BlockPreview(Rectangle area, Rectangle mainArrea, int status) {
		super();
		this.area = area;
		this.mainArrea = mainArrea;
		this.status = status;
	}
	public Rectangle getArea() {
		return area;
	}
	public void setArea(Rectangle area) {
		this.area = area;
	}
	public Rectangle getMainArrea() {
		return mainArrea;
	}
	public void setMainArrea(Rectangle mainArrea) {
		this.mainArrea = mainArrea;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
}
