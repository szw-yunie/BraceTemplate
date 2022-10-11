// If you want to use this class, change the package name to yours.
package com.szw.brace.template;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class provides support for the curly bracket template syntax.
 * <p>
 * correct eg:
 * <ul>
 *     <li>"hello {k1},{k2}"</li>
 *     <li>"hello {k1}{k2}"</li>
 * </ul>
 * <p>
 * incorrect eg:
 * <ul>
 *     <li>"hello {k1{k2}"</li>
 *     <li>"hello {k1{k2}}"</li>
 * </ul>
 *
 * @author szw
 */
public class BraceTemplate {
    private List<Part> partList = new ArrayList<>();
    private StringBuilder builder;
    private int buildType = 0;

    public BraceTemplate(String template) {
        int state = 0;
        char ch = '\1';
        for (int i = 0; ch != '\0'; i++) {
            ch = i < template.length() ? template.charAt(i) : '\0';
            switch (state) {
                case 0:
                    switch (ch) {
                        case '{':
                            state = 1;
                            endBuildPart();
                            break;
                        case '}':
                            error(template);
                            break;
                        case '\0':
                            state = 3;
                            endBuildPart();
                            break;
                        default:
                            state = 0;
                            buildStringPart(ch);
                            break;
                    }
                    break;
                case 1:
                    switch (ch) {
                        case '{':
                        case '\0':
                        case '}':
                            error(template);
                            break;
                        default:
                            state = 2;
                            buildVarPart(ch);
                            break;
                    }
                    break;
                case 2:
                    switch (ch) {
                        case '{':
                        case '\0':
                            error(template);
                            break;
                        case '}':
                            state = 0;
                            endBuildPart();
                            break;
                        default:
                            state = 2;
                            buildVarPart(ch);
                            break;
                    }
                    break;
                case 3:
                    // ch = '\0', end loop.
                    endBuildPart();
                    break;
            }
        }
    }

    private void error(String template) {
        throw new IllegalArgumentException("The template \"" + template + "\" has grammatical errors.");
    }

    private void buildStringPart(char ch) {
        buildPart(0, ch);
    }

    private void buildVarPart(char ch) {
        buildPart(1, ch);
    }

    private void buildPart(int type, char ch) {
        if (builder == null) {
            builder = new StringBuilder();
        }
        buildType = type;
        builder.append(ch);
    }

    private void endBuildPart() {
        if (builder == null) {
            return;
        }
        String partVal = builder.toString();
        if (buildType == 0) {
            partList.add(dataMap -> partVal);
        } else if (buildType == 1) {
            partList.add(dataMap -> toString(dataMap.get(partVal)));
        }
        builder = null;
    }

    private String toString(Object object) {
        // TODO: Transform logical extension points.
        return object.toString();
    }

    /**
     * Replace the curly braces with dataMap, use the content in the curly braces as key.
     *
     * @param dataMap data to merge into template.
     * @return result string.
     */
    public String merge(Map<String, Object> dataMap) {
        StringBuilder builder = new StringBuilder();
        for (Part part : partList) {
            builder.append(part.lookFor(dataMap));
        }
        return builder.toString();
    }

    @FunctionalInterface
    private interface Part {
        String lookFor(Map<String, Object> dataMap);
    }

    public static void main(String[] args) {

        String template = "hello {k1},{k2}";

        Map<String, Object> map = new HashMap<>();
        map.put("k1", "szw1");
        map.put("k2", "szw2");

        System.out.println(new BraceTemplate(template).merge(map));
    }
}
