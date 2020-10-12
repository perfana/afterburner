package nl.stokpop.afterburner.basket.validate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import nl.stokpop.afterburner.basket.BasketRequest;
import nl.stokpop.afterburner.basket.FundReply;
import nl.stokpop.afterburner.service.AfterburnerRemote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TotalAmountCheckValidator implements Validator {

    private static final Logger log = LoggerFactory.getLogger(TotalAmountCheckValidator.class);

    private List<String> errors;

    private final AfterburnerRemote afterburnerRemote;

    public TotalAmountCheckValidator(AfterburnerRemote afterburnerRemote) {
        this.afterburnerRemote = afterburnerRemote;
    }

    @Override
    public boolean validate(BasketRequest request) {

        errors = new ArrayList<>();

        long expectedPrice = request.getPrices().parallelStream()
                .collect(Collectors.summarizingLong(Long::longValue))
                .getSum();

        String customer = request.getCustomer();
        Long totalPrice = request.getTotalPrice();
        if (expectedPrice != totalPrice) {
            errors.add("There seems to be a problem dear '" + customer + "'." +
                    " The total should be: " + expectedPrice + " but is " + totalPrice + ".");
        }

        if (!customerHasSufficientFunds(customer, totalPrice)) {
            errors.add("Dear '" + customer + "'. We regret to say you have insufficient funds to make payment of " + totalPrice + ".");
        }
        return errors.isEmpty();
    }

    private boolean customerHasSufficientFunds(String customer, long amount) {
        String reply;
        try {
            reply = afterburnerRemote.executeCall("/fund/check?customer=" + customer + "&amount=" + amount, "httpclient");
        } catch (IOException e) {
            // should we fail if the funds cannot be checked? or sell and take the risk?
            log.error("Execute remote call failed to check funds for customer " + customer, e);
            return false;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectReader fundReader = objectMapper.readerFor(FundReply.class);
        FundReply fundReply;
        try {
            fundReply = fundReader.readValue(reply);
        } catch (JsonProcessingException e) {
            // should we fail if the funds cannot be checked? or sell and take the risk?
            log.error("Customer has no sufficient funds call failed, will return false. Cannot read: " + reply, e);
            return false;
        }

        return fundReply.isHasSufficientFunds();
    }

    @Override
    public Collection<String> getErrors() {
        return errors;
    }
}
