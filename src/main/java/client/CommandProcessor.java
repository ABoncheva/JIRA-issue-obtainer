package client;

import net.Response;
import net.request.Request;

import javax.naming.OperationNotSupportedException;
import java.util.Scanner;

public class CommandProcessor {

    public void processCommand() throws OperationNotSupportedException {
        while (true) {
            if (!processSendingRequestAndSavingToFile(new Scanner(System.in))) {
                break;
            }
        }
    }

    private  boolean processSendingRequestAndSavingToFile(Scanner inputScanner) throws OperationNotSupportedException {
        System.out.println("Enter host name (e.g https://jira.atlassian.com). (For quitting press q/Q):" + System.lineSeparator());
        String userInput = inputScanner.nextLine();

        if (userInput.compareToIgnoreCase(QUIT_COMMAND) == 0) {
            return false;
        } else {
            var result = Request.send(userInput);
            if (!processSavingToFile(inputScanner, result)) {
                return false;
            }
        }
        return true;
    }

    private boolean processSavingToFile(Scanner inputScanner, Response result) throws OperationNotSupportedException {
        System.out.println("Choose a file format. For JSON press J/j, for XML press X/x. (For quitting press q/Q):" + System.lineSeparator());
        String userInput = inputScanner.nextLine();

        if (userInput.compareToIgnoreCase(QUIT_COMMAND) == 0) {
            return false;
        } else {
            if (userInput.compareToIgnoreCase(JSON_OUTPUT_COMMAND) == 0) {
                result.saveIssuesToJson();
            } else if (userInput.compareToIgnoreCase(XML_OUTPUT_COMMAND) == 0) {
                result.saveIssuesToXml();
            } else {
                throw new OperationNotSupportedException();
            }
        }
        return true;
    }

    private static final String QUIT_COMMAND = "Q";
    private static final String JSON_OUTPUT_COMMAND = "J";
    private static final String XML_OUTPUT_COMMAND = "X";
}
