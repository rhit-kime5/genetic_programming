package source_code;

import java.util.ArrayList;

/**
 * Class: Generation
 * @author Edward Kim
 * <br>Purpose: Used to represent a specific generation of the population during evolution
 * <br>For example:
 * <pre>
 * 		Generation firstGen = new Generation(1, 1000, "x");
 * </pre>
 */
public class Generation {

    // Fields
    public ArrayList<GPTree> population = new ArrayList<GPTree>();
	public Integer genNumber;
	public Integer populationSize;
    public String inputVar;


    // Constructor (generate a population of random trees)
    public Generation(Integer genNumber, Integer populationSize, String inputVar) {
        this.genNumber = genNumber;
        this.populationSize = populationSize;
        this.inputVar = inputVar;

        for (int i = 0; i < this.populationSize; i++) {
            this.population.add(new GPTree(inputVar));			

		} // end for loop

    } // end constructor

    // getBest()
    public GPTree getBest() {
        Long lowestError = Long.MAX_VALUE;
        GPTree bestTree = null;
		
		for (GPTree t : this.population) {

			if (t.fitness() < lowestError) {
				lowestError = t.fitness();
                bestTree = t;
			}
		}
		return bestTree;
    }

    // getWorst()
    public GPTree getWorst() {
        Long highestError = 0L;
        GPTree worstTree = null;
		
		for (GPTree t : this.population) {

			if (t.fitness() > highestError) {
				highestError = t.fitness();
                worstTree = t;
			}
		}
		return worstTree;
    }

    // getAverageFitness()
    public Long getAverageFitness() {
		Long errorSum = 0L;
		
		for (GPTree t : this.population) {
			errorSum += t.fitness();
		}
		return errorSum / this.populationSize;
	}     
}
