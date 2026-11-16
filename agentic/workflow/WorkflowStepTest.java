package agentic.workflow;

import agentic.workflow.llm.SchemaType;
import agentic.workflow.llm.StructuredOutput;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class WorkflowStepTest {
    
    private StructuredOutput output = new StructuredOutput(SchemaType.STRING);
    
    @Test
    public void testExpectsStructuredOutput() {
        WorkflowStep step = new WorkflowStep("name", "prompt", "system", output);
        assertTrue(step.expectsStructuredOutput());
    }
    
    @Test
    public void testSimulateResponseByPrimaryType() {
        assertEquals("0", new WorkflowStep("n", "p", "s", new StructuredOutput(SchemaType.INT)).simulateResponse());
        assertEquals("sample", new WorkflowStep("n", "p", "s", new StructuredOutput(SchemaType.STRING)).simulateResponse());
        assertEquals("true", new WorkflowStep("n", "p", "s", new StructuredOutput(SchemaType.BOOLEAN)).simulateResponse());
        assertEquals("[1,2,3]", new WorkflowStep("n", "p", "s", new StructuredOutput(SchemaType.LIST_INT)).simulateResponse());
        assertEquals("[\"a\",\"b\"]", new WorkflowStep("n", "p", "s", new StructuredOutput(SchemaType.LIST_STRING)).simulateResponse());
        assertEquals("{\"key\":\"value\"}", new WorkflowStep("n", "p", "s", new StructuredOutput(SchemaType.MAP_STRING_STRING)).simulateResponse());
    }
    
}