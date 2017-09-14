package it.caldesi.scalaurlshortener.domain

import scala.slick.driver.MySQLDriver.simple._
import scala.slick.lifted.ColumnOption.NotNull

case class Url(id: Option[Long], url: String, shortCode: String)

object Urls extends Table[Url]("urls") {

  // Columns
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def url = column[String]("url", NotNull)
  def shortCode = column[String]("shortCode", NotNull)

  // unique constraints
  def idxUrl = index("idx_url", url, unique = true)
  def idxShortCode = index("idx_shortcode", shortCode, unique = true)

  // select
  def * = id.? ~ url ~ shortCode <> (Url, Url.unapply _)

  // find
  val findById = for {
    id <- Parameters[Long]
    c <- this if c.id is id
  } yield c

  val findByUrl = for {
    url <- Parameters[String]
    c <- this if c.url is url
  } yield c

  val findByShortCode = for {
    shortCode <- Parameters[String]
    c <- this if c.shortCode is shortCode
  } yield c

}