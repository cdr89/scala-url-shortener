package it.caldesi.scalaurlshortener.dao

import java.sql._

import it.caldesi.scalaurlshortener.config.Configuration
import it.caldesi.scalaurlshortener.domain._

import scala.concurrent.Future
import scala.slick.driver.MySQLDriver.simple.Database.threadLocalSession
import scala.slick.driver.MySQLDriver.simple._
import scala.slick.jdbc.meta.MTable

class UrlDAO extends Configuration {

  // init Database instance
  private val db = Database.forURL(url = "jdbc:mysql://%s:%d/%s".format(dbHost, dbPort, dbName),
    user = dbUser, password = dbPassword, driver = "com.mysql.jdbc.Driver")

  // create tables if not exist
  db.withSession {
    if (MTable.getTables("urls").list().isEmpty) {
      Urls.ddl.create
    }
  }

  def create(url: Url): Url = {
    try {
      val id = db.withSession {
        Urls returning Urls.id insert url
      }
      url.copy(id = Some(id))
    } catch {
      case e: SQLException =>
        null
    }
  }

  def get(id: Long): Url = {
    try {
      db.withSession {
        Urls.findById(id).firstOption match {
          case Some(url: Url) =>
            url
          case _ =>
            null
        }
      }
    } catch {
      case e: SQLException =>
        null
    }
  }

  def getByShortCode(shortCode: String): Url = {
    try {
      db.withSession {
        Urls.findByShortCode(shortCode).firstOption match {
          case Some(url: Url) =>
            url
          case _ =>
            null
        }
      }
    } catch {
      case e: SQLException =>
        null
    }
  }

  def getByUrl(url: String): Url = {
    try {
      db.withSession {
        Urls.findByUrl(url).firstOption match {
          case Some(url: Url) =>
            url
          case _ =>
            null
        }
      }
    } catch {
      case e: SQLException =>
        null
    }
  }

}