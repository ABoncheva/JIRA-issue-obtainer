package client;

import javax.naming.OperationNotSupportedException;


class MyJiraRestClient {

    public static void main(String[] args) {
        MyJiraRestClient client = new MyJiraRestClient();
        try {
            client.commandProcessor.processCommand();
        } catch (OperationNotSupportedException e) {
            System.err.println(e.getMessage());
        }
    }

    private CommandProcessor commandProcessor = new CommandProcessor();
}
