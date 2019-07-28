package net.grydeske.network.course

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.HttpStatus
import io.micronaut.views.View


@Controller("/")
class WiresharkController {

    @View("intro")
    @Get("/intro")
    HttpStatus index() {
        return HttpStatus.OK
    }
}