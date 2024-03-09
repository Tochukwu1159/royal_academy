package examination.teacherAndStudents.service;

import examination.teacherAndStudents.utils.DuesStatus;

public interface PaymentService {
    void payDue(Long dueId);
    void reviewAndSetStatus(Long duesId, DuesStatus newStatus);
    void submitReceiptPhoto(Long duesId, byte[] receiptPhoto);
}
