package source_code;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

/**
 * Class: EvolutionComponent
 * @author Edward Kim
 * <br>Purpose: Used as a component to visualize evolution progress on the EvolutionController
 * <br>For example:
 * <pre>
 * 		EvolutionComponent comp = new EvolutionComponent(GP);
 * </pre>
 */
public class EvolutionComponent extends JComponent {
	
	
	// Fields
	public GeneticProgramming GP;
	
	
	// Constructor
	public EvolutionComponent(GeneticProgramming GP) {
		this.GP = GP;
	}
	
	
	// paintComponent method
	@Override
	protected void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		// Integer generationCount = this.GP.allGenerations.size();
		// Generation thisGen = this.GP.allGenerations.get(generationCount);
		
		// g2.setFont(new Font("Times New Romans", Font.PLAIN, 11));
		// g2.setColor(Color.BLACK);
		// g2.drawString("", 144, 480);		
		
	} // end method
	
	
} // end class
