package examination.teacherAndStudents.practice;


import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExtractNumbers {
    public static List<Double> extractNumbers(String input) {
        List<Double> numbers = new ArrayList<>();

        // Define the pattern to match numeric values (including decimals)
        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        Matcher matcher = pattern.matcher(input);

        // Find all matches and add them to the list
        while (matcher.find()) {
            String match = matcher.group();
            try {
                double number = Double.parseDouble(match);
                numbers.add(number);
            } catch (NumberFormatException g) {
                // Ignore if parsing as a double fails
            }
        }

        return numbers;
    }

    public static final String generateStudentId() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        String yearString = String.valueOf(currentYear);
        StringBuilder randomNumbers = new StringBuilder();
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < 4; i++) {
            int randomNumber = random.nextInt(10);
            randomNumbers.append(randomNumber);
        }

        return  "STU" + yearString + randomNumbers;
    }
    public static String localDateTimeConverter(LocalDateTime localDateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM , yyyy hh:mm a", Locale.US);
        return formatter.format(localDateTime);
    }

    public static void main(String[] args) {
        String input = "abs/-yehs123.2jdjs89j=-='0-090-1.;;'sha09vfgvf1876gvgyv22ghvgv32kjnji999bh bhee";
        List<Double> result = ExtractNumbers.extractNumbers(input);
        System.out.println(result);
        System.out.println(LocalDateTime.now());

        System.out.println(ExtractNumbers.localDateTimeConverter(LocalDateTime.now()));
    }
}