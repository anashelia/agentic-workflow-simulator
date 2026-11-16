package agentic.workflow;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class WorkflowFileWriter {

    private static PrintWriter open(String filename) throws IOException {
        return new PrintWriter(new FileWriter(filename));
    }

    private static void writeStep(PrintWriter writer,
                                    String name,
                                   String prompt,
                                   String systemPrompt,
                                   String outputType) {
        writer.println("STEP");
        writer.println("name=" + name);
        writer.println("prompt=" + prompt);
        writer.println("systemPrompt=" + systemPrompt);
        writer.println("output=" + outputType);
        writer.println("ENDSTEP");
    }
    
    private static void writeStepWithAdvanced(PrintWriter writer,
                                   String name,
                                   String prompt,
                                   String systemPrompt,
                                   String outputType,
                                   Integer temperature,
                                   String dependsOn,
                                   Integer retries,
                                   String tags) {
        writer.println("STEP");
        writer.println("name=" + name);
        writer.println("prompt=" + prompt);
        writer.println("systemPrompt=" + systemPrompt);
        writer.println("output=" + outputType);
        if (temperature != null) writer.println("temperature=" + temperature);
        if (dependsOn != null) writer.println("dependsOn=" + dependsOn);
        if (retries != null) writer.println("retries=" + retries);
        if (tags != null) writer.println("tags=" + tags);
        writer.println("ENDSTEP");
    }

    public static void createCalculatorWorkflow(String filename) throws IOException {
        try (PrintWriter writer = open(filename)) {
            writer.println("AGENT: Calculator Helper");
            writeStep(writer, "collect_numbers", "Ask the user for a list of integers",
                "You are a precise calculator assistant", "LIST_INT");
            writeStep(writer, "sum_numbers", "Return the sum of the integers",
                "Return only the numeric answer", "INT");
        }
    }

    public static void createTravelPlannerWorkflow(String filename) throws IOException {
        try (PrintWriter writer = open(filename)) {
            writer.println("AGENT: Travel Planner");
            writeStep(writer, "get_destination", "Ask the user where they want to travel",
                "You are a helpful travel assistant", "STRING");
            writeStep(writer, "get_dates", "Ask for travel dates",
                "Be precise about date format", "STRING");
            writeStep(writer, "get_budget", "Ask for the travel budget",
                "Be helpful and respectful", "INT");
            writeStep(writer, "get_preferences", "Ask about accommodation preferences",
                "List common options", "LIST_STRING");
        }
    }

    public static void createStudyCoachWorkflow(String filename) throws IOException {
        try (PrintWriter writer = open(filename)) {
            writer.println("AGENT: Study Coach");
            writeStep(writer, "ask_topic", "Ask the student what topic they want to revise",
                "Be supportive and concise", "STRING");
            writeStep(writer, "create_quiz", "Generate three review questions",
                "Output a small list", "LIST_STRING");
            writeStep(writer, "check_readiness", "Decide if the student seems ready for a quiz",
                "Return true or false only", "BOOLEAN");
        }
    }

    public static void createAdvancedWorkflow(String filename) throws IOException {
        try (PrintWriter writer = open(filename)) {
            writer.println("AGENT: Advanced Assistant");
            
            writeStepWithAdvanced(writer, "get_quiz_answers", "Ask for true/false answers",
                "Collect boolean responses", "LIST_BOOLEAN", null, null, null, null);
            
            writeStepWithAdvanced(writer, "get_prices", "Ask for item prices",
                "Return a map of item names to prices", "MAP_STRING_INT", null, null, null, null);
            
            writeStepWithAdvanced(writer, "matrix_operation", "Perform matrix addition",
                "Return nested list of integers", "NESTED_LIST_INT", 0, null, 3, "math");
            
            writeStepWithAdvanced(writer, "generate_grid", "Create a 2D grid",
                "Return nested list of strings", "NESTED_LIST_STRING", 1, null, 2, "grid");
            
            writeStepWithAdvanced(writer, "optional_value", "Get optional configuration",
                "Return optional integer", "OPTIONAL_INT", null, "matrix_operation", 1, "config");
            
            writeStepWithAdvanced(writer, "optional_boolean", "Check optional setting",
                "Return optional boolean", "OPTIONAL_BOOLEAN", null, "optional_value", null, "config");
        }
    }

    public static void createShoppingListWorkflow(String filename) throws IOException {
        try (PrintWriter writer = open(filename)) {
            writer.println("AGENT: Shopping List Assistant");
            writeStepWithAdvanced(writer, "get_items", "Ask what items they need",
                "List items clearly", "LIST_STRING", null, null, null, "shopping");
            writeStep(writer, "get_budget", "Ask for total budget",
                "Be respectful", "INT");
            writeStep(writer, "get_store", "Ask which store",
                "Be helpful", "STRING");
            writeStepWithAdvanced(writer, "check_availability", "Check if items available",
                "Return true/false", "BOOLEAN", null, "get_store", 2, "validation");
            writeStepWithAdvanced(writer, "estimate_cost", "Estimate total cost",
                "Return numeric total", "INT", null, "get_items", null, "calculation");
        }
    }

    public static void createRecipeAssistantWorkflow(String filename) throws IOException {
        try (PrintWriter writer = open(filename)) {
            writer.println("AGENT: Recipe Assistant");
            writeStep(writer, "get_dish_name", "Ask what dish to cook",
                "Helpful cooking assistant", "STRING");
            writeStep(writer, "get_ingredients", "Ask what ingredients they have",
                "List ingredients clearly", "LIST_STRING");
            writeStep(writer, "get_servings", "Ask how many servings",
                "Be precise", "INT");
            writeStepWithAdvanced(writer, "check_feasible", "Check if enough ingredients",
                "Return true/false", "BOOLEAN", null, "get_ingredients", null, "validation");
            writeStepWithAdvanced(writer, "provide_recipe", "Provide recipe with steps",
                "Return map of steps", "MAP_STRING_STRING", 0, null, null, "output");
            writeStepWithAdvanced(writer, "suggest_substitutions", "Suggest substitutions",
                "Provide alternatives", "LIST_STRING", 1, "get_ingredients", 2, "suggestions");
        }
    }
    
    public static void generateAllExamples() throws IOException {
        createCalculatorWorkflow("calculator.agent");
        createTravelPlannerWorkflow("travel_planner.agent");
        createStudyCoachWorkflow("study_coach.agent");
        createShoppingListWorkflow("shopping_list.agent");
        createRecipeAssistantWorkflow("recipe_assistant.agent");
        createAdvancedWorkflow("advanced_workflow.agent");
        System.out.println("All example workflow files generated!");
    }
}