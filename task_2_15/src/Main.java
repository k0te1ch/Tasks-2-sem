/*
2-15
Дан список студентов (свойства: фио, курс). Модифицировать список таким образом,

чтобы вначале шли студенты 1-го курса, затем 2-го, далее 3-го и т.д. (упорядочивать
студентов по фамилии в рамках одного курса не требуется). Новых объектов ListNode
/ ListItem – не создавать.
Будем считать, что точно известно, что курс может быть от 1 до 6.
Подсказка: для каждого курса завести 2 переменных – для первого и последнего
элемента (можно использовать массив). Далее пройти по переданному списку,
переприсоединяя очередной элемент к нужному списку (соответствующему курсу
студента). И в конце объединить полученные списки по курсам в один (при этом не
забыть учесть, что на каком-то курсе может вообще не оказаться студентов).
 */


import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        ListNode list = new ListNode(Arrays.asList(
                Arrays.asList("3", "Хехехе!"),
                Arrays.asList("1", "111111 (2)"),
                Arrays.asList("5", "555555 (3)"),
                Arrays.asList("6", "666666 (4)"),
                Arrays.asList("4", "444444 (5)"),
                Arrays.asList("3", "333333 (6)")));
        list.sort();
        list.print();
    }
}