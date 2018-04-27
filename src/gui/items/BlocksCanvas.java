package gui.items;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.JComponent;

public class BlocksCanvas extends JComponent {
	private static final long serialVersionUID = 7920533803582858096L;
	int sigma;
	Image image;
	List<BlockView> blocks;

	public BlocksCanvas(Image image, List<BlockView> blocks, int sigma) {
		this.image = image;
		this.sigma = sigma;
		this.blocks = blocks;
		addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println("clicked "+e.getX()+" - "+e.getY());
				int x = e.getX()/Params.blockXSize;
				int y = e.getY()/Params.blockYSize;
				System.out.println("Blocks "+Params.blocksX+"-"+Params.blocksY+" |X: "+x+" | Y: "+y+" | pos: "+(y*Params.blocksX+x));
				int i = y*Params.blocksX+x;
				blocks.get(i).setStatus((blocks.get(i).getStatus()+1)%6);
				((BlocksCanvas) e.getSource()).repaint();
				
			}
		});
	}

	public void paint(Graphics g) {
		g.drawImage(image, sigma, sigma, this);
		for (BlockView block : blocks) {
			System.out.println("Block: "+ block.getMainArrea().toString());
			g.drawRect((int) block.getArea().getX(), (int) block.getArea().getY(),
					(int) block.getArea().getWidth(), (int) block.getArea().getHeight());
			g.setColor(Colors.Color(block.getStatus()));
			g.fillRect((int) block.getMainArrea().getX(), (int) block.getMainArrea().getY(),
					(int) block.getMainArrea().getWidth(), (int) block.getMainArrea().getHeight());
		
		}

	}
}