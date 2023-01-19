package eixo.games.thingserver;

import eixo.games.thingserver.persistence.ThingSaverFacade;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
/**
 * Exposes a Service Endpoint for machine/machine interaction for the Thing Server
 * Any incidental inspiration from Dr Seuss's "The Cat in the Hat" is purely intentional
 * <a href="https://en.wikipedia.org/wiki/The_Cat_in_the_Hat">...</a>
 * Simple Storage is the easiest way to demonstrate CRUD life cycle (although no update)
 * TODO: Allow one or more things to be stored or removed or verified in one service call
 * TODO: Allow other "types" of things to be Stored (How does this change the complexity of our Service?)
 * TODO: Improve error handling as current error handling not clear enough to API consumers and directly manipulating response breaking a clean encapsulation
 * DISCUSSION POINTS:
 *   1. We applaud Spring Boot on it's expressiveness (notice very little boiler plate code)
 *   2. Service Endpoints typically should reflect list inputs - why? (1 x n problem)
 *   3. Service Endpoints balance between grouping calls or having more specialized calls
 *     - count of things and unique things could be one service call
 *       - complexity of single interface x too many calls
 *       - time to compute versus time to call (marshall)
 *
 */
@RestController
@Slf4j //In Log we trust, thanks Lombok for removing the boilerplate
public class ServiceController {
    @Autowired //Useful Dependency Injection @see BeanFactory
    private ThingSaverFacade thingSaverFacade;

    /**
     * Add one thing to our ThingServer, allows duplicates
     * Fails on empty thing
     * TODO: No error handling a bit too simplistic but possible for an in memory solution
     * @param thing to hold
     * @return wrapped payload
     * @see #wrapPayload
     */
    @PutMapping(path="/api/hold")
    Map<String,Object> holdThing(@RequestParam(required = true) String thing,HttpServletResponse response) {
        //Trusting required=true above to always return non-blank String did not work as an empty string is passed and is indeed a valid argument for Spring but not for us
        //DISCUSSION POINT: Relying on Spring default validation changes significantly how our API is consumed, call without a parameter at all and verify
        //adding validation
        if(StringUtils.isBlank(thing)) {
            //HTTP return code signifies a client error but possibly not ideal
            return wrapPayload(null,String.format("blank thing=%s not allowed", thing),HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE,0, response);
        }
        //How hard is it to allow empty, null and blank strings? how much would the code change?
        //And how much harder would it make the operation of this service?
        thingSaverFacade.hold(thing);
        return wrapPayload(String.format("thing=%s held",thing),1);
    }

    /**
     * Removes all instances of thing from the ThingServer
     * @param thing the Thing to remove
     * @return wrapped payload 
     * @see #wrapPayload
     */
    @DeleteMapping(path="/api/remove")
    Map<String,Object> removeThing(@RequestParam(required = true) String thing, HttpServletResponse response) {
        int count = thingSaverFacade.remove(thing);
        int responseCode;
        String error=null, message=null;
        if(count < 1) {
            //DISCUSSION POINTS: 404, not found may not be ideal here
            responseCode = HttpServletResponse.SC_NOT_FOUND;
            error = String.format("No thing=%s found to remove",thing);
        } else {
            responseCode = HttpServletResponse.SC_OK;
            message = String.format("%s instances of thing=%s removed",count, thing);
        }
        return wrapPayload(message,error,responseCode,count, response);
    }

    /**
     * Returns List of All Things Stored in given Order
     * DISCUSSION POINTS: Notice this payload is unwrapped meaning the system reports directly the list of things
     * @return List of Things
     */
    @GetMapping(path="/api/all")
    List<String> getAllThings(){
        return thingSaverFacade.getAllThings();
    }

    /**
     * Returns a sorted Set of Unique Things
     * @return Set of Unique Things
     */
    @GetMapping(path="/api/all/unique")
    Set<String> getUniqueThings() {
        return thingSaverFacade.getAllThingsUnique();
    }

    /**
     * Get list of All things that match this Thing String in our Thing Server
     * @param thing
     * @return list of all things that match this Thing String in our Thing Server
     */
    @GetMapping(path="/api/get")
    List<String> getThing(@RequestParam(required = true) String thing) {
        return thingSaverFacade.getThing(thing);
    }

    /**
     * Get the count of Things Stored
     * DISCUSSION POINT:  + Notice this is a wrapped payload where the count (desired payload) is wrapped in an envelope with other context
     *                      An Integer or Long response depending on max expected size is also possible but it is sometimes
     *                      difficult to parse a direct number especially when an error happens but both are possible and can be used
     *                      depending on use case.
     *                    + Notice there is no count of getThing method, you have to get the list and count from that.
     *                      Seems not worth one more service call when the payload is small, for all the elements it may be too cumbersome
     *                      to marshall all of them through the wire when the consumer just wants a count.
     * @return Wrapped Payload
     * @see #wrapPayload(String, int) 
     */
    @GetMapping(path="/api/all/count")
    Map<String,Object> howManyThings() {
        int count = thingSaverFacade.getAllThings().size();
        return wrapPayload(String.format("server contains %s things", count), count);
    }

    /**
     * Get the count of Unique Things Stored
     * @return wrapped payload
     * @see #wrapPayload(String, int) 
     */
    @GetMapping(path="/api/all/unique/count")
    Map<String,Object> howManyUniqueThings() {
        int count = thingSaverFacade.getAllThingsUnique().size();
        return wrapPayload(String.format("server contains %s unique things", count), count);
    }

    /**
     *  Utility method to render a wrapped payload to marshall as a response
     * @param message a human readable convenience message to display on success
     * @param error a human readable convenience error message to display on failure as defined in each individual call
     * @param responseCode an HTTP Response Code that best represents the result of this interaction
     * @param count A count of affected entities by this operation
     * @param response HttpServletResponse tied to this interaction
     * @return a map to be later rendered as JSON
     */
    private Map<String,Object> wrapPayload(String message, String error, int responseCode, int count, HttpServletResponse response) {
        if(responseCode<0) throw new IllegalArgumentException(String.format("method received invalid http responseCode=%s",responseCode));
        Map<String,Object> retVal = new HashMap<>();
        //snake_casing here tends to read better than camelCase
        //removing nulls to simplify consumption of response payload on human readable pieces
        if(StringUtils.isNotBlank(message)) retVal.put("status_message", message);
        if(StringUtils.isNotBlank(error)) retVal.put("error_message",error);
        if(response!=null) response.setStatus(responseCode);
        retVal.put("response_status_code", responseCode);
        if(count>=0) retVal.put("count",count);
        return retVal;
    }

    /**
     * Utility method to render a wrapped payload to marshall as a response
     * @see #wrapPayload(String, String, int, int, HttpServletResponse) 
     */
    private Map<String,Object> wrapPayload(String message, int count) {
        return wrapPayload(message,null, HttpServletResponse.SC_OK, count,null);
    }

}
