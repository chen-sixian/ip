package wwaffle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class WWaffleTest {
    @TempDir
    private Path temporaryDirectory;

    @Test
    void getResponse_taskWorkflow_returnsExpectedMessages() {
        WWaffle wwaffle = new WWaffle(temporaryDirectory.resolve("tasks.txt").toString());

        assertEquals("[+] TASK ADDED\n[T][ ] read book\n1 task total.",
                wwaffle.getResponse("todo read book"));
        assertEquals("[ TASKS ]\n1. [T][ ] read book", wwaffle.getResponse("list"));
        assertEquals("[+] TASK COMPLETED\n[T][X] read book", wwaffle.getResponse("mark 1"));
        assertEquals(1, wwaffle.getTaskCount());
    }

    @Test
    void getResponse_invalidAndExitCommands_returnsHelpfulMessages() {
        WWaffle wwaffle = new WWaffle(temporaryDirectory.resolve("tasks.txt").toString());

        assertEquals("[!] Unknown command.", wwaffle.getResponse("dance"));
        assertEquals("System offline. Goodbye!", wwaffle.getResponse("bye"));
        assertTrue(wwaffle.getWelcomeMessage().contains("0 tasks loaded"));
    }
    @Test
    void getResponse_sortName_preservesTiesAndPersistsNewIndices() {
        String filePath = temporaryDirectory.resolve("sorted.txt").toString();
        WWaffle app = new WWaffle(filePath);
        app.getResponse("todo zebra");
        app.getResponse("todo apple");
        app.getResponse("todo Apple");
        app.getResponse("mark 3");

        String expected = "[ TASKS ]\n1. [T][ ] apple\n2. [T][X] Apple\n3. [T][ ] zebra";
        assertEquals(expected, app.getResponse("sort name"));
        WWaffle reopened = new WWaffle(filePath);
        assertEquals(expected, reopened.getResponse("list"));
        assertEquals("[+] TASK COMPLETED\n[T][X] apple", reopened.getResponse("mark 1"));
        reopened.getResponse("todo aardvark");
        assertTrue(reopened.getResponse("list").endsWith("4. [T][ ] aardvark"));
    }

    @Test
    void getResponse_sortStatus_preservesOrderWithinGroupsAndPersists() {
        String filePath = temporaryDirectory.resolve("status.txt").toString();
        WWaffle app = new WWaffle(filePath);
        app.getResponse("todo first");
        app.getResponse("event second /from morning /to evening");
        app.getResponse("todo third");
        app.getResponse("todo fourth");
        app.getResponse("mark 1");
        app.getResponse("mark 3");

        app.getResponse("sort status");
        String sorted = app.getResponse("list");
        assertTrue(sorted.indexOf("second") < sorted.indexOf("fourth"));
        assertTrue(sorted.indexOf("fourth") < sorted.indexOf("first"));
        assertTrue(sorted.indexOf("first") < sorted.indexOf("third"));
        assertEquals(4, app.getTaskCount());
        assertEquals(sorted, new WWaffle(filePath).getResponse("list"));
    }

    @Test
    void getResponse_invalidSort_keepsMemoryAndStorageUnchanged() {
        String filePath = temporaryDirectory.resolve("invalid.txt").toString();
        WWaffle app = new WWaffle(filePath);
        app.getResponse("todo zebra");
        app.getResponse("todo apple");
        String original = app.getResponse("list");

        for (String command : new String[]{"sort", "sort date", "sort name extra", "sort NAME"}) {
            assertEquals("[!] Use: sort name or sort status.", app.getResponse(command));
            assertEquals(original, app.getResponse("list"));
            assertEquals(original, new WWaffle(filePath).getResponse("list"));
        }
    }

    @Test
    void getResponse_sortEmptyOrSingleTask_returnsUnchangedList() {
        WWaffle app = new WWaffle(temporaryDirectory.resolve("small.txt").toString());
        assertEquals("[i] Your task list is empty.", app.getResponse("sort name"));
        assertEquals("[i] Your task list is empty.", app.getResponse("sort status"));
        app.getResponse("deadline submit /by 2026-12-02");
        String original = app.getResponse("list");
        assertEquals(original, app.getResponse("sort name"));
        assertEquals(original, app.getResponse("sort status"));
    }
}
