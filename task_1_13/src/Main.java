/*
1-13
Реализовать класс Формула (интерпретатор). Должна быть возможность работы с
данным классом следующим образом:
 Formula f = new Formula();
 f.prepare("x*x +y");
 double v = f.execute(2, 5.7);
 // и т.п.
*/


import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        Formula_x f = new Formula_x();
        f.prepare("-x+x*x");
        Map<String, Double> s = new HashMap<>();
        s.put("x", 5d);
        System.out.println(f.execute(s)); // 20


        f.prepare("(-x+x)*x");
        s = new HashMap<>();
        s.put("x", 5d);
        System.out.println(f.execute(s)); // 0


        f.prepare("x      /y");
        s = new HashMap<>();
        s.put("x", 10d);
        s.put("y", 5d);
        System.out.println(f.execute(s)); // 2


        f.prepare("x        /  (  -y)        ");
        s = new HashMap<>();
        s.put("x", 10d);
        s.put("y", 5d);
        System.out.println(f.execute(s)); // -2


        f.prepare("x    /y");
        s = new HashMap<>();
        s.put("x", 10.10);
        s.put("y", 5d);
        System.out.println(f.execute(s)); // 2.02


        f.prepare("x");
        s = new HashMap<>();
        s.put("x", 556405d);
        System.out.println(f.execute(s)); // 556405


        f.prepare("(-x-x)*x");
        s = new HashMap<>();
        s.put("x", 5d);
        System.out.println(f.execute(s)); // -50


        f.prepare("z/y*x");
        s = new HashMap<>();
        s.put("x", 5d);
        s.put("y", 0d);
        s.put("z", 25d);
        System.out.println(f.execute(s)); // inf


        f.prepare("x*x/x*(-y)");
        s = new HashMap<>();
        s.put("x", 25d);
        s.put("y", 1d);
        System.out.println(f.execute(s)); // -25


        f.prepare("-x*y*y/y");
        s = new HashMap<>();
        s.put("x", 1d);
        s.put("y", 25d);
        System.out.println(f.execute(s)); // -25

        f.prepare("-x*y*y/y");
        s = new HashMap<>();
        s.put("x", 0d);
        s.put("y", 25d);
        System.out.println(f.execute(s)); // 0


        Formula fNotX = new Formula();
        fNotX.prepare("-x/y");
        s = new HashMap<>();
        s.put("x", 1d);
        s.put("y", 4d);
        System.out.println(fNotX.execute(1d, 4d)); // -0.25
    }
}