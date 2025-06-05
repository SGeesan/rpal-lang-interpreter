import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import com.rpal.cse.CSE;
import com.rpal.cse.CSE_Exception;
import com.rpal.cse.CSNode;
import com.rpal.lex.LexicalAnalyzer;
import com.rpal.lex.Token;
import com.rpal.parser.AST;
import com.rpal.parser.Parser;
import com.rpal.parser.ParserException;

public class myrpal {
    public static void main(String[] args) {
        // Check if a filename is given
        if (args.length < 1) {
            System.out.println("Usage: java myrpal [-st] [-ast] <sourcefile>");
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
        LexicalAnalyzer lexicalAnalyzer;
        try {
            lexicalAnalyzer = new LexicalAnalyzer(file);
        } catch (FileNotFoundException e) {
            System.out.println("File is not found : "+filename);
            return;
        } catch (Exception e) {
            System.out.println("Read Error Occured.");
            return;
        }

        // Tokanize the input
        List<Token> tokenList = lexicalAnalyzer.getTokenList();
        
        // Build AST
        Parser parser = new Parser(tokenList);
        AST tree;
        try {
            tree = parser.buildAst();
        }
        catch (ParserException e) {
            System.out.println("Syntax Error : \n"+e.getMessage());
            return;
        }
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
        try {
            cse_machine.runCSE();
        } catch (CSE_Exception e) {
            System.out.println("Evaluation failed: \n"+e.getMessage());
        }
    }
}