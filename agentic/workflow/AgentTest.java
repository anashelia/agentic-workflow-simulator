package agentic.workflow;

import agentic.workflow.llm.SchemaType;
import agentic.workflow.llm.StructuredOutput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.*;

public class AgentTest {

    private Agent agent;
    private StructuredOutput output;

    @BeforeEach
    void setup() {
        agent = new Agent("TestAgent");
        output = new StructuredOutput(SchemaType.STRING);
    }

    private WorkflowStep step(String name) {
        return new WorkflowStep(name, "prompt", "system", output);
    }

    @Test
    void testStepCount() {
        assertEquals(0, agent.getStepCount());

        agent.addStep(step("s1"));
        agent.addStep(step("s2"));

        assertEquals(2, agent.getStepCount());
    }

    @Test
    void testAddDuplicateStepRejected() {
        agent.addStep(step("dup"));

        assertThrows(IllegalArgumentException.class,
                () -> agent.addStep(step("dup")));

        assertEquals(1, agent.getStepCount());
    }

    @Test
    void findStepByName() {
        WorkflowStep s = step("findMe");
        agent.addStep(s);

        assertEquals(s, agent.findStepByName("findMe"));
    }

    @Test
    void findStepByNameMissing() {
        agent.addStep(step("exists"));
        assertNull(agent.findStepByName("missing"));
    }

    @Test
    void testLoadAgentSuccess() throws Exception {
        String file = "agent_ok.txt";

        try (PrintWriter w = new PrintWriter(new FileWriter(file))) {
            w.println("AGENT: Loaded");
            w.println("STEP");
            w.println("name=s1");
            w.println("prompt=p");
            w.println("systemPrompt=s");
            w.println("output=STRING");
            w.println("ENDSTEP");
        }

        Agent loaded = Agent.loadAgent(file);

        assertEquals("Loaded", loaded.getName());
        assertEquals(1, loaded.getStepCount());
    }

    @Test
    void testLoadAgentRejectsMissingHeader() throws IOException {
        String file = "agent_bad_header.txt";

        try (PrintWriter w = new PrintWriter(new FileWriter(file))) {
            w.println("STEP");
        }

        assertThrows(WorkflowFormatException.class,
                () -> Agent.loadAgent(file));
    }

    @Test
    void testLoadAgentRejectsDuplicateStepNames() throws IOException {
        String file = "agent_duplicate.txt";

        try (PrintWriter w = new PrintWriter(new FileWriter(file))) {
            w.println("AGENT: Test");

            w.println("STEP");
            w.println("name=dup");
            w.println("prompt=p");
            w.println("systemPrompt=s");
            w.println("output=STRING");
            w.println("ENDSTEP");

            w.println("STEP");
            w.println("name=dup");
            w.println("prompt=p");
            w.println("systemPrompt=s");
            w.println("output=STRING");
            w.println("ENDSTEP");
        }

        assertThrows(WorkflowFormatException.class,
                () -> Agent.loadAgent(file));
    }
}