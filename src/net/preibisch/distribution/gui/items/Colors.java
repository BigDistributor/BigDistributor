package net.preibisch.distribution.gui.items;
import java.awt.Color;

public class Colors {
	
	public static final int START  = 1;
	public static final int SENT  = 2;
	public static final int PROCESSING  = 3;
	public static final int PROCESSED  = 4;
	public static final int GOT  = 5;
	public static final int ERROR  = 0;
	
	
	public static Color Color(int color) {
		switch (color) {
		case START: return rgba(236, 240, 241);
		case SENT: return rgba(255, 195, 18);
		case PROCESSING: return rgba(247, 159, 31);
		case PROCESSED: return rgba(196, 229, 56);
		case GOT: return rgba(0, 148, 50);
		case ERROR: return rgba(234, 32, 39);
		default: return new Color( 0, 0, 0,0);
		}
	}
	
	private static Color rgba(int red, int green, int blue) {
		return new Color( red, green, blue,100);
	}
	
}
