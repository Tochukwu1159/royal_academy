package examination.teacherAndStudents.controller;
import examination.teacherAndStudents.dto.ApiResponse;
import examination.teacherAndStudents.dto.FundWalletRequest;
import examination.teacherAndStudents.dto.PaymentResponse;
import examination.teacherAndStudents.dto.WalletResponse;
import examination.teacherAndStudents.service.WalletService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/wallet")
public class WalletController {

    private final WalletService walletService;
    @PostMapping("/fund")
    public ResponseEntity<ApiResponse<PaymentResponse>> fundWallet(@RequestBody FundWalletRequest fundWalletRequest) throws Exception {
       PaymentResponse paymentResponse = walletService.fundWallet(fundWalletRequest);
        return new ResponseEntity<>(new ApiResponse<>("success",true,paymentResponse), HttpStatus.OK);
    }

    @GetMapping("student/balance")
    public ResponseEntity<ApiResponse<WalletResponse>> getStudentBalance() {
       WalletResponse walletResponse = walletService.getStudentWalletBalance();
        return new ResponseEntity<>(new ApiResponse<>("success",true,walletResponse),HttpStatus.OK);
    }




}
