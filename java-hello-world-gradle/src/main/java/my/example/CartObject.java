package my.example;

import dev.restate.sdk.ObjectContext;
import dev.restate.sdk.annotation.Handler;
import dev.restate.sdk.annotation.VirtualObject;

@VirtualObject
public class CartObject {

    @Handler
    public boolean addTicket(ObjectContext ctx, String ticketId){
        return true;
    }

    @Handler
    public void expireTicket(ObjectContext ctx, String ticketId){

    }

    @Handler
    public boolean checkout(ObjectContext ctx) {
        return true;
    }
}
