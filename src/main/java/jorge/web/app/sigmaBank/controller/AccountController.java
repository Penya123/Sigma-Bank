package jorge.web.app.sigmaBank.controller;


import jorge.web.app.sigmaBank.dto.AccountDto;
import jorge.web.app.sigmaBank.dto.ConvertDto;
import jorge.web.app.sigmaBank.dto.TransferDto;
import jorge.web.app.sigmaBank.entity.Account;
import jorge.web.app.sigmaBank.entity.Transaction;
import jorge.web.app.sigmaBank.entity.User;
import jorge.web.app.sigmaBank.serive.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/accounts")
@AllArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody AccountDto accountDto, Authentication authentication) throws Exception {
        var user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(accountService.createAccount(accountDto, user));
    }

    @GetMapping
    public ResponseEntity<List<Account>> getUserAccounts(Authentication authentication){
        var user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(accountService.getUserAccounts(user.getUdi()));
    }

    @PostMapping("/transfer")
    public ResponseEntity<Transaction> transferFunds(@RequestBody TransferDto transferDto, Authentication authentication) throws Exception {
        var user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(accountService.transferFunds(transferDto, user));
    }

    @GetMapping("/rates")
    public ResponseEntity<Map<String, Double>> getExchangRate(){
        return ResponseEntity.ok(accountService.getExchangeRate());
    }


    @PostMapping("/convert")
    public ResponseEntity<Transaction> convertCurrency(@RequestBody ConvertDto convertDto, Authentication authentication) throws Exception {
        var user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(accountService.convertCurrency(convertDto, user));
    }
}
