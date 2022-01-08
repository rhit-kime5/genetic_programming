package source_code;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

/**
 * Class: EvolutionController
 * @author Edward Kim
 * <br>Purpose: Used as a top-level GUI to control all options and features of the evolution
 * <br>For example:
 * <pre>
 * 		new EvolutionController();
 * </pre>
 */
public class EvolutionController {
	
	
	// Constants
	public static final Dimension EVOLUTION_VIEWER_SIZE = new Dimension(800, 140);
	public static final Dimension TEXT_FIELD_SIZE = new Dimension(35, 27);
	
	
	// Fields
	public Integer terminationCount = 99; // will create 99 new "next generations", ending at Generation 100
	public Integer populationSize = 1000;
	public GeneticProgramming GP = new GeneticProgramming();
	public EvolutionComponent comp;
	public Integer viewingGeneration = 1;
	public Integer startClickCount = 0;
	public Integer evolutionDelay = 20;
	public Integer resetClickCount = 0;
	public Integer terminationScore = 10;
	public Integer terminateMessageCount = 0;

    public static void main(String[] args) {
        new EvolutionController();
    }
	

	// Constructor
	public EvolutionController() {
		
		JFrame frame = new JFrame();
		frame.setSize(EVOLUTION_VIEWER_SIZE);
		frame.setTitle("Evolution Controller");
		
		JPanel buttonsPanel = new JPanel();
		frame.add(buttonsPanel);
		buttonsPanel.setBounds(0, 10, 800, 35);
		
		JPanel buttonsPanel2 = new JPanel();
		frame.add(buttonsPanel2);
		buttonsPanel2.setBounds(0, 50, 800, 35);
		
		comp = new EvolutionComponent(this.GP);
		frame.add(comp);
				
		JLabel generationsLabel = new JLabel();
		buttonsPanel.add(generationsLabel);
		generationsLabel.setText("Generations ");
		
		Integer generationCount = this.terminationCount + 1;
		JTextField generationsInput = new JTextField(generationCount.toString());
		generationsInput.setPreferredSize(TEXT_FIELD_SIZE);
		buttonsPanel.add(generationsInput);
		
		class GenerationsListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				terminationCount = Integer.parseInt(generationsInput.getText()) - 1;
			}
		}
		generationsInput.addActionListener(new GenerationsListener());
		
		JLabel populationLabel = new JLabel();
		buttonsPanel.add(populationLabel);
		populationLabel.setText("Population Size ");
		
		JTextField populationInput = new JTextField(this.populationSize.toString());
		populationInput.setPreferredSize(TEXT_FIELD_SIZE);
		buttonsPanel.add(populationInput);
		
		class PopulationListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				populationSize = Integer.parseInt(populationInput.getText());
			} // end method
		} // end inner class
		populationInput.addActionListener(new PopulationListener());
		
		JLabel delayLabel = new JLabel();
		buttonsPanel.add(delayLabel);
		delayLabel.setText("Evolution Delay (ms) ");
		
		JTextField delayInput = new JTextField(this.evolutionDelay.toString());
		delayInput.setPreferredSize(TEXT_FIELD_SIZE);
		buttonsPanel.add(delayInput);
		
		JLabel terminationLabel = new JLabel();
		buttonsPanel.add(terminationLabel);
		terminationLabel.setText("Termination Score ");
		
		JTextField terminationInput = new JTextField(this.terminationScore.toString());
		terminationInput.setPreferredSize(TEXT_FIELD_SIZE);
		buttonsPanel.add(terminationInput);
		
		class TerminationListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				terminationScore = Integer.parseInt(terminationInput.getText());
			}
		}
		terminationInput.addActionListener(new TerminationListener());
		
		JLabel selectionLabel = new JLabel();
		buttonsPanel2.add(selectionLabel);
		selectionLabel.setText("Selection Type ");
		
		String[] selectionTypes = {"Truncation"};
		JComboBox selectionInput = new JComboBox(selectionTypes);
		buttonsPanel2.add(selectionInput);
		
		class SelectionListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				GP.selectionType = (String) ((JComboBox) e.getSource()).getSelectedItem();
			} // end method
		} // end inner class
		selectionInput.addActionListener(new SelectionListener());

		JLabel elitismLabel = new JLabel();
		buttonsPanel2.add(elitismLabel);
		elitismLabel.setText("Elitism (%) ");
		
		JTextField elitismInput = new JTextField(this.GP.elitismPercent.toString());
		elitismInput.setPreferredSize(TEXT_FIELD_SIZE);
		buttonsPanel2.add(elitismInput);
		
		class ElitismListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				GP.elitismPercent = Integer.parseInt(elitismInput.getText());
			} // end method
		} // end inner class
		elitismInput.addActionListener(new ElitismListener());

        JLabel mutationLabel = new JLabel();
		buttonsPanel2.add(mutationLabel);
		mutationLabel.setText("Mutation");
		
		JCheckBox mutationInput = new JCheckBox("", true);
		buttonsPanel2.add(mutationInput);
		
		class MutationListener implements ItemListener {
			public void itemStateChanged(ItemEvent e) {
				GP.mutation = (e.getStateChange() == 1);				
			}
		}
		mutationInput.addItemListener(new MutationListener());
		
		JLabel crossoverLabel = new JLabel();
		buttonsPanel2.add(crossoverLabel);
		crossoverLabel.setText("Crossover");
		
		JCheckBox crossoverInput = new JCheckBox("", true);
		buttonsPanel2.add(crossoverInput);
		
		class CrossoverListener implements ItemListener {
			public void itemStateChanged(ItemEvent e) {
				GP.crossover = (e.getStateChange() == 1);				
			}
		}
		crossoverInput.addItemListener(new CrossoverListener());
		
		JButton startButton = new JButton("Start/Pause Evolution");
		buttonsPanel2.add(startButton);
		
		class ContinueEvolutionListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {

                GPTree terminatingTree = GP.allGenerations.get(viewingGeneration - 1).getBest();

				if (terminatingTree.fitness() <= terminationScore && terminateMessageCount == 0) {

                    System.out.print("\nEVOLUTION TERMINATED WITH ");
                    terminatingTree.readAsInfix(terminatingTree.root);
                    System.out.print(", fitness = " + terminatingTree.fitness() + "\n");

					GP.writeToCSV();

					terminateMessageCount++;
                    
					return;
				}

                if (GP.allGenerations.size() < terminationCount + 1 && terminateMessageCount == 0) {
					viewingGeneration++;
					GP.nextGeneration(viewingGeneration);
					comp.repaint();

				} else if (terminateMessageCount == 0) {
					GP.writeToCSV();
					terminateMessageCount++;
				}

			} // end method
		} // end inner class
		
		Timer timer = new Timer(this.evolutionDelay, new ContinueEvolutionListener());
		if (GP.allGenerations.size() == terminationCount + 1) {
			timer.stop();
			
		}
		
		class DelayListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				evolutionDelay = Integer.parseInt(delayInput.getText());
				timer.setDelay(evolutionDelay);
			} // end method
		} // end inner class
		delayInput.addActionListener(new DelayListener());
		
		class StartListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				
				startClickCount++;
				if (startClickCount == 1) { 			// very first click (i.e. "start")
					GP.allGenerations.clear();
					GP.createGeneration(1, populationSize);
					viewingGeneration = 1;
					timer.start();
					
				} else if (startClickCount % 2 == 0) {	// "pause"
					timer.stop();
				} else {								// "continue"
					timer.start();
				}	
			} // end method
		} // end inner class
		startButton.addActionListener(new StartListener());
		
		JButton resetButton = new JButton("Reset");
		buttonsPanel2.add(resetButton);
		
		class ResetListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				resetClickCount++;
				terminateMessageCount = 0;
				timer.stop();
				viewingGeneration = 0;
				startClickCount = 0;
				GP.allGenerations.clear();
				comp.repaint();
				
				
				System.out.println("________________________________________evolution_reset");
				for (int i = 0; i < 100; ++i) System.out.println();
				System.out.println("________________________________________evolution_reset");
				System.out.println();
			}
		}
		resetButton.addActionListener(new ResetListener());
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	} // end constructor
	
	
} // end method
