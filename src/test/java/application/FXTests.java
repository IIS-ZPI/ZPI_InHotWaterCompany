package application;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class FXTests extends ApplicationTest {

    @Override public void start(Stage stage) throws Exception {
        Main main = new Main();
        ((Application) main).start(stage);
    }

    @Test public void warningText_EmptyProductStateWholesale_EnterAllData() {
        Text text = find("#warning");
        clickOn("#checkButton");
        assertThat(text.getText(), equalTo("Enter all data"));
    }

    public <T extends Node> T find(final String query) {
        return lookup(query).query();
    }
}
