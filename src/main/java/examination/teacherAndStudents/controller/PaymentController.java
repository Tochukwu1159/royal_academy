package examination.teacherAndStudents.controller;

import examination.teacherAndStudents.service.PaymentService;
import examination.teacherAndStudents.utils.DuesStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/pay")
    public ResponseEntity<String> payDue(@RequestParam Long dueId) {
        paymentService.payDue(dueId);
        return ResponseEntity.ok("Payment successful");
    }

    @PostMapping("/dues/{duesId}/submit-receipt")
    public ResponseEntity<?> submitReceiptPhoto(
            @PathVariable Long duesId,
            @RequestBody byte[] receiptPhoto) {
        paymentService.submitReceiptPhoto(duesId, receiptPhoto);
        return ResponseEntity.ok("Receipt photo submitted successfully");
    }

    @PostMapping("/admin/dues/{duesId}/review")
    public ResponseEntity<?> reviewAndSetStatus(
            @PathVariable Long duesId,
            @RequestParam DuesStatus newStatus) {
        paymentService.reviewAndSetStatus(duesId, newStatus);
        return ResponseEntity.ok("Dues status updated successfully");
    }
}