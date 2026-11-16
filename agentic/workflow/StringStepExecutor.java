package agentic.workflow;

import agentic.workflow.llm.SchemaType;

public class StringStepExecutor implements StepExecutor<String> {
    
    private final boolean verbose;
    
    public StringStepExecutor() {
        this(false);
    }
    
    public StringStepExecutor(boolean verbose) {
        this.verbose = verbose;
    }
    
    @Override
    public String execute(WorkflowStep step) {
        SchemaType primary = step.getStructuredOutput().getPrimaryType();
        
        if (verbose) {
            return String.format("[%s] Executing step '%s' with output type %s -> %s",
                java.time.LocalTime.now(),
                step.getName(),
                primary,
                step.simulateResponse()
            );
        }
        
        return step.simulateResponse();
    }
}