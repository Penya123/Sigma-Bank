package jorge.web.app.sigmaBank.serive;


import jorge.web.app.sigmaBank.dto.AccountDto;
import jorge.web.app.sigmaBank.entity.Account;
import jorge.web.app.sigmaBank.entity.User;
import jorge.web.app.sigmaBank.repository.AccountRepository;
import jorge.web.app.sigmaBank.serive.helper.AccountHelper;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final AccountHelper accountHelper;

    public @Nullable Account createAccount(AccountDto accountDto, User user) throws Exception {
        long accountNumber;
        validateAccountNonExistsForUser(accountDto.getCode(),user.getUdi());
        do{
            accountNumber = generateRandom(10);
        }while (accountRepository.existsByAccountNumber(accountNumber));

        var account = Account.builder()
                .accountNumber(accountNumber)
                .balance(0)
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
}
