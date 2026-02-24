package jorge.web.app.sigmaBank.serive;

import jorge.web.app.sigmaBank.entity.*;
import jorge.web.app.sigmaBank.repository.AccountRepository;
import jorge.web.app.sigmaBank.repository.CardRepository;
import jorge.web.app.sigmaBank.repository.TransactionRepository;
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
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public Card getCard(User user) {
        return cardRepository.findByOwnerUdi(user.getUdi()).orElseThrow();
    }

    public Card createCard(double amount, User user) throws Exception {
        if (amount < 2)
            throw new IllegalArgumentException("Amount should be at least $2");

        if (!accountRepository.existsByCodeAndOwnerUdi("USD", user.getUdi()))
            throw new IllegalArgumentException("USD Account not found for this user so card cannot be created");

        var usdAccount = accountRepository.findByCodeAndOwnerUdi("USD", user.getUdi()).orElseThrow();
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

        accountHelper.createAccountTransaction(1, Type.WITHDRAW,0.00,user, usdAccount);
        accountHelper.createAccountTransaction(amount - 1, Type.WITHDRAW,0.00,user, usdAccount);
        createCardTransaction(amount, Type.CREDIT, 0.00,user, card);

        return cardRepository.save(card);
    }

    private long generateCardNumber() {
        return new RandomUtil().generateRandom(16);
    }

    public Transaction creditCard(double amount, User user) {
        var usdAccount = accountRepository.findByCodeAndOwnerUdi("USD", user.getUdi()).orElseThrow();
        usdAccount.setBalance(usdAccount.getBalance() - amount);
        accountHelper.createAccountTransaction(amount, Type.WITHDRAW,0.00,user, usdAccount);
        var card = user.getCard();
        card.setBalance(card.getBalance() + amount);
        return createCardTransaction(amount, Type.CREDIT, 0.00,user, card);

    }

    public Transaction debitCard(double amount, User user) {
        var usdAccount = accountRepository.findByCodeAndOwnerUdi("USD", user.getUdi()).orElseThrow();
        usdAccount.setBalance(usdAccount.getBalance() + amount);
        accountHelper.createAccountTransaction(amount, Type.DEPOSIT,0.00,user, usdAccount);
        var card = user.getCard();
        card.setBalance(card.getBalance() - amount);
        return createCardTransaction(amount, Type.DEBIT, 0.00,user, card);
    }

    private Transaction createCardTransaction(double amount, Type type, double txFee, User user, Card card) {
        var tx = Transaction.builder()
                .txFee(txFee)
                .amount(amount)
                .type(type)
                .status(Status.COMPLETED)
                .owner(user)
                .card(card)
                .build();
        return transactionRepository.save(tx);
    }

}
