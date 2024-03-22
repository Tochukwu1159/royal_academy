//package examination.teacherAndStudents.service;
//
//import examination.teacherAndStudents.entity.StaffPayroll;
//import examination.teacherAndStudents.entity.User;
//import examination.teacherAndStudents.repository.StaffPayrollRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//        import org.springframework.stereotype.Service;
//        import java.util.List;
//
//@Service
//public class StaffPayrollService {
//
//    @Autowired
//    private StaffPayrollRepository staffPayrollRepository;
//
//    public void calculateAndProcessPayroll() {
//        List<StaffPayroll> employees = staffPayrollRepository.findAll();
//        for (User employee : employees) {
//            double salary = calculateSalary(employee);
//            processPayroll(employee, salary);
//        }
//    }
//
//    private double calculateSalary(User employee) {
//        double baseSalary = employee.getBaseSalary();
//        double bonuses = calculateBonuses(employee);
//        double deductions = calculateDeductions(employee);
//        return baseSalary + bonuses - deductions;
//    }
//
//    private double calculateBonuses(User employee) {
//        return 0; // Placeholder implementation
//    }
//
//    private double calculateDeductions(User employee) {
//        return 0; // Placeholder implementation
//    }
//
//    private void processPayroll(User employee, double salary) {
//        StaffPayroll payStub = generatePayStub(employee, salary);
//        System.out.println("Pay Stub for StaffPayroll: " + employee.getName());
//        System.out.println(payStub);
//    }
//
//    private StaffPayroll generatePayStub(User employee, double salary) {
//        StaffPayroll payStub = new StaffPayroll();
//        payStub.setStaffPayroll(employee);
//        payStub.setSalary(salary);
//        return payStub;
//    }
//
//}
