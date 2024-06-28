package my.example;

import com.fasterxml.jackson.core.type.TypeReference;
import dev.restate.sdk.JsonSerdes;
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
        Set<String> cart = ctx.get(CART).orElseGet(HashSet::new);

        if(cart.isEmpty()){
            return false;
        }

        String idempotencyKey = ctx.random().nextUUID().toString();
        boolean paid = ctx.run(JsonSerdes.BOOLEAN,
                () -> pay(idempotencyKey, cart.size()*40));

        return true;
    }

    private boolean pay(String idempotencyKey, double totalPrice){
        System.out.println("Paying tickets for " + idempotencyKey + " and price " + totalPrice);
        return true;
    }

    private void fail(){
        throw new IllegalStateException("The handler failed");
    }
}
