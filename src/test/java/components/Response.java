package components;

import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Support class for HTTP Response - provides communication for tests
 */
public class Response {
    private String q = "";
    private String limit = "10";
    private String api_key = "bUGDBAhw9egpqMGtEAs2OSgmQ2ScgaLp1po8MDjZ";
    private String basic_url = "https://api.nasa.gov/planetary/sounds?";

    public void setQ(String q) {
        this.q = q;
        this.basic_url += "&q=" + this.q;
    }

    public void setLimit(String limit) {
        this.limit = limit;
        this.basic_url += "&limit=" + this.limit;
    }

    public void setApi_key(String api_key) {
        this.api_key = api_key;
        this.basic_url += "&api_key=" + this.api_key;
    }

    /**
     * Return response - header
     * @return HttpResponse return response of request
     * @throws IOException e
     */
    public HttpResponse getResponse() throws IOException {
        RequestBuilder uri = RequestBuilder.get(this.basic_url);
        HttpUriRequest builtUri = uri.build();
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(builtUri);

        return httpResponse;
    }

    /**
     * Return status code (404, 403, 200) of request
     * @return int status code of request
     * @throws IOException e
     */
    public int getStatusCode() throws IOException {
        return this.getResponse().getStatusLine().getStatusCode();
    }

    /**
     * Returns body of response - JSON
     * @return JSONObject - body of response
     * @throws IOException
     * @throws ParseException
     */
    public JSONObject getJSONresponse() throws IOException, ParseException {

        BufferedReader reader;

        URL uri = new URL(basic_url);
        HttpURLConnection connection = (HttpURLConnection) uri.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();

        reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        JSONParser jsonParser = new JSONParser();
        JSONObject json = (JSONObject) jsonParser.parse(reader);

        return json;
    }

    /**
     * Return part of JSON - array of records
     * @return JSONArray - array of results (records)
     * @throws IOException
     * @throws ParseException
     */
    public JSONArray getResults() throws IOException, ParseException {
        return (JSONArray) this.getJSONresponse().get("results");
    }
}
