package net.grydeske.network.course

import groovy.util.logging.Slf4j
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Post
import io.micronaut.http.server.types.files.StreamedFile
import io.micronaut.http.simple.SimpleHttpHeaders
import io.micronaut.views.View

@Slf4j
@Controller("/")
class WiresharkController {

    @Get("/")
    HttpStatus index() {
        return HttpStatus.OK
    }

    @View("intro")
    @Get("/intro")
    HttpStatus intro() {
        return HttpStatus.OK
    }

    @View("file1")
    @Get("/file1.html")
    HttpStatus file1() {
        return HttpStatus.OK
    }

    @View("file2")
    @Get("/file2.html")
    HttpResponse<String> file2(final HttpHeaders headers) {
        String lastModified = 'Sun, 28 Jul 2019 05:59:01 GMT'
        headers.asMap().each { name, value ->
            log.info("$name -> $value")
        }

        if( headers.get(SimpleHttpHeaders.IF_MODIFIED_SINCE) == lastModified) {
            log.info("Returning: Not modified")
            return HttpResponse.notModified().header(SimpleHttpHeaders.CONNECTION, "close")
        } else {
            log.info("Returning: Ok")
            return HttpResponse.ok()
                    .header(SimpleHttpHeaders.LAST_MODIFIED ,lastModified)
                    .header(SimpleHttpHeaders.ETAG ,'173-58eb778e48340')
        }
    }

    @View("file3")
    @Get("/file3.html")
    HttpStatus file3() {
        return HttpStatus.OK
    }

    @View("file4")
    @Get("/file4.html")
    HttpStatus file4() {
        return HttpStatus.OK
    }

    @View("file5")
    @Get("/file5.html")
    HttpResponse file5(final HttpHeaders headers) {
        log.info("HEADER: '${headers.get(SimpleHttpHeaders.AUTHORIZATION)}'")
        // Username: dm557 - password: network
        if( headers.get(SimpleHttpHeaders.AUTHORIZATION) != 'Basic ZG01NTc6bmV0d29yaw==') {
            log.info("Returning: unauthorized")
            return HttpResponse.unauthorized().header(SimpleHttpHeaders.WWW_AUTHENTICATE, 'Basic realm="Network and Security"')
        } else {
            log.info("Returning: Ok")
            return HttpResponse.ok()
        }
    }

    @View("dns")
    @Get("/dns")
    HttpResponse dns() {
        log.info("Dns endpoint")
        HttpResponse.ok()
    }

    @Get("/alice.txt")
    StreamedFile alice() {
        log.info("Serving alice.txt")
        InputStream inputStream = WiresharkController.getClassLoader().getResourceAsStream('alice.txt')
        return new StreamedFile(inputStream, MediaType.TEXT_PLAIN_TYPE)
    }

    @View("tcp-upload")
    @Get("/tcp-upload")
    HttpResponse tcpUpload() {
        log.info("TCP upload")
        HttpResponse.ok()
    }

    @View("tcp-thanks")
    @Post(value="/tcp-receive", consumes = MediaType.MULTIPART_FORM_DATA)
    HttpResponse tcpReceive() {
        log.info("TCP receive")
        HttpResponse.ok()
    }

    @Get("/image/{imageName}")
    StreamedFile image(String imageName) {
        log.info("Serving image: $imageName")
        switch(imageName) {
            case 'SDU.png':
                InputStream inputStream = WiresharkController.getClassLoader().getResourceAsStream('SDU.png')
                return new StreamedFile(inputStream, MediaType.IMAGE_PNG_TYPE)
            case 'dave.jpeg':
                InputStream inputStream = WiresharkController.getClassLoader().getResourceAsStream('dave.jpeg')
                return new StreamedFile(inputStream, MediaType.IMAGE_JPEG_TYPE)
            default:
                return null
        }
    }

}
