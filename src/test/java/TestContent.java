import components.Response;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class TestContent {
    private final String DEFAULT_API_KEY = "bY5kzsi2gefCwz8l9INHELg5BYgTfLTLwnZWWKma";

    Response r;

    /**
     * Check if every record has every key inside.
     * @throws IOException e
     * @throws ParseException e
     */
    @Test
    public void test_check_record_keys() throws IOException, ParseException {
        String[] keys = {"description", "license", "download_url", "duration",
                "last_modified", "stream_url", "tag_list", "id", "title"};
        r = new Response();
        r.setApi_key(DEFAULT_API_KEY);
        r.setLimit("100");

        JSONArray jsonArray = r.getResults();

        for (JSONObject record : (Iterable<JSONObject>) jsonArray) {
            for (String key : keys) {
                Assert.assertTrue(record.containsKey(key));
            }
        }
    }

    /**
     * DataProvider for testing values of every record.
     * @return Object[][] with two values - String (key), String(regex, which is checked with value)
     */
    @org.testng.annotations.DataProvider
    public Object[][] regexProvider() {
        return new Object[][]{
                {"license", "cc-by-nc|cc-by-nc-sa"},
                {"download_url", "https://api.soundcloud.com/tracks/\\d{9,}/download"}, // \\d{9,} 9+digits
                {"duration", "\\d{2,}"}, // 2+digits
                {"last_modified", "\\d{4}\\/\\d{1,2}\\/\\d{1,2} \\d{2}:\\d{2}:\\d{2} \\+\\d{4}"}, // date
                {"stream_url", "https://api.soundcloud.com/tracks/\\d{9,}/stream"}, // 9+digits
                {"tag_list", "[a-zA-Z]+"}, // words
                {"id", "\\d{2,}"} // 2+digits
        };
    }

    /**
     * Test values to valid format given by DataProvider
     * @param key String - key of record
     * @param regex String - regex for check value
     * @throws IOException e
     * @throws ParseException e
     */
    @Test(dataProvider = "regexProvider")
    public void test_check_record_values_via_regex(String key, String regex) throws IOException, ParseException {

        r = new Response();
        r.setApi_key(DEFAULT_API_KEY);
        r.setLimit("100");

        JSONArray jsonArray = r.getResults();

        for (JSONObject record : (Iterable<JSONObject>) jsonArray) {
            Assert.assertTrue(record.get(key).toString().matches(regex));
        }
    }
}
