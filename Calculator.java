package Calculator;

import java.io.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Calculator {
    private int firstOperand;
    private int secondOperand;
    private String operator;
    private String rawString;
    private String systemOfCalculating;


    enum RomanNumeral {
        I(1), IV(4), V(5), IX(9), X(10),
        XL(40), L(50), XC(90), C(100),
        CD(400), D(500), CM(900), M(1000);

        private int value;

        RomanNumeral(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static List<RomanNumeral> getReverseSortedValues() {
            return Arrays.stream(values())
                    .sorted(Comparator.comparing((RomanNumeral e) -> e.value).reversed())
                    .collect(Collectors.toList());
        }
    }

    public int romanToArabic(String input) {
        String romanNumeral = input.toUpperCase();
        int result = 0;

        List<RomanNumeral> romanNumerals = RomanNumeral.getReverseSortedValues();

        int i = 0;

        while ((romanNumeral.length() > 0) && (i < romanNumerals.size())) {
            RomanNumeral symbol = romanNumerals.get(i);
            if (romanNumeral.startsWith(symbol.name())) {
                result += symbol.getValue();
                romanNumeral = romanNumeral.substring(symbol.name().length());
            } else {
                i++;
            }
        }

        if (romanNumeral.length() > 0) {
            throw new IllegalArgumentException(input + " cannot be converted to a Arabic Numeral");
        }

        return result;
    }

    public Calculator() {
    }

    public void setRawString(String expression) {
        this.rawString = expression;
    }

    public String getSystemOfCalculating() {return this.systemOfCalculating;}

    public String getRawString(){
        return this.rawString;
    }

    public boolean hasRoman(String input) {
        return input.contains("I") || input.contains("V") ||
                input.contains("X") || input.contains("C") ||
                input.contains("D") || input.contains("M") ||
                input.contains("L");
    }

    public boolean hasDigitsOrOperands(String input) {
        char[] charArray = input.toCharArray();
        for(char element : charArray) {
            if (Character.isDigit(element) || element == '+'
                    || element == '-' || element == '*'
                    || element == '/') {
                return true;
            }
        }
        return false;
    }

    public void parseRawString() throws Exception {
        String[] splittedExpression = this.rawString.split(" ");
        String firstOperand;
        String secondOperand;
        if (splittedExpression.length > 3) {
            if (hasDigitsOrOperands(splittedExpression[3])) {
                throw new IllegalArgumentException("There are more than 3 elements in the expression");
            }
        }
        if (splittedExpression.length < 3) {
            throw new IllegalArgumentException("Number of elements less than 3 elements in the expression");
        }
        firstOperand = splittedExpression[0];
        this.operator = splittedExpression[1];
        secondOperand = splittedExpression[2];

        if (hasRoman(firstOperand) != hasRoman(secondOperand)) {
            throw new Exception("Different data type");
        } else {
            if (hasRoman(firstOperand) || hasRoman(secondOperand)) {
                this.systemOfCalculating = "Roman";
                this.firstOperand = romanToArabic(firstOperand);
                this.secondOperand = romanToArabic(secondOperand);
            } else {
                this.systemOfCalculating = "Arabic";
                this.firstOperand = Integer.parseInt(splittedExpression[0]);
                this.secondOperand = Integer.parseInt(splittedExpression[2]);
            }
        }
        if (Objects.equals(this.systemOfCalculating, "Arabic")
                && (this.firstOperand > 10 || this.firstOperand < 0
                || this.secondOperand > 10 || this.secondOperand < 0)
                || (Objects.equals(this.systemOfCalculating, "Roman")
                && (this.firstOperand > 10 || this.firstOperand < 1
                || this.secondOperand > 10 || this.secondOperand < 1))) {
            throw new IllegalArgumentException("Numbers which you can use: (0 < x <= 10 in arabic and I < x < X in roman) only");
        }
    }

    public String arabicToRoman(int result) throws IllegalArgumentException {
        if (result <= 0) {
            throw new IllegalArgumentException(result + "is not in a range");
        }
        List<RomanNumeral> romanNumerals = RomanNumeral.getReverseSortedValues();

        int i = 0;
        StringBuilder sb = new StringBuilder();

        while ((result > 0) && (i < romanNumerals.size())) {
            RomanNumeral currentSymbol = (RomanNumeral) romanNumerals.get(i);
            if (currentSymbol.getValue() <= result) {
                sb.append(currentSymbol.name());
                result -= currentSymbol.getValue();
            } else {
                i++;
            }
        }
        return sb.toString();
    }

    public String calc() throws Exception {
        parseRawString();
        switch (this.operator) {
            case "+":
                return String.valueOf(this.firstOperand + this.secondOperand);
            case "-":
                return String.valueOf(this.firstOperand - this.secondOperand);
            case "*":
                return String.valueOf(this.firstOperand * this.secondOperand);
            case "/":
                return String.valueOf(this.firstOperand / this.secondOperand);
            default:
                throw new IllegalArgumentException("is not in a range");
        }
    }

    public static void main(String[] args) throws Exception {
        boolean flag = true;
        String result;
        String answer;
        Calculator calc = new Calculator();
        while (flag) {
            InputStream inputStream = System.in;
            Reader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            System.out.println("Input expression please: ");
            String rawString = bufferedReader.readLine(); //читаем строку с клавиатуры
            calc.setRawString(rawString);
            result = calc.calc();
            if (calc.hasRoman(calc.getRawString()) && calc.hasRoman(calc.getRawString())) {
                result= calc.arabicToRoman(Integer.parseInt(result));
            }
            System.out.println("The final value is : " + result + " system of calculating is : " + calc.getSystemOfCalculating());
            System.out.println("Do you want to carry on?");
            answer = bufferedReader.readLine(); //читаем строку с клавиатуры
            if (answer.toLowerCase().contains("no")) {
                flag = false;
                System.out.println("Finishing calculating.");
            } else if(answer.toLowerCase().contains("yes")){
                System.out.println("Continue calculating...");
            }
        }
    }
}
