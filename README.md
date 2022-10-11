# BraceTemplate
This project provides support for the curly bracket template syntax, implemented using finite state machines.

这个项目提供了对大括号模板语法的支持，使用有限状态机实现。

# Bracket template syntax / 大括号模板语法

Like "Mustache", but it use single curly brace, so I call it "Bracket template syntax".

类似于双大括号模板语法，但是它只使用单个大括号，所以我把它称之为“大括号模板语法”。

 correct eg:
 
 正确示例：
 
 * "hello {k1},{k2}"
 * "hello {k1}{k2}"
 
 incorrect eg:
 
 错误示例：
 
 * "hello {k1{k2}"
 * "hello {k1{k2}}"

# Usage/用法

````java
public static void main(String[] args) {

    String template = "hello {k1},{k2}";

    Map<String, Object> map = new HashMap<>();
    map.put("k1", "szw1");
    map.put("k2", "szw2");

    System.out.println(new BraceTemplate(template).merge(map));
}
````
