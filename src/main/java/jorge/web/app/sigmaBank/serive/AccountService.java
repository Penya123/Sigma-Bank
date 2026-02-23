package jorge.web.app.sigmaBank.serive;


import jorge.web.app.sigmaBank.dto.AccountDto;
import jorge.web.app.sigmaBank.dto.ConvertDto;
import jorge.web.app.sigmaBank.dto.TransferDto;
import jorge.web.app.sigmaBank.entity.Account;
import jorge.web.app.sigmaBank.entity.Transaction;
import jorge.web.app.sigmaBank.entity.User;
import jorge.web.app.sigmaBank.repository.AccountRepository;
import jorge.web.app.sigmaBank.serive.helper.AccountHelper;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final AccountHelper accountHelper;
    private final ExchangeRateService exchangeRateService;

    public @Nullable Account createAccount(AccountDto accountDto, User user) throws Exception {
        long accountNumber;
        validateAccountNonExistsForUser(accountDto.getCode(),user.getUdi());
        do{
            accountNumber = generateRandom(10);
        }while (accountRepository.existsByAccountNumber(accountNumber));

        var account = Account.builder()
                .accountNumber(accountNumber)
                .accountName(user.getFirstName() + " " + user.getLastName())
                .balance(1000)
                .owner(user)
                .code(accountDto.getCode())
                .symbol(accountDto.getSymbol())
                .label(accountHelper.getCURRENCIES().get(accountDto.getCode()))
                .build();
        accountRepository.save(account);
        return account;
    }

    public void validateAccountNonExistsForUser(String code, String udi) throws Exception {
        if (accountRepository.existsByCodeAndOwnerUdi(code, udi)) {
            throw new Exception("Account of this type already exists for this user");
        }
    }

    public Long generateRandom(int length){
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++){
            int digit = (int) (Math.random() * 10);
            sb.append(digit);
        }
        return Long.parseLong(sb.toString());
    }

    public @Nullable List<Account> getUserAccounts(String udi) {
        return accountRepository.findAllByOwnerUdi(udi);
    }

    public @Nullable Transaction transferFunds(TransferDto transferDto, User user) throws Exception {
        var senderAccount = accountRepository.findByCodeAndOwnerUdi(transferDto.getCode(), user.getUdi())
                .orElseThrow(() -> new UnsupportedOperationException("Account of type currency do not exists for user"));
        var receiverAccount = accountRepository.findByAccountNumber(transferDto.getRecipientAccountNumber()).orElseThrow();
        return accountHelper.performTransfer(senderAccount, (Account) receiverAccount, transferDto.getAmount(), user);
    }

    public Map<String, Double> getExchangeRate(){
        return exchangeRateService.getRates();
    }

    public Transaction convertCurrency(ConvertDto convertDto, User user) throws  Exception{
        return accountHelper.convertCurrency(convertDto, user);
    }

}
