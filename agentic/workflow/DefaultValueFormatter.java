package agentic.workflow;

import java.util.List;
import java.util.Map;

public class DefaultValueFormatter implements ValueFormatter<Object> {
    
    private final boolean prettyPrint;
    
    public DefaultValueFormatter() {
        this(false);
    }
    
    public DefaultValueFormatter(boolean prettyPrint) {
        this.prettyPrint = prettyPrint;
    }
    
    @Override
    public String format(Object value) {
        if (value == null) {
            return "null";
        }
        if (value instanceof String) {
            return "\"" + value + "\"";
        }
        if (value instanceof List) {
            return formatList((List<?>) value);
        }
        if (value instanceof Map) {
            return formatMap((Map<?, ?>) value);
        }
        return value.toString();
    }
    
    private String formatList(List<?> list) {
        StringBuilder sb = new StringBuilder();
        if (prettyPrint) sb.append("\n  ");
        sb.append("[");
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) sb.append(", ");
            if (prettyPrint && i > 0) sb.append(" ");
            sb.append(format(list.get(i)));
        }
        sb.append("]");
        return sb.toString();
    }
    
    private String formatMap(Map<?, ?> map) {
        StringBuilder sb = new StringBuilder();
        if (prettyPrint) sb.append("\n  ");
        sb.append("{");
        int i = 0;
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (i > 0) sb.append(", ");
            if (prettyPrint && i > 0) sb.append("\n   ");
            sb.append(format(entry.getKey())).append(": ").append(format(entry.getValue()));
            i++;
        }
        sb.append("}");
        return sb.toString();
    }
}