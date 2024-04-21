package org.example;

import com.inflectra.spiratest.addons.junitextension.SpiraTestCase;
import com.inflectra.spiratest.addons.junitextension.SpiraTestConfiguration;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

@SpiraTestConfiguration(
//following are REQUIRED
        url = "https://rmit.spiraservice.net",
        login = "s3961136",
        rssToken = "{273D95B4-8256-4A38-963A-51BE79F1B934}",
        projectId = 122

)

public class BasicDataTest {

    @Test
    @SpiraTestCase(testCaseId = 543)
    public void testPrint() {
        // Arrange
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        BasicData basicData = new BasicData(888, "Billy Butter", "p1234567#");

        // Act
        basicData.print();

        // Assert
        String expectedOutput = "\nID: 888\nName: Billy Butter\nPassword: p1234567#\n";
        assertEquals(expectedOutput, outContent.toString());
    }

}