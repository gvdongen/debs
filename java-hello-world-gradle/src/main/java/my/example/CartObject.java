package my.example;

import com.fasterxml.jackson.core.type.TypeReference;
import dev.restate.sdk.ObjectContext;
import dev.restate.sdk.annotation.Handler;
import dev.restate.sdk.annotation.VirtualObject;
import dev.restate.sdk.common.StateKey;
import dev.restate.sdk.serde.jackson.JacksonSerdes;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

@VirtualObject
public class CartObject {

    private static final StateKey<Set<String>> CART =
            StateKey.of("tickets",
                    JacksonSerdes.of(new TypeReference<>() {}));

    @Handler
    public boolean addTicket(ObjectContext ctx, String ticketId){
        boolean success = TicketObjectClient.fromContext(ctx, ticketId)
                .reserve()
                .await();

        if (success) {
            Set<String> cart = ctx.get(CART).orElseGet(HashSet::new);
            cart.add(ticketId);
            ctx.set(CART, cart);

            CartObjectClient.fromContext(ctx, ctx.key())
                    .send(Duration.ofMinutes(15))
                    .expireTicket(ticketId);
        }

        return success;
    }

    @Handler
    public void expireTicket(ObjectContext ctx, String ticketId){

    }

    @Handler
    public boolean checkout(ObjectContext ctx) {
        return true;
    }
}
