package nl.stokpop.afterburner.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
// needed for Jackson mapper in Feign
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class BurnerMessage {
    String message;
    String name;
    long durationInMillis;

}
