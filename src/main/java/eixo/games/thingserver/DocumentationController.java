package eixo.games.thingserver;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *  A quick and hastily done place to Document our ThingServer...
 *  Pinch your nose please...
 *  Done in the spirit of:
 *  As Kidder Peabody details in The Soul of a New Machine to the effect that not everything needs to be perfectly done 
 *  (so that something gets done)....
 *  But you didn't hear it from me.
 *  TODO    Refactor this controller to follow more currently established html rendering design patterns
 *          such as decoupled from code templates, parameter substitution, smaller sized units of code et. al..
 */
@Controller
@Slf4j
public class DocumentationController {

    @GetMapping(path="/")
    @ResponseBody
    public String getSplashPage(HttpServletRequest request) {
        String url = request.getRequestURL().toString();
        if(!url.endsWith("/")) url += "/";
        String html = getSplashPageHtml();
        return html.replace("$url$",url);
    }
    private String getSplashPageHtml() {
        return """
                               <!DOCTYPE html>
                               <html lang="en">
                                 <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-GLhlTQ8iRABdZLl6O3oVMWSktQOp6b7In1Zl3/Jr59b6EGGoI1aFkw7cmDA6j6gD" crossorigin="anonymous">
                                 <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js" integrity="sha384-w76AqPfDkMBDXo30jS1Sgez6pr3x5MlQ1ZAGC+nuZB+EYdgRZgiwxhTBTkF7CXvN" crossorigin="anonymous"></script>
                                <body>
                                 <div class="accordion" id="ThingServerDocumentation">
                <!-- 1. Introduction -->
                                       <div class="accordion-item">
                                         <h2 class="accordion-header" id="headingOne">
                                           <button class="accordion-button" type="button" data-bs-toggle="collapse" data-bs-target="#collapseOne" aria-expanded="true" aria-controls="collapseOne">
                                             Welcome to ThingServer
                                           </button>
                                         </h2>
                                         <div id="collapseOne" class="accordion-collapse collapse show" aria-labelledby="headingOne" data-bs-parent="#ThingServerDocumentation">
                                           <div class="accordion-body">
                                             Example RESTful service to be used as a teaching tool to allow a ramp up from the novice to more experienced developers.
                                             The idea is to have a lot of depth in the code but let the student find his level...
                                             With some students you stay more on basic concepts, with others dive deeper into details and nuances.
                                           </div>
                                         </div>
                                       </div>
                <!-- 2. Usage Tips -->
                                       <div class="accordion-item">
                                         <h2 class="accordion-header" id="headingTwo">
                                           <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#collapseTwo" aria-expanded="false" aria-controls="collapseTwo">
                                             Usage Tips
                                           </button>
                                         </h2>
                                         <div id="collapseTwo" class="accordion-collapse collapse" aria-labelledby="headingTwo" data-bs-parent="#ThingServerDocumentation">
                                           <div class="accordion-body">
                                           As RESTful endpoint is primarily intended as a machine/machine interaction for some endpoints you will need a tool to change the http parameters beyond what usually is done by the browser.<p>
                                           One such tool is curl. <br>
                                           <code>
                                            curl -X PUT $url$api/hold?thing=yourThing
                                           </code>
                                           </div>
                                         </div>
                                       </div>
                <!-- 3. Hold a Thing  -->
                                       <div class="accordion-item">
                                         <h2 class="accordion-header" id="headingThree">
                                           <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#collapseThree" aria-expanded="false" aria-controls="collapseThree">
                                             Hold a Thing
                                           </button>
                                         </h2>
                                         <div id="collapseThree" class="accordion-collapse collapse" aria-labelledby="headingThree" data-bs-parent="#ThingServerDocumentation">
                                           <div class="accordion-body">
                                             <code> curl -X PUT $url$api/hold?thing=yourThing</code><br>
                                             try with:<br>
                                             <ul>
                                             <li> no thing parameter or empty thing parameter -> should error </li>
                                             <li> try various upper case, lower case, spaces, special characters -> what happens, what do other endpoints show </li>
                                             </ul>
                                           </div>
                                         </div>
                                       </div>
                <!-- 4. Remove a Thing -->
                                       <div class="accordion-item">
                                         <h2 class="accordion-header" id="headingFour">
                                           <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#collapseFour" aria-expanded="false" aria-controls="collapseFour">
                                             Remove a Thing
                                           </button>
                                         </h2>
                                         <div id="collapseFour" class="accordion-collapse collapse" aria-labelledby="headingFour" data-bs-parent="#ThingServerDocumentation">
                                           <div class="accordion-body">
                                             <code> curl -X DELETE $url$api/remove?thing=yourThing</code>
                                             try with:<br>
                                             <ul>
                                             <li> no thing parameter or empty thing parameter -> should error </li>
                                             <li> try various upper case, lower case, spaces, special characters -> what happens, what do other endpoints show </li>
                                             </ul>
                                           </div>
                                         </div>
                                       </div>
                <!-- 5. Get All Things -->
                                       <div class="accordion-item">
                                         <h2 class="accordion-header" id="headingFive">
                                           <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#collapseFive" aria-expanded="false" aria-controls="collapseFive">
                                             Get All Things
                                           </button>
                                         </h2>
                                         <div id="collapseFive" class="accordion-collapse collapse" aria-labelledby="headingFive" data-bs-parent="#ThingServerDocumentation">
                                           <div class="accordion-body">
                                             <code><a target="_blank" href="$url$api/all">$url$api/all</a></code>
                                           </div>
                                         </div>
                                       </div>
                <!-- 6. Get Unique Things -->
                                       <div class="accordion-item">
                                         <h2 class="accordion-header" id="headingSix">
                                           <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#collapseSix" aria-expanded="false" aria-controls="collapseSix">
                                             Get Unique Things
                                           </button>
                                         </h2>
                                         <div id="collapseSix" class="accordion-collapse collapse" aria-labelledby="headingSix" data-bs-parent="#ThingServerDocumentation">
                                           <div class="accordion-body">
                                             <code><a target="_blank" href="$url$api/all/unique">$url$api/all</a></code>
                                           </div>
                                         </div>
                                       </div>
                <!-- 7. Get A Thing -->
                                       <div class="accordion-item">
                                         <h2 class="accordion-header" id="headingSeven">
                                           <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#collapseSeven" aria-expanded="false" aria-controls="collapseSeven">
                                             Get a Thing
                                           </button>
                                         </h2>
                                         <div id="collapseSeven" class="accordion-collapse collapse" aria-labelledby="headingSeven" data-bs-parent="#ThingServerDocumentation">
                                           <div class="accordion-body">
                                             <form target="_blank" method="GET" action="$url$api/get">
                                              Get this Thing:<input type="text" name="thing">
                                              <input type="submit">
                                              </form>
                                             </div>
                                         </div>
                                       </div>
                <!-- 8. How Many Things -->
                                       <div class="accordion-item">
                                         <h2 class="accordion-header" id="headingEight">
                                           <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#collapseEight" aria-expanded="false" aria-controls="collapseEight">
                                             How many Things?
                                           </button>
                                         </h2>
                                         <div id="collapseEight" class="accordion-collapse collapse" aria-labelledby="headingEight" data-bs-parent="#ThingServerDocumentation">
                                           <div class="accordion-body">
                                             <code><a target="_blank" href="$url$api/all/count">$url$api/all/count</a></code>
                                           </div>
                                         </div>
                                       </div>
                <!-- 9. How Many Unique Things -->
                                       <div class="accordion-item">
                                         <h2 class="accordion-header" id="headingNine">
                                           <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#collapseNine" aria-expanded="false" aria-controls="collapseNine">
                                             How many Unique Things?
                                           </button>
                                         </h2>
                                         <div id="collapseNine" class="accordion-collapse collapse" aria-labelledby="headingNine" data-bs-parent="#ThingServerDocumentation">
                                           <div class="accordion-body">
                                             <code><a target="_blank" href="$url$api/all/unique/count">$url$api/all/unique/count</a></code>
                                           </div>
                                         </div>
                                       </div>
                                     </div>
                                </body>
                               </html>
                                   """;
    }

}
