package source_code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Class: GeneticProgramming
 * @author Edward Kim
 * <br>Purpose: Used to initiate and process the evolution of binary expression trees
 * <br>For example:
 * <pre>
 * 		GeneticProgramming GP = new GeneticProgramming();
 * </pre>
 */
public class GeneticProgramming {

    // Fields
	ArrayList<Generation> allGenerations = new ArrayList<Generation>();	
    String inputVar = "x";
    String selectionType = "Truncation";
    boolean mutation = true;
	boolean crossover = true;
    Integer elitismPercent = 1;

    // Main
	public static void main(String[] args) {
		GeneticProgramming GP = new GeneticProgramming();
    
        GP.createGeneration(1, 1000);

        for (int i = 2; i <= 100; i++) {
            GP.nextGeneration(i);
        }

        GP.writeToCSV();
	} // end main


    // createGeneration()
	public void createGeneration(Integer genNumber, Integer populationSize) {
		
		// construct a randomly generated population
		Generation g = new Generation(genNumber, populationSize, this.inputVar);
		
		// add to allGenerations
		this.allGenerations.add(g);
		
        System.out.println();
		System.out.println(String.format("Best and worst expression trees for Generation %d are: ", genNumber));

        System.out.print("Best tree: ");
        g.getBest().readAsInfix(g.getBest().root);

        System.out.print("\nWorst tree: ");
        g.getWorst().readAsInfix(g.getWorst().root);

		System.out.println();
        System.out.println();

	} // end createGeneration()

    // nextGeneration()
    public void nextGeneration(Integer genNumber) {
		
		// get previous generation
		Generation prevGeneration = this.allGenerations.get(genNumber - 2);
		
		// construct HashMap that links each tree to its score
		HashMap<GPTree, Long> treeScoreMap = new HashMap<GPTree, Long>();
		for (int i = 0; i < prevGeneration.populationSize; i++) {
			GPTree t = prevGeneration.population.get(i);
			Long fitnessScore = t.fitness();
		    treeScoreMap.put(t, fitnessScore);
		}
		
		// list to hold children
		ArrayList<GPTree> children = new ArrayList<GPTree>();

		// elitism
		Integer numOfEliteChildren = (this.elitismPercent * prevGeneration.populationSize) / 100;
		
		for (int i = 0; i < numOfEliteChildren; i++) {
			Long minScore = Long.MAX_VALUE;
            GPTree best = null;
			for (GPTree tree : treeScoreMap.keySet()) {
				Long score = treeScoreMap.get(tree); 
				if (score < minScore) {
					minScore = score;
					best = tree;
				}
			}

            GPTree eliteChild = new GPTree(this.inputVar);
            eliteChild.root.data = best.root.data;
            eliteChild.root.left = best.root.left;
            eliteChild.root.right = best.root.right;
            children.add(eliteChild);
            treeScoreMap.remove(best);
		}

        // Truncation
        if (this.selectionType == "Truncation") {
            
            // after elitism, extract an arraylist of trees that would survive
			ArrayList<GPTree> survived = new ArrayList<GPTree>();
			for (int i = 0; i < (prevGeneration.populationSize / 2) - (children.size() / 2); i++) {
				Long minScore = Long.MAX_VALUE;
				GPTree best = null;
				for (GPTree tree : treeScoreMap.keySet()) {
					Long score = treeScoreMap.get(tree); 
					if (score < minScore) {
						minScore = score;
						best = tree;
					}
				}
				survived.add(best);
				treeScoreMap.remove(best);
			}

            // utils for crossover
            Random randomizer = new Random();
            Integer crossoverCount = 0;

            // crossover among the survived (top 50%) parent, before mutation
            if (crossover) {

                for (GPTree tree : survived) {

                    GPTree crossoverMatch = survived.get(randomizer.nextInt(survived.size()));

                    tree.crossWith(crossoverMatch);
                    crossoverCount++;
                }
            }

            // produce children and mutate
            for (GPTree tree : survived) {

                GPTree child1 = new GPTree(this.inputVar);
                child1.root.data = tree.root.data;
                child1.root.left = tree.root.left;
                child1.root.right = tree.root.right;
                if (mutation) {child1.mutateThrough(child1.root);}
                children.add(child1);

                GPTree child2 = new GPTree(this.inputVar);
                child2.root.data = tree.root.data;
                child2.root.left = tree.root.left;
                child2.root.right = tree.root.right;
                if (mutation) {child2.mutateThrough(child2.root);}
                children.add(child2);
            }

            System.out.println("     " + crossoverCount + " crossovers complete");
			System.out.println("     " + "Truncation successful with " + numOfEliteChildren + " elite children");
        } // end truncation if statement

        
        Generation newGeneration = new Generation(genNumber, prevGeneration.populationSize, this.inputVar);
        
        newGeneration.population = children;
        this.allGenerations.add(newGeneration);

        System.out.println();
		System.out.println(String.format("Best and worst expression trees for Generation %d are: ", genNumber));

        System.out.print("Best tree: ");
        newGeneration.getBest().readAsInfix(newGeneration.getBest().root);

        System.out.print("\nWorst tree: ");
        newGeneration.getWorst().readAsInfix(newGeneration.getWorst().root);

		System.out.println();
        System.out.println();

    } // end nextGeneration()
    
    // writeToCSV()
    public void writeToCSV() {

        // instantiate writer and file
        PrintWriter csvWriter;
        File csvFile = new File("source_code/plot_data.csv");

        // create file, disregard overwriting
        try {
            csvFile.createNewFile();
        } catch (IOException e) {
            System.err.println("CSV file not created");
        }

        // write data
        try {
            csvWriter = new PrintWriter(csvFile);

            // first row
            csvWriter.print("generation,bestFitness,worstFitness\n");

            // each row is a generation
            for (int genNum = 1; genNum <= this.allGenerations.size(); genNum++) {
                Generation thisGen = this.allGenerations.get(genNum - 1);

                csvWriter.printf("%d,%d,%d\n", genNum, thisGen.getBest().fitness(), thisGen.getWorst().fitness());
            }

            //
            System.out.println("Evolution data successfully written to plot_data.csv");
            System.out.println();

            // close writer
            csvWriter.flush();
            csvWriter.close();

        } catch (FileNotFoundException e) {
            System.err.println("CSV file not found");
        }
    }
}
