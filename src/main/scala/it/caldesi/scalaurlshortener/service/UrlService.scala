package it.caldesi.scalaurlshortener.service

import scala.util.Random

import it.caldesi.scalaurlshortener.dao.UrlDAO
import it.caldesi.scalaurlshortener.domain.Url

class UrlService {

  val urlDao = new UrlDAO

  def getShortCode(urlString: String): Url = {
    def url = urlDao.getByUrl(urlString)
    if (url != null) return url

    // TODO check for possible collisions, ignore it for the moment
    // to make the service scalable is useful to make the random string
    // belong to a fixed interval, for example:
    //      Server 1: 0???????
    //      Server 2: 1???????
    //      etc. etc.
    val shortCode = randStr(8)
    // TODO check if in the blacklist, otherwise re-generate
    urlDao.create(new Url(None, urlString, shortCode))
  }

  def getUrl(shortCode: String): String = {
    urlDao.getByShortCode(shortCode).url
  }

  val alpha = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
  val size = alpha.size

  def randStr(n:Int) = (1 to n).map(x => alpha(Random.nextInt.abs % size)).mkString

}
