package agentic.workflow.llm;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StructuredOutputTest {
    
    @Test
    public void testContainsExistingType() {
        StructuredOutput so = new StructuredOutput(SchemaType.INT, SchemaType.STRING);
        assertTrue(so.contains(SchemaType.INT));
        assertTrue(so.contains(SchemaType.STRING));
    }
    
    @Test
    public void testContainsMissingType() {
        StructuredOutput so = new StructuredOutput(SchemaType.INT, SchemaType.STRING);
        assertFalse(so.contains(SchemaType.BOOLEAN));
    }
    
    @Test
    public void testSize() {
        assertEquals(2, new StructuredOutput(SchemaType.INT, SchemaType.STRING).size());
        assertEquals(1, new StructuredOutput(SchemaType.INT).size());
    }
    
}