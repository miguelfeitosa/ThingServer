# ThingServer
Example RESTful service to be used as a teaching tool to allow a ramp up from the novice to more experienced developers.
The idea is to have a lot of depth in the code but let the student find his level...
With some students you stay more on basic concepts, with others dive deeper into details and nuances.

The example code introduces on a slow glide ramp concepts such as:
- Machine to Machine interaction 
  - Data Exchange, Segregation
  - Specialization of Concerns
  - Readability by Humans
- Human to Machine interaction
- HTTP protocol
  - suitable wire mechanism for machine to machine interaction
  - stateless, how to add state
- JSON notation
  - human readable yet easily machine consumed
- Multi-threading
  - who is calling?
- Concurrency
  - everyone at the same time? get in line!
- Payload Design
  - Happy and unhappy paths, failing gracefully
- Expected/Unexpected error states on the "wire"

Topics still to cover or improve:
- Authentication
  - Who is who in the zoo?
- Testing
  - Manual, Automation, Coverage, suitability 
- Logging
  - leaving the operator with enough information to allow correct operation and issue investigation
  - Auditing and Compliance

### To run:
Install a current java jvm and from the command line:
$./gradlew bootRun

From a browser:
http://localhost:8080/
An html page with instructions to exercise the ThingServer end points