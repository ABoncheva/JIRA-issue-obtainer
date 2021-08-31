package net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.JsonNode;
import dto.Issue;
import dto.Issues;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.*;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;

public class Response {

    public Response(final int total, final Collection<Issue> issues) {
        this.total = total;
        this.issues = (Set<Issue>) issues;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Response response = (Response) o;
        return total == response.total && Objects.equals(issues, response.issues);
    }

    @Override
    public int hashCode() {
        return Objects.hash(total, issues);
    }

    public static Response fromJson(final JsonNode jsonNode) {
        Response parsedResponse;
        Gson gson = new GsonBuilder().create();

        parsedResponse = gson.fromJson(jsonNode.toString(), Response.class);

        return parsedResponse;
    }

    public void saveIssuesToJson() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (Writer writer = new BufferedWriter(new FileWriter("Issues.json"))) {
            gson.toJson(issues, writer);
            writer.flush();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public void saveIssuesToXml() {
        Issues issuesObj = new Issues(issues);
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Issues.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            jaxbMarshaller.marshal(issuesObj, new File("Issues.xml"));
        } catch (JAXBException e) {
            System.err.println(e.getMessage());
        }
    }

    public Set<Issue> getIssues() {
        return issues;
    }

    public void addIssues(final Collection<Issue> newIssues) {
        newIssues.stream().forEach((x) -> issues.add(x));
    }

    public int totalCount() {
        return total;
    }


    private int total;
    private Set<Issue> issues;
}
