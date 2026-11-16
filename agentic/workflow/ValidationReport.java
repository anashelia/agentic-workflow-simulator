package agentic.workflow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ValidationReport {
    private final List<String> errors = new ArrayList<>();
    private final List<String> warnings = new ArrayList<>();
    private final Map<String, List<String>> stepErrors = new HashMap<>();
    
    public void addError(String error) {
        errors.add(error);
    }
    
    public void addErrorForStep(String stepName, String error) {
        stepErrors.computeIfAbsent(stepName, k -> new ArrayList<>()).add(error);
        addError("Step '" + stepName + "': " + error);
    }
    
    public void addWarning(String warning) {
        warnings.add(warning);
    }
    
    public void addWarningForStep(String stepName, String warning) {
        stepErrors.computeIfAbsent(stepName, k -> new ArrayList<>()).add(warning);
        addWarning("Step '" + stepName + "': " + warning);
    }
    
    public boolean hasErrors() {
        return !errors.isEmpty();
    }
    
    public boolean hasWarnings() {
        return !warnings.isEmpty();
    }
    
    public List<String> getErrors() {
        return new ArrayList<>(errors);
    }
    
    public List<String> getWarnings() {
        return new ArrayList<>(warnings);
    }
    
    public Map<String, List<String>> getStepErrors() {
        return new HashMap<>(stepErrors);
    }
    
    public int getErrorCount() {
        return errors.size();
    }
    
    public int getWarningCount() {
        return warnings.size();
    }
    
    public String getSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("Validation Report\n");
        sb.append("Errors: ").append(errors.size()).append("\n");
        sb.append("Warnings: ").append(warnings.size()).append("\n\n");
        
        if (!errors.isEmpty()) {
            sb.append("ERRORS:\n");
            for (String error : errors) {
                sb.append("  x ").append(error).append("\n");
            }
            sb.append("\n");
        }
        
        if (!warnings.isEmpty()) {
            sb.append("WARNINGS:\n");
            for (String warning : warnings) {
                sb.append("  x ").append(warning).append("\n");
            }
            sb.append("\n");
        }
        
        if (errors.isEmpty() && warnings.isEmpty()) {
            sb.append("All validations passed!\n");
        } else if (errors.isEmpty()) {
            sb.append(" Valid with warnings\n");
        } else {
            sb.append(" Invalid - fix errors before running\n");
        }
        
        return sb.toString();
    }
    
    @Override
    public String toString() {
        return getSummary();
    }
}