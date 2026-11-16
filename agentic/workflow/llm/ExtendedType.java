/*for part3a at first, i wrote the extension in SchemaType, like i just added the new enum options there. but that made the tests fail
and i dont think we are allowed to change the testSuite and provided tests, so, im making this new file here now.*/

package agentic.workflow.llm;

public enum ExtendedType {
    LIST_BOOLEAN,
    MAP_STRING_INT,
    NESTED_LIST_INT,
    NESTED_LIST_STRING,
    OPTIONAL_INT,
    OPTIONAL_STRING,
    OPTIONAL_BOOLEAN
}