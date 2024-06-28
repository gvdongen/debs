package my.example;

import dev.restate.sdk.ObjectContext;
import dev.restate.sdk.annotation.Handler;
import dev.restate.sdk.annotation.VirtualObject;

@VirtualObject
public class TicketObject {

    @Handler
    public boolean reserve(ObjectContext ctx){
        return true;
    }

    @Handler
    public void unreserve(ObjectContext ctx){
    }

    @Handler
    public void markAsSold(ObjectContext ctx){

    }

}
