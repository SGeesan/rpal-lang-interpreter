import java.io.File;
import java.util.List;

import com.rpal.cse.CSE;
import com.rpal.cse.CSNode;
import com.rpal.lex.LexicalAnalyzer;
import com.rpal.lex.Token;
import com.rpal.parser.AST;
import com.rpal.parser.Parser;

public class myrpal {
    public static void main(String[] args) {
        // Check if a filename is given
        if (args.length < 1) {
            System.out.println("Usage: java com.rpal.Main <sourcefile.rpal>");
            return;
        }
        
        boolean astSwitch = false;
        boolean stSwitch = false;
        // Read switches
        for (int i = 0; i < args.length - 1; i++) {
            switch (args[i]) {
                case "-ast":
                    astSwitch = true;
                    break;
                case "-st":
                    stSwitch = true;
                    break;
                default:
                    System.out.println("Unknown option: " + args[i]);
            }
        }
        String filename = args[args.length - 1];

        // Generate the lexical analyser targeting given file
        File file = new File(filename);
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(file);

        // Tokanize the input
        List<Token> tokenList = lexicalAnalyzer.getTokenList();
        
        // Build AST
        Parser parser = new Parser(tokenList);
        AST tree = parser.buildAst();
        //Print if required
        if (astSwitch) {
            tree.print();
        }
        
        // Standardise AST
        tree.standardize();
        //Print if required
        if (stSwitch) {
            tree.print();
        }

        //Get Control Structures
        List<List<CSNode>> controlList = tree.getCS();

        // Interpret
        CSE cse_machine = new CSE(controlList);
        cse_machine.runCSE();
        System.out.println();
    }
}