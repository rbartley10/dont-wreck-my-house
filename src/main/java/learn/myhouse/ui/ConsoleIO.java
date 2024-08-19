package learn.myhouse.ui;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

@Component
public class ConsoleIO {
    //private static fields that represent input error messages for user
    private static final String INVALID_NUMBER
            = "[INVALID] Enter a valid number.";
    private static final String NUMBER_OUT_OF_RANGE
            = "[INVALID] Enter a number between %s and %s.";
    private static final String REQUIRED
            = "[INVALID] Value is required.";
    private static final String INVALID_DATE
            = "[INVALID] Enter a date in MM/dd/yyyy format.";

    private final Scanner scanner = new Scanner(System.in); //scanner to accept input from user

    //the required date format expected from user
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    /**
     * Method to print a string without a line
     * @param message
     */
    public void print(String message) {
        System.out.print(message);
    }

    /**
     * Method to print a string with a new line at the end
     * @param message
     */
    public void println(String message) {
        System.out.println(message);
    }

    /**
     * Method to print a formatted string
     * @param format
     * @param values
     */
    public void printf(String format, Object... values) {
        System.out.printf(format, values);
    }

    /**
     * Method to read an input as a string
     * @param prompt
     * @return the user input as a string
     */
    public String readString(String prompt) {
        print(prompt);
        return scanner.nextLine();
    }

    /**
     * Method to read a string that is required to not be null or blank
     * @param prompt
     * @return the user input as a string
     */
    public String readRequiredString(String prompt) {
        while (true) { //continuing to read string until a non blank string is entered
            String result = readString(prompt);
            if (!result.isBlank()) {
                return result;
            }
            println(REQUIRED);
        }
    }

    /**
     * Method to read an input as a string and convert it to an int
     * @param prompt
     * @return the converted int
     */
    public int readInt(String prompt) {
        while (true) { //continuing to read int until valid int is entered
            try {
                return Integer.parseInt(readRequiredString(prompt));
            } catch (NumberFormatException ex) {
                println(INVALID_NUMBER);
            }
        }
    }

    /**
     * Method to read an input as a string and convert it to a int. The input
     * is required to be between two given numbers
     * @param prompt
     * @param min
     * @param max
     * @return the converted int
     */
    public int readInt(String prompt, int min, int max) {
        while (true) { //continuing to read int until valid int is entered within range
            int result = readInt(prompt);
            if (result >= min && result <= max) {
                return result;
            }
            println(String.format(NUMBER_OUT_OF_RANGE, min, max));
        }
    }

    /**
     * Method to read an input as a string and convert it to a boolean.
     * @param prompt
     * @return the converted boolean
     */
    public boolean readBoolean(String prompt) {
        while (true) { //continuing to read input until user chooses yes (y) or no (n)
            String input = readRequiredString(prompt).toLowerCase();
            if (input.equals("y")) {
                return true;
            } else if (input.equals("n")) {
                return false;
            }
            println("[INVALID] Please enter 'y' or 'n'.");
        }
    }

    /**
     * Method to read an input as a string and convert it to a local date
     * @param prompt
     * @return the converted local date
     */
    public LocalDate readLocalDate(String prompt) {
        while (true) { //continuing to read date until valid date is entered
            String input = readRequiredString(prompt);
            try {
                return LocalDate.parse(input, formatter);
            } catch (DateTimeParseException ex) {
                println(INVALID_DATE);
            }
        }
    }

    public LocalDate readUpdateDate(String prompt, LocalDate date){

        //allowing user to enter empty string
        do{
            String start = readString(String.format("%s (%s): ",prompt,date)); //reading user date as string
            if(start.isEmpty()){ //leave loop and keep initial date value
                break;
            }else{ //checking input since it is not empty
                try { //assigning new date and ending loop if try block is successful
                    date = LocalDate.parse(start, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
                    break;
                } catch (DateTimeParseException ex) { //re-prompt user if input could not be converted to date
                    println("[INVALID] Enter a date in MM/dd/yyyy format.");
                }
            }
        }while(true);

        return date;
    }


}
