package net.request;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import dto.comment.Comments;
import net.Response;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class Request {

    public static Response send(final String hostName) {
        try {
            Response firstResponse = requestIssues(hostName, 0);
            AtomicReference<Response> withAllIssues = new AtomicReference<>();

            if (firstResponse.totalCount() > MAX_RESULTS_PER_PAGE) {
                Thread requestMoreIssues = new Thread(() -> {
                    try {
                        withAllIssues.set(sendMoreIssuesRequestsAsync(hostName, firstResponse));
                    } catch (InterruptedException e) {
                        System.err.println(e.getMessage());
                    }
                });
                requestMoreIssues.start();
                requestMoreIssues.join();
                withAllIssues.set(CommentsRequest.requestComments(hostName, withAllIssues.get()));

                return withAllIssues.get();
            } else {
                return CommentsRequest.requestComments(hostName, firstResponse);
            }
        } catch (UnirestException | InterruptedException exception) {
            System.err.println(exception.getMessage());
            return null;
        }
    }

    private static String configureIssuesRequestUri(final String hostName, final int startAt) {
        String baseUri = hostName + SEARCH_PATH;

        return configureRequestUri(baseUri, startAt);
    }


    private static String configureRequestUri(final String baseUri, final int startAt) {
        String URI_PARAMETER_MAX_RESULTS_NAME = "maxResults";
        String URI_PARAMETER_MAX_RESULTS_VALUE = String.valueOf(MAX_RESULTS_PER_PAGE);
        String URI_PARAMETER_START_AT_NAME = "startAt";
        String URI_PARAMETER_START_AT_VALUE = String.valueOf(startAt);

        HttpGet httpGet = new HttpGet(baseUri);
        try {
            URI uri = new URIBuilder(httpGet.getURI())
                    .addParameter(URI_PARAMETER_MAX_RESULTS_NAME, URI_PARAMETER_MAX_RESULTS_VALUE)
                    .addParameter(URI_PARAMETER_START_AT_NAME, URI_PARAMETER_START_AT_VALUE)
                    .build();
            return uri.toString();
        } catch (URISyntaxException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }


    private static Response requestIssues(final String hostName, final int startAt) throws UnirestException {
        JsonNode serverResponse = Unirest.get(configureIssuesRequestUri(hostName, startAt))
                .queryString(Query.getQueryName(), Query.getQueryValue())
                .asJson()
                .getBody();

        return Response.fromJson(serverResponse);
    }

    private static Response sendMoreIssuesRequestsAsync(final String hostName, final Response previousResponse) throws InterruptedException {
        Set<Future<HttpResponse<JsonNode>>> responses = new HashSet<>();
        int requestsCount = requestsToBeMade(previousResponse);
        Thread makeRequests = new Thread(() -> {
            for (var i = 1; i <= requestsCount; ++i) {
                responses.add(Unirest.get(configureIssuesRequestUri(hostName, MAX_RESULTS_PER_PAGE * i))
                        .queryString(Query.getQueryName(), Query.getQueryValue())
                        .asJsonAsync());
            }
        });
        makeRequests.start();
        makeRequests.join();
        Response withAllIssues = parseIssuesResponses(responses, previousResponse, requestsCount);

        return withAllIssues;
    }


    private static Response parseIssuesResponses(final Set<Future<HttpResponse<JsonNode>>> responses,
                                                 final Response previousResponse, final int requestsCount) {
        Response withAllIssues = previousResponse;
        boolean waitingForResponses = true;
        Set<JsonNode> responsesBodies = new HashSet<>(requestsCount);

        while (waitingForResponses) {
            if (responses.stream().allMatch((x) -> x.isDone())) {
                responsesBodies = responses.parallelStream().map((x) -> {
                    try {
                        return x.get().getBody();
                    } catch (InterruptedException | ExecutionException e) {
                        System.err.println(e.getMessage());
                    }
                    return null;
                }).collect(Collectors.toSet());
                waitingForResponses = false;
            }
        }
        responsesBodies.parallelStream().map((x) -> Response.fromJson(x)).forEach((x) -> withAllIssues.addIssues(x.getIssues()));

        return withAllIssues;
    }

    private static int requestsToBeMade(final Response firstResponse) {
        int totalIssues = firstResponse.totalCount();
        int requestsToBeMade;

        if (totalIssues % MAX_RESULTS_PER_PAGE == 0) {
            requestsToBeMade = (totalIssues / MAX_RESULTS_PER_PAGE) - 1;
        } else {
            requestsToBeMade = totalIssues / MAX_RESULTS_PER_PAGE;
        }

        return requestsToBeMade;
    }

    private static class CommentsRequest {

        private static Response requestComments(final String hostName, final Response withoutComments) {
            Response toBeUpdated = withoutComments;

            toBeUpdated.getIssues().parallelStream().forEach((x) -> {
                try {
                    var currentResponse = Unirest.get(configureCommentsRequestUri(hostName, x.getKey()))
                            .asJson()
                            .getBody();
                    x.setComments(fromJson(currentResponse));
                } catch (UnirestException e) {
                    System.err.println(e.getMessage());
                }
            });

            return toBeUpdated;
        }

        private static Comments fromJson(final JsonNode response) {
            Comments comments;
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            comments = gson.fromJson(response.toString(), Comments.class);

            return comments;
        }

        private static String configureCommentsRequestUri(final String hostName, final String key) {
            String baseUri = hostName + COMMENT_PATH + key + "/comment";

            return configureRequestUri(baseUri, 0);
        }

        private static final String COMMENT_PATH = "/rest/api/latest/issue/";

    }

    private static final String SEARCH_PATH = "/rest/api/latest/search/";
    private static final int MAX_RESULTS_PER_PAGE = 100;
}

