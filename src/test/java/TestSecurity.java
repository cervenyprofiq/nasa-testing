import components.Response;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.io.IOException;

public class TestSecurity {
    private final String DEFAULT_API_KEY = "bY5kzsi2gefCwz8l9INHELg5BYgTfLTLwnZWWKma";

    Response r;

    @Test()
    public void test_check_record_values_via_regex() throws IOException, ParseException {
        for (int i = 0; i < 10; i++) {
            new Thread("" + i) {
                public void run() {
                    r = new Response();
                    r.setApi_key(DEFAULT_API_KEY);
                    r.setLimit("1");

                    try {
                        System.out.println(r.getStatusCode());
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println(e);
                    } finally {
                        System.out.println("WHAT");
                    }
                }
            }.start();
        }
    }
}

