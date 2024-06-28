package my.example;

import dev.restate.sdk.JsonSerdes;
import dev.restate.sdk.ObjectContext;
import dev.restate.sdk.annotation.Handler;
import dev.restate.sdk.annotation.VirtualObject;
import dev.restate.sdk.common.StateKey;

@VirtualObject
public class TicketObject {

    private static final StateKey<String> STATUS =
            StateKey.of("status", JsonSerdes.STRING);

    @Handler
    public boolean reserve(ObjectContext ctx){
        String status = ctx.get(STATUS).orElse("Available");

        if(status.equals("Available")){
            ctx.set(STATUS, "Reserved");
            return true;
        }

        return false;
    }

    @Handler
    public void unreserve(ObjectContext ctx){
        String status = ctx.get(STATUS).orElse("Available");

        if(!status.equals("Sold")){
            ctx.clear(STATUS);
        }
    }

    @Handler
    public void markAsSold(ObjectContext ctx){
        String status = ctx.get(STATUS).orElse("Available");

        if(status.equals("Reserved")){
            ctx.set(STATUS, "Sold");
        }
    }
}
