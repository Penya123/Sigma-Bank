package jorge.web.app.sigmaBank.controller;


import jorge.web.app.sigmaBank.entity.Transaction;
import jorge.web.app.sigmaBank.entity.User;
import jorge.web.app.sigmaBank.serive.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    public List<Transaction> getAllTransactions(@RequestParam String page, Authentication auth){
        return transactionService.getAllTransactions(page, (User) auth.getPrincipal());
    }

    @GetMapping("/c/{cardId}")
    public List<Transaction> getTransactionsByCardId(@PathVariable String cardId, @RequestParam String page, Authentication auth){
        return transactionService.getTransactionsByCardId(cardId, page, (User) auth.getPrincipal());
    }

    @GetMapping("/a/{accountId}")
    public List<Transaction> getTransactionsByAccount(@PathVariable String accountId, @RequestParam String page, Authentication auth){
        return transactionService.getTransactionsByAccountId(accountId, page, (User) auth.getPrincipal());
    }

}
