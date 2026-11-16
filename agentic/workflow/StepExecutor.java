package agentic.workflow;

public interface StepExecutor<T> {
    T execute(WorkflowStep step);
}