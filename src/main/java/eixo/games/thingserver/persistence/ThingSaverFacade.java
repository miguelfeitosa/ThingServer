package eixo.games.thingserver.persistence;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
/**
 * Facade to encapsulate the persistence of Things (currently Strings)
 * Note: For more info on Facade Design Pattern see <a href="https://www.amazon.com/Design-Patterns-Object-Oriented-Addison-Wesley-Professional-ebook/dp/B000SEIBB8">...</a>
 * TODO: Add a persistence server to allow "permanent data storage"
 * DISCUSSION POINTS:
 *  1. How would error handling and code initialization have to change if we add some kind of database here
 *    - SQL, noSQL, plain file - advantages, disadvantages
 *    - how would consumers of this class have to change as well
 *      - a little bit more complexity becomes a lot more complexity pretty quickly
 *    - would it be worth decoupling this class from the consumers with an interface
 *      - abstraction, encapsulation x again complexity of initialization
 *
 */
@Slf4j //In Log we trust, thanks Lombok for removing the boilerplate
public class ThingSaverFacade {
    /**
     * In memory only thread safe (synchronized) List
     * Stores our Things
     * Considered Collections.SynchronizedList but not enough as using an iterator
     */
     private final List<String> storage = new ArrayList<>();

    /**
     * Singleton for this Application
     * Note: For singleton design pattern see <a href="https://www.amazon.com/Design-Patterns-Object-Oriented-Addison-Wesley-Professional-ebook/dp/B000SEIBB8">...</a>
     * TODO: Consider allowing multiple instances but making static the backing storage collection
     *       probably an easier to change design when later introducing full database
     *
     */
      private static final ThingSaverFacade singleton = new ThingSaverFacade();

    /**
     *  private Constructor to restrict initialization and enforce Singleton Design pattern
     */
     private ThingSaverFacade() {}

    /**
     *  Gets the Single Instance
     *  DISCUSSION POINT: This instance is single for a JVM, what about high load environments where more than one JVM may be involved
     * @return el Singleton ;-)
     */
     public static ThingSaverFacade getSingleton() {
         return singleton;
     }

    /*
     *** Business Methods
     */

    /**
     * Holds non-blank things
     * Fails with an IllegalArgumentException when StringUtils.isBlank(thing) == true
     * @param thing a non blank string
     */
    public synchronized void hold(String thing) {
        //Cowardly refusing to hold blank strings
        // DISCUSSION POINTS:   handling of blanks through RuntimeException yet at this level the app will not send a blank as validation also in the RESTful Server
        //                      Should we use an explicit Exception, probably yes but over time practice seems to prefer RuntimeExceptions here
        //                      Silently Ignore? Other options
        if(StringUtils.isBlank(thing)) throw new IllegalArgumentException(String.format("blank thing=%s unsupported",thing));
        storage.add(thing);
        log.debug("thing={} stored, storage has size={}",thing,storage.size());
     }

    /**
     * Remove all instances of thing from storage
     * @see #equalThings(String, String)  for our definition of equality
     * @param thing String to remove from storage, all instances will be removed if equalThings == true
     * @return count of removed things, possibly 0
     */
    public synchronized int remove(String thing) {
        Iterator<String> i = storage.listIterator();
        int count=0;
        while(i.hasNext()) {
            String storedThing = i.next();
            if(equalThings(thing, storedThing)) {
                i.remove();
                count++;
            }//if
        }//while
        return count;
     }

    /**
     * Obtains List of all Things in entry order
     * DISCUSSION POINT: Typically Sorted Order is better but there may be business cases were given order is needed
     * @return list of all Things in entry order
     */
     public synchronized List<String> getAllThings() {
        return List.copyOf(storage);
     }

    /**
     * Obtains a sorted unique Set of Things stored by ThingServer
     * DISCUSSION POINT: Preserving given order is not impossible but a lot more difficult, if a business requirement we would fatten the code to do it
     * @return sorted unique Set of Things stored by ThingServer
     */
    public synchronized Set<String> getAllThingsUnique() {
         Set<String> retVal = new TreeSet<>(this::compareToThings);
         retVal.addAll(storage);
         return retVal;
    }

    /**
     * Return all Things in our Storage that match thing
     * @param thing
     * @return
     */
    public synchronized List<String> getThing(String thing) {
        return storage.stream().filter(storedThing -> equalThings(storedThing, thing)).toList();
    }

    /**
     * Our equality test for the ThingServer
     * DISCUSSION POINTS:
     *   The tests considers blank Strings (null, " ", "") and lumps them together.
     *   Blank Strings and trailing spaces and the consideration of uppercase versus lowercase
     *   many times creates ugly errors in applications so should always be remembered
     *   Yet, in our case the thingServer should not hold blank things, it fails when storing
     *   Should still our equalityMethod then test for them or not?
     * @param thing1 First String to compare
     * @param thing2 Second String to compare
     * @return true if both StringUtils.isBlank() or thing1.equalsIgnoreCase(thing2) == true, false otherwise
     */
     private boolean equalThings(String thing1, String thing2) {
         if(StringUtils.isBlank(thing1) && StringUtils.isBlank(thing2)) return true;
         if(StringUtils.isBlank(thing1)) return false;
         return thing1.equalsIgnoreCase(thing2);
     }

    /**
     * Our Comparison test for the ThingServer
     * @see #equalThings(String, String) for DISCUSSION POINTS
     * @param thing1
     * @param thing2
     * @return
     */
    private int compareToThings(String thing1, String thing2) {
         if(StringUtils.isBlank(thing1) && StringUtils.isBlank(thing2)) return 0;
         if(StringUtils.isBlank(thing1)) return -1;
         return thing1.compareToIgnoreCase(thing2);
     }

}