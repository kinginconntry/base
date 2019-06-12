package com.needto.simulate.data;

import com.needto.simulate.entity.Response;
import org.springframework.context.ApplicationEvent;

/**
 * @author Administrator
 */
public class SimulateResponseEvent extends ApplicationEvent {

    private Response response;
    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public SimulateResponseEvent(Object source, Response response) {
        super(source);
        this.response = response;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}
