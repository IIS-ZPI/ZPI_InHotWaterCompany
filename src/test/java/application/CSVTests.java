package application;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import utils.CSV;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class CSVTests {

    private CSV csv;

    @Before
    public void init() {
        try {
            csv = new CSV("src/test/java/application/products.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void getHeader_RetrievedHeaderEqualToExpected() {
        String[] header = csv.getHeader();

        assertThat(header[0], equalTo("product"));
        assertThat(header[1], equalTo("category"));
        assertThat(header[2], equalTo("wholesale_price"));
    }

    @Test
    public void getRow_ArrayOutOfRange_ReturnedNull() {
        String[] strings = {"Some data"};

        for (int i = 0; i < 15; i++) {
            try {
                strings = csv.readRow();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        assertThat(strings, equalTo(null));
    }

    @Test
    public void getRow_ArrayInRange_ReturnedSpecifiedData() {
        String[] strings = {"Some data"};

        for (int i = 0; i < 5; i++) {
            try {
                strings = csv.readRow();
            } catch (IOException e) {
                System.out.println(e);
            }
        }

        assertThat(strings[0], equalTo("Fentanyl"));
        assertThat(strings[1], equalTo("Non-prescription-drug"));
        assertThat(strings[2], equalTo("13.58"));
    }

    @Test
    public void revert_ShouldBackToFirstLine() {
        String[] strings = {"Some data"};

        for (int i = 0; i < 5; i++) {
            try {
                strings = csv.readRow();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            csv.revert();
            strings = csv.readRow();
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertThat(strings[0], equalTo("apple"));
        assertThat(strings[1], equalTo("groceries"));
        assertThat(strings[2], equalTo("0.24"));
    }

}
