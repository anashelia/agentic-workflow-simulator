package agentic.workflow;

import agentic.workflow.llm.SchemaType;
import agentic.workflow.llm.StructuredOutput;
import agentic.workflow.llm.ExtendedType;
import java.util.Objects;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays; 

public class WorkflowStep {

    private String name;
    private String prompt;
    private String systemPrompt;
    private StructuredOutput structuredOutput;
    private Integer temperature;
    private String dependsOn;
    private Integer retries;
    private Set<String> tags;
    private Set<ExtendedType> extendedTypes = new HashSet<>();

    public WorkflowStep(
            String name,
            String prompt,
            String systemPrompt,
            StructuredOutput structuredOutput
    ) {
        this(name, prompt, systemPrompt, structuredOutput, null, null, null, new HashSet<>());
    }

    public WorkflowStep(
            String name,
            String prompt,
            String systemPrompt,
            StructuredOutput structuredOutput,
            Integer temperature,
            String dependsOn,
            Integer retries,
            Set<String> tags
    ) {
        if (name == null || name.isBlank()
                || prompt == null || prompt.isBlank()
                || systemPrompt == null || systemPrompt.isBlank()
                || structuredOutput == null) {

            throw new IllegalArgumentException(
                "`name`, `prompt`, and `systemPrompt` must not be blank, and `structuredOutput` must not be `null`."
            );
        }

        this.name = name;
        this.prompt = prompt;
        this.systemPrompt = systemPrompt;
        this.structuredOutput = structuredOutput;
        this.temperature = temperature;
        this.dependsOn = dependsOn;
        this.retries = retries;
        this.tags = tags != null ? new HashSet<>(tags) : new HashSet<>();
    }

    
public WorkflowStep(String name, String prompt, String systemPrompt, 
                    StructuredOutput structuredOutput, Integer temperature, 
                    String dependsOn, Integer retries, Set<String> tags, 
                    Set<ExtendedType> extendedTypes) {
    this(name, prompt, systemPrompt, structuredOutput, temperature, dependsOn, retries, tags);
    if (extendedTypes != null) {
        for (ExtendedType et : extendedTypes) {
            addExtendedType(et);
        }
    }
}
    
    public void addExtendedType(ExtendedType type) {
        extendedTypes.add(type);
        structuredOutput.addExtendedType(type);
    }
    
    public boolean hasExtendedType(ExtendedType type) {
        return extendedTypes.contains(type);
    }
    
    public Set<ExtendedType> getExtendedTypes() {
        return new HashSet<>(extendedTypes);
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPrompt() { return prompt; }
    public void setPrompt(String prompt) { this.prompt = prompt; }
    public String getSystemPrompt() { return systemPrompt; }
    public void setSystemPrompt(String systemPrompt) { this.systemPrompt = systemPrompt; }
    public StructuredOutput getStructuredOutput() { return structuredOutput; }
    public void setStructuredOutput(StructuredOutput structuredOutput) { this.structuredOutput = structuredOutput; }
    public Integer getTemperature() { return temperature; }
    public void setTemperature(Integer temperature) { this.temperature = temperature; }
    public String getDependsOn() { return dependsOn; }
    public void setDependsOn(String dependsOn) { this.dependsOn = dependsOn; }
    public Integer getRetries() { return retries; }
    public void setRetries(Integer retries) { this.retries = retries; }
    public Set<String> getTags() { return new HashSet<>(tags); }
    public void setTags(Set<String> tags) { this.tags = new HashSet<>(tags); }

    public boolean expectsStructuredOutput() {
        return structuredOutput.size() > 0;
    }

    public String simulateResponse() {
        if (!extendedTypes.isEmpty()) {
            ExtendedType primary = extendedTypes.iterator().next();
            return simulateExtendedResponse(primary);
        }
        
        SchemaType primary = structuredOutput.getPrimaryType();
        return switch (primary) {
            case INT -> "0";
            case STRING -> "sample";
            case BOOLEAN -> "true";
            case LIST_INT -> "[1,2,3]";
            case LIST_STRING -> "[\"a\",\"b\"]";
            case MAP_STRING_STRING -> "{\"key\":\"value\"}";
        };
    }
    
    private String simulateExtendedResponse(ExtendedType type) {
        return switch (type) {
            case LIST_BOOLEAN -> "[true, false, true]";
            case MAP_STRING_INT -> "{a:1, b:2}";
            case NESTED_LIST_INT -> "[[1,2],[3,4]]";
            case NESTED_LIST_STRING -> "[[\"a\",\"b\"],[\"c\",\"d\"]]";
            case OPTIONAL_INT -> "Optional[42]";
            case OPTIONAL_STRING -> "Optional[sample]";
            case OPTIONAL_BOOLEAN -> "Optional[true]";
        };
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("WorkflowStep{name='").append(name).append('\'');
        sb.append(", prompt='").append(prompt).append('\'');
        sb.append(", outputTypes=").append(Arrays.toString(structuredOutput.getSchemaTypes()));
        if (!extendedTypes.isEmpty()) sb.append(", extendedTypes=").append(extendedTypes);
        if (temperature != null) sb.append(", temperature=").append(temperature);
        if (dependsOn != null) sb.append(", dependsOn='").append(dependsOn).append('\'');
        if (retries != null) sb.append(", retries=").append(retries);
        if (!tags.isEmpty()) sb.append(", tags=").append(tags);
        sb.append('}');
        return sb.toString();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        WorkflowStep that = (WorkflowStep) obj;
        return Objects.equals(name, that.name) &&
               Objects.equals(prompt, that.prompt) &&
               Objects.equals(systemPrompt, that.systemPrompt) &&
               Arrays.equals(structuredOutput.getSchemaTypes(), that.structuredOutput.getSchemaTypes());
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(name, prompt, systemPrompt, Arrays.hashCode(structuredOutput.getSchemaTypes()));
    }
}