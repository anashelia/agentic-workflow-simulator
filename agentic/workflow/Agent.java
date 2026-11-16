package agentic.workflow;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import agentic.workflow.llm.SchemaType;
import agentic.workflow.llm.StructuredOutput;
import agentic.workflow.llm.ExtendedType;

public class Agent {
    
    private String name;
    private final List<WorkflowStep> steps;
    private final Map<String, WorkflowStep> stepMap;
    private final Set<String> stepNames;
    
    public Agent(String name) {
        setName(name);
        this.steps = new ArrayList<>();
        this.stepMap = new HashMap<>();
        this.stepNames = new HashSet<>();
    }
    
    public String getName() { return name; }
    
    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("the name must not be `null`, empty, or blank.");
        }
        this.name = name;
    }
    
    public List<WorkflowStep> getSteps() {
        return Collections.unmodifiableList(steps);
    }
    
    public Map<String, WorkflowStep> getStepMap() {
        return Collections.unmodifiableMap(stepMap);
    }
    
    public ValidationReport validateAgent() {
        ValidationReport report = new ValidationReport();
        if (name == null || name.isBlank()) report.addError("Agent name is blank");
        if (steps.isEmpty()) report.addWarning(" no workflow steps");
        
        for (WorkflowStep step : steps) {
            if (step.getPrompt() == null || step.getPrompt().isBlank()) {
                report.addError("Step '" + step.getName() + "' has blank prompt");
            }
            if (step.getSystemPrompt() == null || step.getSystemPrompt().isBlank()) {
                report.addError("Step '" + step.getName() + "' has blank systemPrompt");
            }
            if (step.getStructuredOutput() == null) {
                report.addError("Step '" + step.getName() + "' has null structuredOutput");
            }
        }
        return report;
    }
    
    public void addStep(WorkflowStep step) {
        if (step == null) {
            throw new IllegalArgumentException("the step must not be `null`");
        }
        if (stepNames.contains(step.getName())) {
            throw new IllegalArgumentException("the step must not be `null`");
        }
        steps.add(step);
        stepMap.put(step.getName(), step);
        stepNames.add(step.getName());
    }
    
    public int getStepCount() { return steps.size(); }
    
    public WorkflowStep findStepByName(String stepName) {
        if (stepName == null || stepName.isBlank()) {
            throw new IllegalArgumentException("the step name must not be `null`, empty, or blank.");
        }
        return stepMap.get(stepName.trim());
    }
    
    public void run() {
        System.out.println("Running agent: " + name);
        for (WorkflowStep step : steps) {
            System.out.println("  Step: " + step.getName());
            System.out.println("    Response: " + step.simulateResponse());
        }
    }
    
    public static Agent loadAgent(String filename) throws IOException, WorkflowFormatException {
    if (filename == null || filename.isBlank()) {
        throw new IllegalArgumentException("the filename must not be `null`, empty, or blank.");
    }
    
    try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
        String firstLine = reader.readLine();
        while (firstLine != null && firstLine.isBlank()) firstLine = reader.readLine();
        
        if (firstLine == null || !firstLine.startsWith("AGENT:")) {
            throw new WorkflowFormatException("Missing or invalid AGENT header.");
        }
        
        String agentName = firstLine.substring(6).trim();
        if (agentName.isEmpty()) throw new WorkflowFormatException("Agent name cannot be empty");
        
        Agent agent = new Agent(agentName);
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.isBlank()) continue;
            if (line.trim().equals("STEP")) {
                WorkflowStep step = parseStep(reader);
                try {
                    agent.addStep(step);
                } catch (IllegalArgumentException e) {
                    throw new WorkflowFormatException(e.getMessage(), e);
                }
            }
        }
        return agent;
    }
}
    
    private static WorkflowStep parseStep(BufferedReader reader) throws IOException, WorkflowFormatException {
        String name = null, prompt = null, systemPrompt = null, output = null;
        Integer temperature = null;
        String dependsOn = null;
        Integer retries = null;
        Set<String> tags = new HashSet<>();
        
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.equals("ENDSTEP")) break;
            if (line.isEmpty()) continue;
            
            int eq = line.indexOf('=');
            if (eq <= 0) throw new WorkflowFormatException("Invalid property: " + line);
            
            String prop = line.substring(0, eq).trim();
            String val = line.substring(eq + 1).trim();
            
            switch (prop) {
                case "name": name = val; break;
                case "prompt": prompt = val; break;
                case "systemPrompt": systemPrompt = val; break;
                case "output": output = val; break;
                case "temperature": temperature = Integer.parseInt(val); break;
                case "dependsOn": dependsOn = val; break;
                case "retries": retries = Integer.parseInt(val); break;
                case "tags": for (String t : val.split(",")) tags.add(t.trim()); break;
                default: throw new WorkflowFormatException("Unknown property: " + prop);
            }
        }
        
        if (name == null || prompt == null || systemPrompt == null || output == null) {
            throw new WorkflowFormatException("Missing required properties");
        }
        
        SchemaType schemaType = SchemaType.valueOf(output);
        StructuredOutput structuredOutput = new StructuredOutput(schemaType);
        WorkflowStep step = new WorkflowStep(name, prompt, systemPrompt, structuredOutput, temperature, dependsOn, retries, tags);
        
        
        try {
            ExtendedType extType = ExtendedType.valueOf(output);
            step.addExtendedType(extType);
        } catch (IllegalArgumentException e) {
            
        }
        
        return step;
    }
    
    @Override
    public String toString() {
        return "Agent{name='" + name + "', steps=" + steps.size() + "}";
    }
}