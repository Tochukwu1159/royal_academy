package examination.teacherAndStudents.practice;

public class Cow extends Animal{
    @Override
    public String talk() {
        return "Cow cannot talk";
    }
    public String makeSound() {
        return "Cow can make sound";
    }
}
