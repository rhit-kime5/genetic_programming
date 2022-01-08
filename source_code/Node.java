package source_code;

import java.util.Random;

/**
 * Class: Node
 * @author Edward Kim
 * <br>Purpose: Used to represent a single node in a binary expression tree
 * <br>For example:
 * <pre>
 * 		Node plusNode = new Node("+");
 *      Node xNode = new Node("x");
 *      Node numNode = new Node("10");
 * </pre>
 */
public class Node {
    public String data;
    public Node left;
    public Node right;
    public String inputVar;

    public Node(String data) {
        this.data = data;
        this.left = null;
        this.right = null;
        this.inputVar = null;
    }


    // NOTE: no longer used, replaced with mutateNode() in GPTree that allows architecture alteration
    public void mutate() {

        String d = this.data;
        
         // 10% probability, switch terminal node to different value, switch function node to different function
        if (new Random().nextInt(10) < 1) {

            // if function node, switch to strictly different function
            if (d == "+") {

                if (new Random().nextInt(2) == 0) {
                    this.data = "-";
                } else {
                    this.data = "*";
                }

            } else if (d == "-") {

                if (new Random().nextInt(2) == 0) {
                    this.data = "*";
                } else {
                    this.data = "+";
                }

            } else if (d == "*") {

                if (new Random().nextInt(2) == 0) {
                    this.data = "+";
                } else {
                    this.data = "-";
                }

            // if terminal node, switch to new random value
            } else if (d == this.inputVar || (-20 <= Integer.parseInt(d) && Integer.parseInt(d) <= 20)) {

                Integer terminalId = new Random().nextInt(42);
    
                if (terminalId == 41) {
                    this.data = this.inputVar;
                } else if (terminalId < 21) {
                    this.data = terminalId.toString();
                } else if (20 < terminalId && terminalId < 41) {
                    this.data = ((Integer) (-1 * (terminalId - 20))).toString();
                }

            } // end if statement

        } // end probability if statement

    }
}