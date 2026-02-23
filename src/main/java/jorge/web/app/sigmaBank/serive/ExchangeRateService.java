package jorge.web.app.sigmaBank.serive;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.JsonNode;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Getter
public class ExchangeRateService {
    private final RestTemplate restTemplate;

    private Map<String, Double> rates = new HashMap<>();

    private final Set<String> CURRIENCIES = Set.of(
            "USD",
            "EUR",
            "GBP",
            "JPY",
            "NGN",
            "INR",
            "MXN"
    );

    @Value("${currency.api.key}")
    private String apiKey;
    public void getExchangeRate(){
        String CURRENCY_API = "https://api.currencyapi.com/v3/latest?apikey=";

        var response = restTemplate.getForEntity(CURRENCY_API + apiKey, JsonNode.class);
        var data = Objects.requireNonNull(response.getBody()).get("data");

        for (var currency : CURRIENCIES){
            rates.put(currency, data.get(currency).get("value").doubleValue());
        }
        System.out.println(rates);
    }
}
