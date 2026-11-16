package agentic.workflow.llm;

import java.util.*;

public class StructuredOutput {

    private final SchemaType[] schemaTypes;
    private final boolean allowsOptional;
    private final boolean allowsNested;


    public StructuredOutput(SchemaType[] schemaTypes, boolean allowsOptional, boolean allowsNested, Set<ExtendedType> extendedTypes) {
        this.schemaTypes = Arrays.copyOf(schemaTypes, schemaTypes.length);
        this.allowsOptional = allowsOptional;
        this.allowsNested = allowsNested;
        if (extendedTypes != null) {
            for (ExtendedType et : extendedTypes) {
                addExtendedType(et);
            }
        }
    }

        public StructuredOutput(SchemaType... schemaTypes) {
        if (schemaTypes == null || schemaTypes.length == 0) {
            throw new IllegalArgumentException("at least one schema type must be provided.");
        }

        this.schemaTypes = Arrays.copyOf(schemaTypes, schemaTypes.length);
        
        boolean hasOptional = false;
        boolean hasNested = false;
        
        for (SchemaType schemaType : schemaTypes) {
            Objects.requireNonNull(schemaType, "schema types themselves must not contain null.");
        }
        
        this.allowsOptional = hasOptional;
        this.allowsNested = hasNested;
    }

    public SchemaType[] getSchemaTypes() {
        return Arrays.copyOf(schemaTypes, schemaTypes.length);
    }

    public boolean contains(SchemaType schemaType) {
        for (SchemaType type : schemaTypes) {
            if (type == schemaType) {
                return true;
            }
        }
        return false;
    }

    public int size() {
        return schemaTypes.length;
    }
    
    public boolean allowsOptional() {
        return allowsOptional;
    }
    
    public boolean allowsNested() {
        return allowsNested;
    }
    
    public SchemaType getPrimaryType() {
        return schemaTypes[0];
    }
    
    private Set<ExtendedType> extendedTypes = new HashSet<>();
    
    public void addExtendedType(ExtendedType type) {
        extendedTypes.add(type);
    }
    
    public boolean hasExtendedType(ExtendedType type) {
        return extendedTypes.contains(type);
    }
    
    public Set<ExtendedType> getExtendedTypes() {
        return new HashSet<>(extendedTypes);
    }

    @Override
    public String toString() {
        return "StructuredOutput{types=" + Arrays.toString(schemaTypes) + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        StructuredOutput that = (StructuredOutput) obj;
        return Arrays.equals(schemaTypes, that.schemaTypes);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(schemaTypes);
    }
}