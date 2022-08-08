package com.bouff.oids;

import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.Scanner;
import java.util.Set;

import com.bouff.oids.app.OidApp;
import com.bouff.oids.app.OidAppImpl;
import com.bouff.oids.loader.OidLoader;
import com.bouff.oids.loader.YamlOidLoader;
import com.bouff.oids.validator.BasicOidValidator;
import com.bouff.oids.validator.OidValidator;

/**
 * The launcher for the application.
 */
public final class OidAppLauncher {

    /**
     * A constant for the quit command.
     */
    private static final String QUIT_COMMAND = "quit";

    /**
     * The mainline. Process user input via STDIN and display output via STDOUT (or STDERR on error).
     *
     * @param args the command line arguments. The first and only (mandatory) argument is the path for the OID loader.
     */
    public static void main(String[] args) {

        if (args.length < 1) {
            System.err.println("Error: path to OIDs must be specified.");
            System.exit(1);
        }

        OidValidator oidValidator = new BasicOidValidator();
        OidLoader loader = new YamlOidLoader(oidValidator);
        Set<String> oids = Collections.emptySet();

        try {
            oids = loader.load(args[0]);
        } catch (FileNotFoundException fnfe) {
            System.err.println("Error: Loader: cannot process file '" + args[0] + "'.");
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Error: Loader: " + e.getMessage());
            System.exit(1);
        }

        OidApp app = new OidAppImpl(oids);

        System.out.println(
                "Enter an OID to process.\n" +
                "Enter 'quit' to exit at any time.\n\n");

        Scanner inputScanner = new Scanner(System.in);
        try {
            while (true) {
                String inputLine = inputScanner.nextLine();
                if (QUIT_COMMAND.equalsIgnoreCase(inputLine)) {
                    break;
                } else if (oidValidator.isValid(inputLine)) {
                    System.out.println(inputLine + ": " + app.isOidDescendant(inputLine) + "\n");
                } else {
                    System.err.println("Error: OID '" + inputLine + "' is invalid.\n");
                }
            }
            System.out.println("Exiting.");
            System.exit(0);
        }
        catch (Exception e) {
            System.err.println("Exiting. Exception: " + e.getMessage());
            System.exit(1);
        }
    }
}
