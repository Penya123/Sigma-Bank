package jorge.web.app.sigmaBank.serive.helper;

import jorge.web.app.sigmaBank.entity.*;
import jorge.web.app.sigmaBank.repository.AccountRepository;
import jorge.web.app.sigmaBank.repository.TransactionRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.naming.OperationNotSupportedException;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Getter
public class AccountHelper {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    private final Map<String, String> CURRENCIES = Map.of(
            "USD", "United States Dollar",
            "EUR", "Euro",
            "GBP", "British Pound",
            "JPY", "Japanese Yen",
            "NGN", "Nigerian Naira",
            "INR", "Indian Rupee",
            "MXN", "Mexican Peso"
    );

    public Transaction performTransfer(Account senderAccount, Account receiverAccount, double amount, User user) throws Exception {
        validateSufficientFunds(senderAccount, (amount * 1.01));
        senderAccount.setBalance(senderAccount.getBalance() - (amount * 1.01));
        accountRepository.saveAll(List.of(senderAccount, receiverAccount));
        var senderTransaction = Transaction.builder()
                .account(senderAccount)
                .status(Status.COMPLETED)
                .type(Type.WITHDRAW)
                .txFee(amount * 1.01)
                .amount(amount)
                .owner(senderAccount.getOwner())
                .build();

        var recipientTransaction = Transaction.builder()
                .account(senderAccount)
                .status(Status.COMPLETED)
                .type(Type.DEPOSIT)
                .amount(amount)
                .owner(receiverAccount.getOwner())
                .build();
        return transactionRepository.saveAll(List.of(senderTransaction, recipientTransaction)).getFirst();
    }

    public void validateAccountOwner(Account account, User user) throws Exception {
        if (!account.getOwner().getUdi().equals(user.getUdi())) throw new Exception("Invalid account owner");
    }

    public void validateSufficientFunds(Account account, double amount) throws OperationNotSupportedException {
        if (account.getBalance() < amount) throw new OperationNotSupportedException("Insufficient funds in the account");

    }

}
