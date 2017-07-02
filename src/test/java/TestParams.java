import components.Response;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.testng.*;
import org.testng.annotations.Test;

import java.io.IOException;

public class TestParams {
    private final String DEFAULT_API_KEY = "bY5kzsi2gefCwz8l9INHELg5BYgTfLTLwnZWWKma";
    Response r;
    JSONObject json;

    /**
     * DataProvider for param limit.
     * @return Object[][] with two values - String (given limit), int (expected count of results)
     */
    @org.testng.annotations.DataProvider
    public Object[][] limitProvider() {
        int DEFAULT_LIMIT = 10;
        return new Object[][]{
                {"0", 0}, {"1", 1},
                {"10", 10}, {"-10", DEFAULT_LIMIT},
                {"50", 50}, {"limit", DEFAULT_LIMIT},
                {"What", DEFAULT_LIMIT}, {"$%^&*(", DEFAULT_LIMIT}
        };
    }

    /**
     * DataProvider for param q (query)
     * @return Object[][] with one value - String (given string for)
     */
    @org.testng.annotations.DataProvider
    public Object[][] queryProvider() {
        return new Object[][]{{"Voyager"}, {"Apollo"}, {"123"}, {""}, {"null"}};
    }

    /**
     * DataProvider for param api_key
     * @return Object[][] with 2 values - String (api_key) and int (expected returned status code of server)
     */
    @org.testng.annotations.DataProvider
    public Object[][] keyProvider() {
        return new Object[][]{{"bY5kzsi2gefCwz8l9INHELg5BYgTfLTLwnZWWKma", 200},
                {"bUGDBAhw9egpqMGtEAs2OSgmQ2ScgaLp1po8MDjZ", 200},
                {"DEMO_KEY", 200},
                {"563", 403},
                {"null", 403},
                {"CookieMonster", 403},
                {"", 403}
        };
    }

    /**
     * Test default limit - it should return 10 records.
     * https://api.nasa.gov/api.html#sounds
     * @throws IOException e
     * @throws ParseException e
     */
    @Test
    public void defaultLimitIs10() throws IOException, ParseException {
        r = new Response();
        r.setApi_key(DEFAULT_API_KEY);
        json = r.getJSONresponse();
        JSONArray jsonArray = r.getResults();

        Assert.assertEquals(((Long) json.get("count")).intValue(), 10, "Default limit is 10.");
        Assert.assertEquals(jsonArray.size(), 10);
    }

    /**
     * Test param limit. It tests, if value of count is same as given limit.
     * @param limit int - To show only limited amount of records.
     * @throws IOException e
     * @throws ParseException e
     */
    @Test(dataProvider = "limitProvider")
    public void test_limit(String limit, int expectedLimit) throws IOException, ParseException {
        r = new Response();
        r.setApi_key(DEFAULT_API_KEY);
        r.setLimit(limit);
        json = r.getJSONresponse();

        Assert.assertEquals(((Long) json.get("count")).intValue(), expectedLimit,
                "Limit is set to " + limit + " and count should be " + expectedLimit + ".");

        JSONArray jsonArray = r.getResults();
        Assert.assertEquals(jsonArray.size(), expectedLimit);
    }

    /**
     * Test param q (query). It tests, if given string is in every record's title or description.
     * If not, it fail.
     * @param q String - filter for given word.
     * @throws IOException e
     * @throws ParseException e
     */
    @Test(dataProvider = "queryProvider")
    public void test_query_filter(String q) throws IOException, ParseException {
        r = new Response();
        r.setApi_key(DEFAULT_API_KEY);
        r.setQ(q);

        boolean queryPresent = true;
        JSONArray jsonArray = r.getResults();
        String[] invalidStrings = new String[2];
        for (JSONObject record : (Iterable<JSONObject>) jsonArray) {
            boolean[] isPresent = {true, true};
            String[] arrayString = {"title", "description"};

            for (int i = 0; i < arrayString.length; i++) {
                if (record.get(arrayString[i]) != null) {
                    if (!record.get(arrayString[i]).toString().contains(q)) {
                        isPresent[i] = false;
                    }
                } else {
                    isPresent[i] = false;
                }
            }

            if (!(isPresent[0] || isPresent[1])) {
                invalidStrings[0] = record.get(arrayString[0]).toString();
                invalidStrings[1] = record.get(arrayString[1]).toString();
                queryPresent = false;
                break;
            }
        }
        Assert.assertTrue(queryPresent, "Filter on word \"" + q
                + "\" is not in every record. \n" + invalidStrings[0] + "\n" + invalidStrings[1] + "\n");
    }

    /**
     * It tests if you need valid api_key for getting results.
     * @param api_key String - Given API_KEY (you can generate it at (https://api.nasa.gov/index.html#apply-for-an-api-key)
     * @param expectedResult int - Expected Status code (404, 200, 403)
     * @throws IOException e
     * @throws ParseException e
     */
    @Test(dataProvider = "keyProvider")
    public void test_api_key(String api_key, int expectedResult) throws IOException, ParseException {
        r = new Response();
        r.setApi_key(api_key);

        Assert.assertEquals(r.getStatusCode(), expectedResult);
    }
}
