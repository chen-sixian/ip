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
}
