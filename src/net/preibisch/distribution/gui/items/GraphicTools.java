package net.preibisch.distribution.gui.items;

import java.awt.Color;

public class GraphicTools {
	
	public static Color randomColor() {
		return new Color((int)(Math.random() * 0x1000000));
	}
}
