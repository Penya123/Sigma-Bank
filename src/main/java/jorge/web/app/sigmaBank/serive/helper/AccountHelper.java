package jorge.web.app.sigmaBank.serive.helper;

import jorge.web.app.sigmaBank.dto.ConvertDto;
import jorge.web.app.sigmaBank.entity.*;
import jorge.web.app.sigmaBank.repository.AccountRepository;
import jorge.web.app.sigmaBank.repository.TransactionRepository;
import jorge.web.app.sigmaBank.serive.ExchangeRateService;
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
    private final ExchangeRateService exchangeRateService;

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
        receiverAccount.setBalance(receiverAccount.getBalance() + amount);
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
                .account(receiverAccount)
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

    public void validateAmount(double amount) throws Exception {
        if (amount <= 0)
            throw new IllegalArgumentException("Invalid amount");
    }

    public void validateDiffetentCurrencyType(ConvertDto convertDto) throws Exception {
        if (convertDto.getToCurrency().equals(convertDto.getFromCurrency()))
            throw new IllegalArgumentException("Conversion between the same currency is not allowed");
    }

    public void validateAccountOwnerShip(ConvertDto convertDto, String uid) throws Exception {
        accountRepository.findByCodeAndOwnerUdi(convertDto.getFromCurrency(), uid).orElseThrow();
        accountRepository.findByCodeAndOwnerUdi(convertDto.getToCurrency(), uid).orElseThrow();
    }

    public void validateConversion(ConvertDto convertDto, String udi) throws Exception {
        validateDiffetentCurrencyType(convertDto);
        validateAccountOwnerShip(convertDto, udi);
        validateAmount(convertDto.getAmount());
        validateSufficientFunds(accountRepository.findByCodeAndOwnerUdi(convertDto.getFromCurrency(), udi).get(),convertDto.getAmount());
    }

    public Transaction convertCurrency(ConvertDto convertDto, User user) throws Exception {
        validateConversion(convertDto, user.getUdi());
        var rates = exchangeRateService.getRates();

        var sendingRates = rates.get(convertDto.getFromCurrency());
        var recievingRates = rates.get(convertDto.getToCurrency());

        var computedAmount = (recievingRates/sendingRates) * convertDto.getAmount();

        var fromAccount = accountRepository.findByCodeAndOwnerUdi(convertDto.getFromCurrency(), user.getUdi()).get();
        var toAccount = accountRepository.findByCodeAndOwnerUdi(convertDto.getToCurrency(), user.getUdi()).get();

        fromAccount.setBalance(fromAccount.getBalance() - (convertDto.getAmount() * 1.01));
        toAccount.setBalance(toAccount.getBalance() + computedAmount);

        accountRepository.saveAll(List.of(fromAccount, toAccount));

        var transaction = Transaction.builder()
                .owner(user)
                .amount(convertDto.getAmount())
                .txFee(convertDto.getAmount() * 0.01)
                .account(fromAccount)
                .status(Status.COMPLETED)
                .type(Type.CONVERSION)
                .build();
        return  transaction;
    }

}
