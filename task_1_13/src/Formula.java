import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Formula {
    private String preparing;

    public Formula() {
    }

    private static Double evaluate(String expression) {

        expression = expression.replaceAll(" ", "");
        List<String> strList = new ArrayList<>(Arrays.asList(expression.trim().split("(?!^)")));
        int start = 0;
        StringBuilder str = new StringBuilder();
        boolean first = true;
        for (int i = 0; i < strList.size(); i++) {
            char ch = strList.get(i).charAt(0);
            if (Character.isDigit(ch) || (start == i && ch == '-' && first)) {
                str.append(ch);
                if (start == i && ch == '-') first = false;
            } else if (ch == '.') {
                str.append('.');
            } else if (str.length() > 1) {
                strList.add(start, str.toString());
                for (int d = start; d < i; d++) {
                    strList.remove(start + 1);
                }
                i = start;
                start = i + 1;
                str = new StringBuilder();
            } else {
                str = new StringBuilder();
                start = i + 1;
            }
        }
        if (str.length() > 1) {
            strList.add(start, str.toString());
            for (int d = 0; d < str.length(); d++) {
                strList.remove(start + 1);
            }
        }

        if (strList.contains("(")) {
            for (int i = strList.indexOf("(") + 1; i < strList.size(); i++) {
                StringBuilder recursion = new StringBuilder();
                if (strList.get(i).equals("(")) {
                    for (int j = i; j < strList.lastIndexOf(")"); j++) {
                        recursion.append(strList.get(j));
                    }
                    String test = expression.substring(expression.indexOf("("), expression.lastIndexOf(")") + 1);
                    String testRecursion = String.valueOf(evaluate(recursion.toString()));
                    expression = expression.replace(test, testRecursion);
                    strList = new ArrayList<>();
                    Collections.addAll(strList, expression.trim().split("(?!^)"));
                }
                StringBuilder recursion2 = new StringBuilder();
                if (strList.get(i).equals(")")) {

                    for (int j = strList.indexOf("(") + 1; j < i; j++) {
                        recursion2.append(strList.get(j));
                    }
                    String test2 = expression.substring(expression.indexOf("("), expression.lastIndexOf(")") + 1);
                    String testRecursion2 = String.valueOf(evaluate(recursion2.toString()));
                    expression = expression.replace(test2, testRecursion2);
                    strList = new ArrayList<>();
                    Collections.addAll(strList, expression.trim().split("(?!^)"));
                }
            }
        }
        List<String> StringList2 = new ArrayList<>();
        Collections.addAll(StringList2, expression.trim().split("(?!^)"));
        start = 0;
        str = new StringBuilder();
        first = true;
        for (int i = 0; i < StringList2.size(); i++) {
            char ch = StringList2.get(i).charAt(0);
            if (Character.isDigit(ch) || (start == i && ch == '-' && first)) {
                str.append(ch);
                if (start == i && ch == '-') first = false;
            } else if (ch == '.') {
                str.append('.');
            } else if (str.length() > 1) {
                StringList2.add(start, str.toString());
                for (int d = start; d < i; d++) {
                    StringList2.remove(start + 1);
                }
                i = start;
                start = i + 1;
                str = new StringBuilder();
            } else {
                str = new StringBuilder();
                start = i + 1;
            }
        }
        if (str.length() > 1) {
            StringList2.add(start, str.toString());
            for (int d = 0; d < str.length(); d++) {
                StringList2.remove(start + 1);
            }
        }


        while (StringList2.size() != 0) {
            Double result = 0d;
            if (StringList2.contains("/")) {
                int index = StringList2.indexOf("/");
                if (Double.parseDouble(StringList2.get(index + 1)) == 0) return Double.MAX_VALUE;
                result = Double.parseDouble(StringList2.get(index - 1)) / Double.parseDouble(StringList2.get(index + 1));
                StringList2.add(index - 1, String.valueOf(result));
                StringList2.remove(index + 2);
                StringList2.remove(index + 1);
                StringList2.remove(index);
            } else if (StringList2.contains("*")) {
                int index = StringList2.indexOf("*");
                result = Double.parseDouble(StringList2.get(index - 1)) * Double.parseDouble(StringList2.get(index + 1));
                StringList2.add(index - 1, String.valueOf(result));
                StringList2.remove(index + 2);
                StringList2.remove(index + 1);
                StringList2.remove(index);
            } else if (StringList2.contains("-")) {
                int index = StringList2.indexOf("-");
                int lastIndex = StringList2.lastIndexOf("-");
                if (index == 0) {
                    result = 0.0 - Double.parseDouble(StringList2.get(index + 1));
                    StringList2.add(0, String.valueOf(result));
                    StringList2.remove(2);
                    StringList2.remove(1);
                } else if ((lastIndex - 2 > 0) && (StringList2.get(lastIndex - 2).equals("-"))) {
                    result = Double.parseDouble(StringList2.get(lastIndex + 1)) + Double.parseDouble(StringList2.get(lastIndex - 1));
                    StringList2.add(lastIndex - 1, String.valueOf(result));
                    StringList2.remove(lastIndex + 2);
                    StringList2.remove(lastIndex + 1);
                    StringList2.remove(lastIndex);
                } else {
                    result = Double.parseDouble(StringList2.get(index - 1)) - Double.parseDouble(StringList2.get(index + 1));
                    StringList2.add(index - 1, String.valueOf(result));
                    StringList2.remove(index + 2);
                    StringList2.remove(index + 1);
                    StringList2.remove(index);
                }
            } else if (StringList2.contains("+")) {
                int index = StringList2.indexOf("+");
                result = Double.parseDouble(StringList2.get(index - 1)) + Double.parseDouble(StringList2.get(index + 1));
                StringList2.add(index - 1, String.valueOf(result));
                StringList2.remove(index + 2);
                StringList2.remove(index + 1);
                StringList2.remove(index);
            }

            if (StringList2.size() == 1) {
                result = Double.parseDouble(StringList2.get(0));
            }

            if ((!StringList2.contains("*")) && (!StringList2.contains("/")) && (!StringList2.contains("+")) && (!StringList2.contains("-"))) {
                return result;
            }
        }
        return (double) 0;
    }

    public void prepare(String expression) {
        preparing = expression;
    }

    public Double execute(Double x, Double y) {
        preparing = preparing.replaceAll("x", String.valueOf(x));
        preparing = preparing.replaceAll("y", String.valueOf(y));
        return evaluate(preparing);
    }
}
