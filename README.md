# BraceTemplate
This project provides support for the curly bracket template syntax.
# Usage
````java
 public static void main(String[] args) {

        String template = "hello {k1},{k2}";

        Map<String, Object> map = new HashMap<>();
        map.put("k1", "szw1");
        map.put("k2", "szw2");

        System.out.println(new BraceTemplate(template).merge(map));
    }
````
