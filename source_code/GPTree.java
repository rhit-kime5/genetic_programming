package source_code;

import java.util.Random;
// import java.util.function.Consumer;

/**
 * Class: GPTree
 * @author Edward Kim
 * <br>Purpose: Used to represent a binary expression tree, with numerous relevant methods
 * <br>For example:
 * <pre>
 * 		GPTree tree = new GPTree("x");
 * </pre>
 */
public class GPTree {

    // Fields
    public Node root;
    public String inputVar;

    public Long targetFunction(Long x) {
        return 10 * x;
        // return 50 * x;
        // return (5 * x * x) + (7 * x) + 19;
    }

    // final Consumer<Integer> targetFunction = GPTree::testFunction;
    //     private static Integer testFunction(Integer x) {return 10 * x;};

    // public Integer SIZE_MULTIPLIER = 0;

    // Main
    public static void main(String[] args) {

        // // testing
        // for (int i = 0; i < 10; i ++) {

        //     GPTree tree1 = new GPTree("x");

        //     System.out.println("\n____________________________________________");
        //     System.out.print("tree1, before crossover: ");
        //     tree1.readAsInfix(tree1.root);

        //     GPTree tree2 = new GPTree("x");

        //     System.out.print("\ntree2, before crossover: ");
        //     tree2.readAsInfix(tree2.root);

        //     tree1.crossWith(tree2);

        //     System.out.print("\ntree1, after crossover: ");
        //     tree1.readAsInfix(tree1.root);

        //     System.out.print("\ntree2, after crossover: ");
        //     tree2.readAsInfix(tree2.root);
        // }


        // testing
        for (int i = 0; i < 100; i ++) {

            GPTree tree = new GPTree("x");

            tree.readAsInfix(tree.root);

            System.out.print(" = " + tree.evaluateUpTo(tree.root, 5L) + " for x = 5, has " + tree.size(tree.root) 
            + " node" + ", Fitness = " + tree.fitness() + "\n");

            // System.out.print(" node chosen: " + tree.chooseRandNode().data + "\n");

            // tree.readAsInfix(tree.root);
            // tree.mutateThrough(tree.root);
            // System.out.print(" after mutation --> ");
            // tree.readAsInfix(tree.root);
            // System.out.println();


        }

    } // end main


    // Constructor (generate a random tree)
    public GPTree(String inputVar) {
        this.inputVar = inputVar;

        this.root = new Node(this.chooseNodeVal());
        this.root.inputVar = inputVar;
        this.buildFrom(this.root);

    } // end constructor


    // chooseNodeVal()
    public String chooseNodeVal() {

        String nodeValue = null;
    
        // 90% probability, terminal node (inputVar or -20 to 20; 42 cases total)
        if (new Random().nextInt(10) > 0) {

            Integer terminalId = new Random().nextInt(42);
    
            if (terminalId == 41) {
                nodeValue = this.inputVar;
            } else if (terminalId < 21) {
                nodeValue = terminalId.toString();
            } else if (20 < terminalId && terminalId < 41) {
                nodeValue = ((Integer) (-1 * (terminalId - 20))).toString();
            }
    
        // 10% probability, function node (+ or - or *; 3 cases)
        } else {

            Integer functionId = new Random().nextInt(3);
    
            if (functionId == 0) {
                nodeValue = "+";
            } else if (functionId == 1) {
                nodeValue = "-";
            } else if (functionId == 2) {
                nodeValue = "*";
            }

        } // end if statement

        return nodeValue;

    } // end chooseNodeVal()

    // buildFrom(), recursive
    public void buildFrom(Node top) {

        String d = top.data;

        // if function,
        if (d == "+" || d == "-" || d == "*") {

            // create the left node
            top.left = new Node(chooseNodeVal());
            top.left.inputVar = this.inputVar;

            // build the tree from the left node
            buildFrom(top.left);

            // when done with left, create the right node
            top.right = new Node(chooseNodeVal());
            top.right.inputVar = this.inputVar;

            // build the tree from the right node
            buildFrom(top.right);

        // if terminal,
        } else if (d == this.inputVar || (-20 <= Integer.parseInt(d) && Integer.parseInt(d) <= 20)) {
            return;
        }

    } // end buildFrom()
    
    // readAsInfix(), recursive
    public void readAsInfix(Node top) {
        if (top != null) {
            System.out.print("(");
            readAsInfix(top.left);
            System.out.print(" " + top.data + " ");
            readAsInfix(top.right);
            System.out.print(")");
        }
    }

    // evaluateUpTo(), recursive
    public Long evaluateUpTo(Node top, Long xValue) {

        String d = top.data;

        // if function, recursion
        if (d == "+") {
            return evaluateUpTo(top.left, xValue) + evaluateUpTo(top.right, xValue);
        } else if (d == "-") {
            return evaluateUpTo(top.left, xValue) - evaluateUpTo(top.right, xValue);
        } else if (d == "*") {
            return evaluateUpTo(top.left, xValue) * evaluateUpTo(top.right, xValue);

        // if terminal with input variable (e.g. "x"),
        } else if (d == this.inputVar) {
            return xValue;

        // if terminal with number,
        } else if (-20 <= Integer.parseInt(d) && Integer.parseInt(d) <= 20) {
            return Long.parseLong(d);
        }

        return 0L;

    } // end evaluateUpTo()

    // size(), recursive
    public Integer size(Node top) {

        String d = top.data;

        // if function,
        if (d == "+" || d == "-" || d == "*") {

            return 1 + size(top.left) + size(top.right);

        // if terminal,
        } else if (d == this.inputVar || (-20 <= Integer.parseInt(d) && Integer.parseInt(d) <= 20)) {
            return 1;
        }

        return 0;

    } // end size()

    // fitness()
    public Long fitness() {

        Long sum = 0L;

        for (Long x = -50L; x <= 50L; x++) {

            Long difference = Math.abs(evaluateUpTo(this.root, x) - targetFunction(x));

            sum += (difference * difference);

        }

        // sum += size(this.root) * this.SIZE_MULTIPLIER;

        // handles error with negative fitness
        if (sum < 0) {
            return 2147483647L;     // numerically equivalent to Integer.MAX_VALUE
        }

        return sum;
    }

    // chooseRandNode()
    public Node chooseRandNode() {

        Integer position = new Random().nextInt(size(this.root));

        return chooseRandNodeHelper(this.root, position);
    }

    // chooseRandNodeHelper(), recursive
    public Node chooseRandNodeHelper(Node top, Integer position) {

        if (position == 0) {
            return top;
        }

        if (top.left != null) {
            return chooseRandNodeHelper(top.left, position - 1);
        } else if (top.right != null) {
            return chooseRandNodeHelper(top.right, position - 1);
        } else {
            return chooseRandNodeHelper(top, position - 1);
        }
    }

    // mutateThrough(), recursive
    public void mutateThrough(Node top) {
        if (top != null) {
            mutateThrough(top.left);

            // 10% probability of mutation happening
            if (new Random().nextInt(10) < 1) {
                mutateNode(top);
            }

            mutateThrough(top.right);
        }
    }

    // mutateNode()
    public void mutateNode(Node toMutate) {

        // determine node type
        boolean wasFunction = (toMutate.data == "+" || toMutate.data == "-" || toMutate.data == "*");
        boolean wasTerminal = !wasFunction;

        // choose and assign new random value for node data (10% function node, 90% terminal node)
        toMutate.data = chooseNodeVal();

        // determine node type after mutation
        boolean isFunction = (toMutate.data == "+" || toMutate.data == "-" || toMutate.data == "*");
        boolean isTerminal = !isFunction;

        // handle child nodes accordingly
        if (wasFunction && isFunction) {
            ; // do nothing

        } else if (wasFunction && isTerminal) {
            toMutate.left = null;
            toMutate.right = null;

        } else if (wasTerminal && isFunction) {
            buildFrom(toMutate);

        } else if (wasTerminal && isTerminal) {
            ; // do nothing
        }
    }

    // crossWith()
    public void crossWith(GPTree tree) {

        // choose node to swap from this tree
        Node fromThis = chooseRandNode();

        // save data of chosen node
        Node save = new Node(fromThis.data);
        save.left = fromThis.left;
        save.right = fromThis.right;

        // choose node to swap from the match tree
        Node fromMatch = tree.chooseRandNode();

        // transfer subtree from match tree to this tree
        fromThis.left = fromMatch.left;
        fromThis.right = fromMatch.right;
        fromThis.data = fromMatch.data;

        // transfer subtree from this tree to match tree
        fromMatch.left = save.left;
        fromMatch.right = save.right;
        fromMatch.data = save.data;     

    } // crossWith()

} // end class
