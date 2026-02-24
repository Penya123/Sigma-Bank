package jorge.web.app.sigmaBank.serive;

import jorge.web.app.sigmaBank.entity.Card;
import jorge.web.app.sigmaBank.entity.Transaction;
import jorge.web.app.sigmaBank.entity.Type;
import jorge.web.app.sigmaBank.entity.User;
import jorge.web.app.sigmaBank.repository.CardRepository;
import jorge.web.app.sigmaBank.serive.helper.AccountHelper;
import jorge.web.app.sigmaBank.util.RandomUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
@Transactional
public class CardService {

    private final CardRepository cardRepository;
    private final AccountHelper accountHelper;
    private final TransactionService transactionService;

    public Card getCard(User user) {
        return cardRepository.findByOwnerUdi(user.getUdi()).orElseThrow();
    }

    public Card createCard(double amount, User user) throws Exception {
        if (amount < 2)
            throw new IllegalArgumentException("Amount should be at least $2");

        if (!accountHelper.existsByCodeAndOwnerUdi("USD", user.getUdi()))
            throw new IllegalArgumentException("USD Account not found for this user so card cannot be created");

        var usdAccount = accountHelper.findByCodeAndOwnerUdi("USD", user.getUdi()).orElseThrow();
        accountHelper.validateSufficientFunds(usdAccount, amount);
        usdAccount.setBalance(usdAccount.getBalance() - amount);

        long cardNumber;

        do {
            cardNumber = generateCardNumber();
        }while (cardRepository.existsByCardNumber(cardNumber));

        Card card = Card.builder()
                .cardHolder(user.getFirstName() + " " + user.getLastName())
                .cardNumber(cardNumber)
                .exp(LocalDateTime.now().plusYears(3))
                .cvv(new RandomUtil().generateRandom(3).toString())
                .balance(amount - 1)
                .build();

        card = cardRepository.save(card);

        transactionService.createAccountTransaction(1, Type.WITHDRAW,0.00,user, usdAccount);
        transactionService.createAccountTransaction(amount - 1, Type.WITHDRAW,0.00,user, usdAccount);
        transactionService.createCardTransaction(amount, Type.WITHDRAW, 0.00,user, card);

        accountHelper.save(usdAccount);

        return card;
    }

    private long generateCardNumber() {
        return new RandomUtil().generateRandom(16);
    }

    public Transaction creditCard(double amount, User user) {
        var usdAccount = accountHelper.findByCodeAndOwnerUdi("USD", user.getUdi()).orElseThrow();
        usdAccount.setBalance(usdAccount.getBalance() - amount);
        transactionService.createAccountTransaction(amount, Type.WITHDRAW,0.00,user, usdAccount);
        var card = user.getCard();
        card.setBalance(card.getBalance() + amount);
        cardRepository.save(card);
        return transactionService.createCardTransaction(amount, Type.CREDIT, 0.00,user, card);

    }

    public Transaction debitCard(double amount, User user) {
        var usdAccount = accountHelper.findByCodeAndOwnerUdi("USD", user.getUdi()).orElseThrow();
        usdAccount.setBalance(usdAccount.getBalance() + amount);
        transactionService.createAccountTransaction(amount, Type.DEPOSIT,0.00,user, usdAccount);
        var card = user.getCard();
        card.setBalance(card.getBalance() - amount);
        cardRepository.save(card);
        return transactionService.createCardTransaction(amount, Type.DEBIT, 0.00,user, card);
    }

}
