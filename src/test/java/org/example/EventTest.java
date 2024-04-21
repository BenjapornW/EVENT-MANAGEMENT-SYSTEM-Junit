package org.example;

import com.inflectra.spiratest.addons.junitextension.SpiraTestCase;
import com.inflectra.spiratest.addons.junitextension.SpiraTestConfiguration;
import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static org.junit.jupiter.api.Assertions.*;

@SpiraTestConfiguration(
//following are REQUIRED
        url = "https://rmit.spiraservice.net",
        login = "s3961136",
        rssToken = "{273D95B4-8256-4A38-963A-51BE79F1B934}",
        projectId = 122

)

public class EventTest {
    public Event event;
    private ByteArrayOutputStream errorMessage;
    private final PrintStream originalOut = System.out;
    private final InputStream originalSystemIn = System.in;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() throws Exception {
        event = new Event();
        errorMessage = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errorMessage));
        System.setIn(originalSystemIn);
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void tearDown() throws Exception {
        System.setOut(originalOut);
        this.event = null;
        System.setIn(originalSystemIn);
    }

    // 1.   Test AdminLogin
    @Test
    @Tag("Login")
    @DisplayName("Test AdminLogin - Valid Inputs")
    @SpiraTestCase(testCaseId = 521)
    void testAdminLogin_ValidInputs() {
        String input = "Admin1\npass1\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        boolean result = event.AdminLogin();
        assertTrue(result);
    }

    @Test
    @Tag("Login")
    @DisplayName("Test AdminLogin - Invalid Inputs")
    @SpiraTestCase(testCaseId = 522)
    void testAdminLogin_InvalidInputs() {
        String input = "Admin777\nwrongpasssssss\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        boolean result = event.AdminLogin();
        assertFalse(result);
    }

    // 2.   Test StudentLogin
    @Test
    @Tag("Login")
    @DisplayName("Test StudentLogin - Valid Input")
    @SpiraTestCase(testCaseId = 520)
    public void testStudentLogin_ValidInput() {
        // Given
        event.Student.add(new BasicData(32144, "student32144", "p7654324#"));
        String simulatedUserInput = "32144\np7654324#\n";
        InputStream in = new ByteArrayInputStream(simulatedUserInput.getBytes(StandardCharsets.UTF_8));
        System.setIn(in);

        // When
        boolean result = event.StudentLogin();

        // Then
        assertTrue(result);
    }


    // 3.   Test showStudentEvents
    @Test
    @Tag("Show Events")
    @DisplayName("Test showStudentEvents - Valid File")
    @SpiraTestCase(testCaseId = 540)
    public void testShowStudentEvents_ValidFile() {
        // Create a mock "event.txt" file
        try {
            Path filePath = Paths.get("event.txt");
            Files.write(filePath, "Event1\nEvent2\n".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Initialize the Event object and call showStudentEvents
        Event event = new Event();
        event.showStudentEvents();

        // Prepare the expected output and assert
        String expectedOutput = "\nList of Events: \no Event1\no Event2";
        assertEquals(expectedOutput.trim(), outContent.toString().trim());

        // Clean up the mock "event.txt" file
        try {
            Files.delete(Paths.get("event.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //check Exception catching again
    @Test
    @Tag("Show Events")
    @DisplayName("Test showStudentEvents - File Not Found")
    @SpiraTestCase(testCaseId = 542)
    public void testShowStudentEvents_FileNotFound() {
        // Arrange
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Event event = new Event();

        // Act
        event.showStudentEvents();
        // Assert (Deliberate Failure) - intentionally make the code fail in order to get a failed case and raise a bug.
        String expectedOutput = "This is not the expected output";  // assuming the method silently catches the exception
        assertEquals(expectedOutput, outContent.toString());
    }



    // 4.   Test ViewStudentDetails
    @Test
    @Tag("View Details")
    @DisplayName("Test ViewStudentDetails - StudentList is not empty")
    @SpiraTestCase(testCaseId = 523)
    public void testViewStudentDetails_StudentListNotEmpty() {
        // Arrange
        Event event = new Event();
        event.Student.clear();  // Clear the Student list

        event.Student.add(new BasicData(701, "studentName701", "p7654324#"));
        event.Student.add(new BasicData(702, "studentName702", "p123456#"));

        // Act
        boolean result = event.viewStudentDetails();

        // Assert
        assertTrue(result);

        String expectedOutput = "\nDetails of All Student:.\n" +
                "ID: 701\n" +
                "Name: studentName701\n" +
                "Password: p7654324#\n" +
                "\n"+
                "ID: 702\n" +
                "Name: studentName702\n" +
                "Password: p123456#\n";

        assertEquals(expectedOutput.trim(), outContent.toString().trim());
    }

    @Test
    @Tag("View Details")
    @DisplayName("Test ViewStudentDetails - StudentList is empty")
    @SpiraTestCase(testCaseId = 541)
    public void testViewStudentDetails_StudentListEmpty() {
        // Arrange
        Event event = new Event();
        event.Student.clear();  // Clear the Student list

        // Act
        boolean result = event.viewStudentDetails();

        // Assert
        assertFalse(result);
        String expectedOutput = "\nDetails of All Student:.";  // As there are no students, only this should be printed.
        assertEquals(expectedOutput.trim(), outContent.toString().trim());
    }
    // 5.   Test searchStudentDetails
    @Test
    @Tag("Search")
    @DisplayName("Test SearchStudentDetails - Valid ID")
    @SpiraTestCase(testCaseId = 516)
    public void testSearchStudentDetails_ValidID() {
        Event event = new Event();
        event.Student.add(new BasicData(516, "studentName516", "p7654324#"));
        assertTrue(event.searchStudentDetails(516));
        //event.removeStudent(516);
    }

    @Test
    @Tag("Search")
    @DisplayName("Test SearchStudentDetails - Invalid ID")
    @SpiraTestCase(testCaseId = 517)
    public void testSearchStudentDetails_InvalidID() {
        Event event = new Event();
        event.Student.add(new BasicData(51, "studentName51", "p7654324#"));
        assertFalse(event.searchStudentDetails(999), "Should return false for invalid ID");
    }

    // 6.   Test removeStudent_valid
    @Test
    @Tag("Remove")
    @DisplayName("Test RemoveStudent - Valid Student")
    @SpiraTestCase(testCaseId = 513)
    public void testRemoveStudent_ValidStudent() {
        Event event = new Event();
        BasicData studenttest = new BasicData(20, "TestRemove1", "p1234567#");
        Event.Student.add(studenttest);

        assertTrue(event.removeStudent(20));
        assertFalse(Event.Student.contains(studenttest));
    }

    @Test
    @Tag("Remove")
    @DisplayName("Test RemoveStudent - Invalid Student")
    @SpiraTestCase(testCaseId = 514)
    public void testRemoveStudent_InvalidStudent() {
        Event event = new Event();
        assertFalse(event.removeStudent(9999));
    }

    // 7.   Test  countStudent Class
    @Test
    @Tag("Count")
    @DisplayName("Count Students")
    @SpiraTestCase(testCaseId = 515)
    public void testCountStudent() {
        Event event = new Event();
        Event.Student.clear();  // Clearing the list
        BasicData student333 = new BasicData(333, "Gummies", "p1234567#");
        BasicData student444 = new BasicData(444, "Eucalyptus", "p2345678#");
        Event.Student.add(student333);
        Event.Student.add(student444);
        assertEquals(2, event.countStudent());
    }

    // 8.  Test AddStudent
    @Test
    @Tag("Add")
    @DisplayName("AddStudent - Student does not exist")
    @SpiraTestCase(testCaseId = 518)
    public void testAddStudent_StudentDoesNotExist() {
        String input = "204\nAlice Bob\np1234567#\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        Event event = new Event();
        String result = event.AddStudent();
        assertEquals("Student Added Successfully", result);
        // Cleanup: remove the added student
        event.removeStudent(204);
    }

    @Test
    @Tag("Add")
    @DisplayName("AddStudent - Student exists")
    @SpiraTestCase(testCaseId = 519)
    public void testAddStudent_StudentExists() {
        String input1 = "1111\nVitamin D\np1234567#\n";
        InputStream in1 = new ByteArrayInputStream(input1.getBytes());
        System.setIn(in1);

        Event event = new Event();
        event.AddStudent();  // This will add the student

        String input2 = "1111\nVitamin D\np1234567#\n";
        InputStream in2 = new ByteArrayInputStream(input2.getBytes());
        System.setIn(in2);

        String result = event.AddStudent();
        assertEquals("Student Exists", result);
    }

    // 9.  Test ValidatePassword Class
    @Test
    @Tag("Validate Password")
    @DisplayName("ValidatePassword - Null Password")
    @SpiraTestCase(testCaseId = 478)
    public void testValidatePassword_NullPassword() {
        assertThrows(IllegalArgumentException.class, () -> event.ValidatePassword(null));
    }

    @Test
    @Tag("Validate Password")
    @DisplayName("ValidatePassword - Valid Password")
    @SpiraTestCase(testCaseId = 479)
    public void testValidatePassword_ValidPassword() throws PasswordValidationException {
        // Arrange & Act
        boolean result = true;
        Event.ValidatePassword("p1234567#");
        // Assert
        assertTrue(result);
    }


    @Test
    @Tag("Validate Password")
    @DisplayName("ValidatePassword - Invalid Passwords")
    @SpiraTestCase(testCaseId = 480)
    public void testValidatePassword_InvalidPasswords3Cases() {
        assertAll(
                () -> {
                    String shortPassword = "p564#";
                    Exception exception = assertThrows(PasswordValidationException.class, () -> Event.ValidatePassword(shortPassword));
                    assertTrue(exception.getMessage().contains("Password length should be 9"));
                },
                () -> {
                    String passwordWithInvalidStart = "a7654321#";
                    Exception exception = assertThrows(PasswordValidationException.class, () -> Event.ValidatePassword(passwordWithInvalidStart));
                    assertTrue(exception.getMessage().contains("First letter of the Password should be p"));
                },
                () -> {
                    String passwordWithInvalidEnd = "p12345678";
                    Exception exception = assertThrows(PasswordValidationException.class, () -> Event.ValidatePassword(passwordWithInvalidEnd));
                    assertTrue(exception.getMessage().contains("Last letter of the password should be #"));
                }
        );
    }
}