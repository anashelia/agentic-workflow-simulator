package agentic.workflow;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class WorkflowFormatExceptionTest {
    
    @Test
    public void testConstructorWithMessage() {
        WorkflowFormatException e = new WorkflowFormatException("error");
        assertEquals("error", e.getMessage());
    }
    
    @Test
    public void testConstructorWithMessageAndCause() {
        Throwable cause = new RuntimeException();
        WorkflowFormatException e = new WorkflowFormatException("error", cause);
        assertEquals("error", e.getMessage());
        assertEquals(cause, e.getCause());
    }
}