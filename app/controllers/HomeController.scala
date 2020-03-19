package controllers

import javax.inject._
import play.api.libs.json.JsValue
import play.api.mvc._

@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def index = Action { implicit request: Request[AnyContent] =>
    Ok("Welcome!").withSession(request.session +  ("connected" -> "user@gmail.com"))
    Ok(views.html.index("Hello World."))
  }

  def help() = Action {
    Redirect("http://google.com")
  }

  def infinite() = Action {
    Redirect("/")
  }

  def unimplemented = TODO

  def asHtml() = Action {
    Ok(<h1>Hello World!</h1>).as(HTML)
  }

  def asTextHtml() = Action {
    Ok(<h1>Hello World!</h1>).as("text/html")
  }

  def differentCharset() = Action {implicit request: Request[AnyContent] =>
    implicit val myCustomCharset: Codec = Codec.javaSupported("iso-8859-1")
    Ok("Hello World")
  }

  def useCookie() = Action {
    Ok("Hello World!").withCookies(
      Cookie("colour", "blue")
    )
  }

  def returnCookie() = Action { implicit request: Request[AnyContent] =>
    request.cookies.get("colour") match {
      case Some(cookie) => Ok(s"Your cookie value is: ${cookie.value}")
      case _ => Ok("Cookie not found")
    }
  }

  def deleteCookie() = Action {
    Ok("Cookie deleted")
      .discardingCookies(
        DiscardingCookie("colour")
      )
  }

  def returnSessionValue() = Action { implicit request: Request[AnyContent] =>
    Ok(request.session.get("connected").getOrElse("User is not logged in"))
  }

  def returnAndDeleteSessionValue = Action { implicit request: Request[AnyContent] =>
    Ok(request.session.get("connected").getOrElse("User is not logged in"))
    Ok("Welcome!").withSession(request.session - "connected")
  }

  def write = Action { implicit request: Request[AnyContent] =>
      Redirect("/read").flashing("success" -> "You have been successfully redirected")

    }

  def read = Action { implicit request: Request[AnyContent] =>
    Ok(request.flash.get("success").getOrElse("Something went wrong while redirecting"))
  }

  def bodyParser = Action { request: Request[AnyContent] =>
    request.body.asJson.map { json =>
      Ok("Got: \nname: " + (json \ "name").as[String] + ", age: " + (json \ "age").as[String])
    }.getOrElse {
      BadRequest("Please provide your name and age")
    }
  }

  def specifiedBodyparser = Action(parse.json) { request: Request[JsValue] =>
    Ok("Got: \nname: " + (request.body \ "name").as[String] + ", age: " + (request.body \ "age").as[String])
  }
}
