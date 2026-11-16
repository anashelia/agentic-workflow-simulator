AGENTIC WORKFLOW SIMULATOR
==========================

A Java-based simulation of an AI agent workflow. It loads workflow 
configurations from a text file, validates the structure, and 
simulates the execution of each step without requiring a real 
LLM API.

The project demonstrates advanced Object-Oriented design, focusing 
heavily on encapsulation, defensive copying, custom exceptions, 
interfaces, and generics.


OVERVIEW
--------

This system models how an AI agent processes a sequence of tasks. 
A workflow file describes the agent's name and its steps. Each step 
has a name, a prompt, a system prompt, and an expected output schema.

The program parses this file, builds Java objects, and provides a 
`run()` method that simulates executing the steps and generating 
placeholder outputs based on the requested schema type.


FEATURES
--------

  * Strict File Parsing: Loads agent definitions from `.txt` files 
    with a specific `AGENT:` / `STEP` / `ENDSTEP` format.
  * Validation: Rejects duplicate step names, blank prompts, and 
    malformed files using custom exceptions.
  * Encapsulation: Defensive copying and unmodifiable views are used 
    extensively to prevent internal data leaks.
  * Simulated Execution: Generates mock responses (e.g., "0" for INT, 
    "sample" for STRING) based on the step's schema type.
  * Extensibility: Uses interfaces and generics (e.g., `StepExecutor`, 
    `ValueFormatter`) to allow flexible formatting and execution logic.


PROJECT STRUCTURE
-----------------

  Source Code (src/agentic/workflow/):
    - Agent.java                     : Represents the whole workflow.
    - WorkflowStep.java              : Represents a single step.
    - StructuredOutput.java          : Defines expected output schema.
    - SchemaType.java                : Enum for schema types.
    - WorkflowFormatException.java   : Custom checked exception.
    - StepExecutor.java, etc.        : Interfaces/classes for extensions.

  Test Code:
    - AgenticWorkflowTestSuite.java  : The main test suite runner.
    - *StructureTest.java            : Structural tests provided by the university.
    - *Test.java                     : Custom functional tests.
    - agent_*.txt                    : Sample agent description files for testing.

  Grading / Build Files:
    - check.cmd                      : Script to compile and run the tests.
    - CheckThat.txt                  : Instructions for the test runner.


HOW TO COMPILE AND RUN
----------------------

Ensure you have Java 25+ installed.

Because Java 25+ requires specific module configurations that the provided 
script might handle incorrectly on Windows, it is recommended to run 
the tests manually:

1. Compile all source files:
   javac -cp ".;junit6all.jar;checkthat6.jar" -sourcepath . -d . agentic\workflow\*.java agentic\workflow\llm\*.java

2. Compile the test suite:
   javac -cp ".;junit6all.jar;checkthat6.jar" -sourcepath . -d . *.java

3. Run the test suite:
   java -cp ".;junit6all.jar;checkthat6.jar" org.junit.platform.console.ConsoleLauncher execute -c AgenticWorkflowTestSuite --disable-banner

Alternatively, you can try the provided script:
   ./check.cmd AgenticWorkflowTestSuite.java AgenticWorkflowTestSuite


LICENSE
-------

This program is provided as-is for educational and demonstration
purposes. Use and modify it freely.