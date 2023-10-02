package sem3;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class hw {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in, "UTF-8");

        System.out.println("Введите следующую информацию через пробел: ");
        System.out.println("1) ФИО - строки");
        System.out.println("2) Дата рождения - строка формата dd.mm.yyyy");
        System.out.println("3) Номер телефона - целое беззнаковое число без форматирования");
        System.out.println("4) Пол - символ латиницей f или m");
        String str = input.nextLine();

        Person person;

        try {
            person = Person.parse(str);
            validateData(str, person);
            saveToFile(person);
            System.out.println(person);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

        input.close();
    }

    public static void validateData(String input, Person person) throws RuntimeException {
        if (input.replaceAll("\\s+", " ").length() != person.toString().length())
            throw new RuntimeException("Некорректный ввод");
    }

    public static void saveToFile(Person person) throws RuntimeException {
        try {
            FileWriter fw = new FileWriter(getPath(person), true);
            fw.append(person.toString() + "\n");
            fw.close();
        } catch (IOException e) {
            throw new RuntimeException("Не удалось записать в файл", e);
        }
    }

    public static String getPath(Person person) {
        return person.getName().getLastName() + ".txt";
    }
}

class Person {
    private Name name;
    private Date birthday;
    private Long phoneNumber;
    private char gender;

    public Person(String firstName, String middleName, String lastName, Date birthday, Long phoneNumber, char gender) {
        this.name = new Name(firstName, middleName, lastName);
        this.birthday = birthday;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
    }

    public Person(String name, Date birthday, Long phoneNumber, char gender) {
        this.name = Name.parse(name);
        this.birthday = birthday;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
    }

    public Person(Name name, Date birthday, Long phoneNumber, char gender) {
        this.name = name;
        this.birthday = birthday;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
    }

    public Name getName() {
        return name;
    }

    public static Person parse(String input) throws RuntimeException {
        String inputCondense = input.replaceAll("\\s+", " ");
        validateInput(inputCondense);
        return new Person(
                getName(inputCondense),
                getBirthday(inputCondense),
                getPhoneNumber(inputCondense),
                getGender(inputCondense));
    }

    protected static void validateInput(String input) throws RuntimeException {
        String[] personInfo = input.split(" ");
        if (personInfo.length != 6)
            throw new RuntimeException("Вы ввели больше/меньше требуемых параметров");
    }

    protected static Name getName(String input) throws RuntimeException {
        Pattern pattern = Pattern.compile("\\b[а-яёА-ЯЁa-zA-Z]{2,} [а-яёА-ЯЁa-zA-Z]{2,} [а-яёА-ЯЁa-zA-Z]{2,}\\b");
        Matcher matcher = pattern.matcher(input);
        if (!matcher.find())
            throw new RuntimeException("ФИО не удалось найти");

        return Name.parse(input.substring(matcher.start(), matcher.end()));
    }

    protected static Date getBirthday(String input) throws RuntimeException {
        Pattern pattern = Pattern.compile("([0-9]{1,2})\\.([0-9]{1,2})\\.([0-9]{4})");
        Matcher matcher = pattern.matcher(input);
        if (!matcher.find())
            throw new RuntimeException("Дата рождения не удалось найти");
        String date = String.join("-", matcher.group(3), matcher.group(2), matcher.group(1));
        return Date.valueOf(date);
    }

    protected static long getPhoneNumber(String input) throws RuntimeException {
        Pattern pattern = Pattern.compile("[0-9]{11}");
        Matcher matcher = pattern.matcher(input);
        if (!matcher.find())
            throw new RuntimeException("Телефон не удалось найти");

        return Long.parseLong(input.substring(matcher.start(), matcher.end()));
    }

    protected static char getGender(String input) throws RuntimeException {
        Pattern pattern = Pattern.compile("\\b[fFmM]{1}\\b");
        Matcher matcher = pattern.matcher(input);
        if (!matcher.find())
            throw new RuntimeException("Пол не удалось найти");

        return input.substring(matcher.start(), matcher.end()).charAt(0);
    }

    @Override
    public String toString() {
        SimpleDateFormat formatDate = new SimpleDateFormat("dd.MM.yyyy");
        return String.join(" ",
                name.toString(),
                formatDate.format(birthday),
                Long.toString(phoneNumber),
                new String(new char[] { gender }));
    }
}

class Name {

    private String firstName;
    private String middleName;
    private String lastName;

    public Name(String firstName, String middleName, String lastName) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
    }

    public static Name parse(String input) {
        String inputCondense = input.replaceAll("\\s+", " ");
        validateInput(inputCondense);
        String[] fullName = input.split(" ");
        String firstName = getCapitalized(fullName[1]);
        String middleName = getCapitalized(fullName[2]);
        String lastName = getCapitalized(fullName[0]);
        return new Name(firstName, middleName, lastName);
    }

    protected static String getCapitalized(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    protected static void validateInput(String input) throws RuntimeException {
        String[] personInfo = input.split(" ");
        if (personInfo.length != 3)
            throw new RuntimeException("ФИО: указаны не все значения");
    }

    public String getFullName() {
        return String.join(" ", getLastName(), getFirstName(), getMiddleName());
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public String toString() {
        return getFullName();
    }
}