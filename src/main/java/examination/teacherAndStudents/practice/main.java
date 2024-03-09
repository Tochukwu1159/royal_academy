package examination.teacherAndStudents.practice;

import examination.teacherAndStudents.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Scanner;
@RequiredArgsConstructor
public class main {
    private static UserRepository userRepository;

    public static final String generateStudentId() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        String yearString = String.valueOf(currentYear);

        SecureRandom random = new SecureRandom();
        String studentId;

        do {
            StringBuilder randomNumbers = new StringBuilder();
            for (int i = 0; i < 5; i++) {
                int randomNumber = random.nextInt(10);
                randomNumbers.append(randomNumber);
            }

            studentId = "STU" + yearString + randomNumbers;
        } while (userRepository.existsByUniqueRegistrationNumber(studentId));

        return studentId;
    }
    public static void main(String[] args) {



        String studentId = generateStudentId();
        System.out.println("Generated Student ID: " + studentId);

//        Hen newHen = new Hen();
//        System.out.println(newHen.swim());
//        BigBird bigBird = new BigBirdImpl();
//        System.out.println(bigBird.hasFeather());
//        Cow cow1 = new Cow();
//        System.out.println(cow1.makeSound());
//        String name = "emeka";
//        String s = "welcome";
//        s.concat(" Home");
//        System.out.println(s);
//        System.out.println("\n");
//        StringBuilder newName = new StringBuilder("uche");
//        newName.append("nna");
//        System.out.println( newName);
//        Ladikpo ladikpo = new Ladikpo();
//        System.out.println(ladikpo.cloth());
//
//        int nere = 4;
//        Integer mer = nere;
//        Integer i = Integer.valueOf(nere);
//        System.out.println(i+ " Object");
//        System.out.println(mer+ " integer");
//        System.out.println(mer);
//
//        Integer a = new Integer(3);
//        int j = a.intValue();
//        int k = a;
//        System.out.println(j);
//        System.out.println(k);


    }
}