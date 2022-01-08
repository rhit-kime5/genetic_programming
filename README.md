# Genetic Programming: Usage Instructions

Use EvolutionController as the top-level GUI to run this software (run the main() method in EvolutionController). The evolution progress will be shown in the console print-out. 

Specify the desired solution by modifying targetFunction() in GPTree.

The detailed evolution data are written onto plot_data.csv for each evolution run.

# Further TODOs in mind
1.  TreeViewer to constantly visualize the best GPTree
2.  GenerationViewer to constantly visualize the population (class Generation)
3.  Visualize the console print-out messages on the EvolutionController as well, by using EvolutionComponent
4.  Add more selection methods (Roulette Wheel, Ranked) and enable through EvolutionController
5.  Implement custom mutation rate and enable through EvolutionController
6.  Calculate diversity by some measure and visualize on EvolutionController