package it.caldesi.scalaurlshortener.rest

import akka.actor.Actor
import akka.event.slf4j.SLF4JLogging
import it.caldesi.scalaurlshortener.domain._

import it.caldesi.scalaurlshortener.service.UrlService
import net.liftweb.json.Serialization._

import spray.http._
import spray.routing._

/**
 * REST Service actor.
 */
class RestServiceActor extends Actor with RestService {

  implicit def actorRefFactory = context

  def receive = runRoute(rest)
}

/**
 * REST Service
 */
trait RestService extends HttpService with SLF4JLogging {

  val urlService = new UrlService

  implicit val executionContext = actorRefFactory.dispatcher

  implicit val formats = net.liftweb.json.DefaultFormats

  val rest = respondWithMediaType(MediaTypes.`application/json`) {
    path("shorten") {
      post {
        formFields('url) {
          url: String =>
            ctx: RequestContext =>
              handleRequest(ctx, StatusCodes.Created) {
                log.debug("Requested short url for: %s".format(url))
                urlService.getShortCode(url)
              }
        }
      }
    } ~
      path(Segment) {
        shortCode =>
          get {
            log.debug("Retrieving url with shortCode %s".format(shortCode))
            val redirectUrl:String = urlService.getUrl(shortCode)
            // TODO handle when null
            redirect(redirectUrl, StatusCodes.PermanentRedirect)
          }
      }
  }

  protected def handleRequest(ctx: RequestContext, successCode: StatusCode = StatusCodes.OK)(result: => Url) {
    ctx.complete(successCode, write(result))
  }

}