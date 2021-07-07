package controllers

import models.User

import javax.inject.Inject
import play.api.mvc.{AbstractController,  AnyContent, ControllerComponents, Request}
import play.api.libs.json._
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}
import reactivemongo.api.bson.collection.BSONCollection
import reactivemongo.api.bson.{ BSONObjectID}
import repo.UserRepo

import scala.concurrent.Future
import scala.util.{Failure, Success}


class UserController @Inject() (cc:ControllerComponents,val reactiveMongoApi: ReactiveMongoApi,val userRepo: UserRepo)
extends AbstractController(cc) with MongoController with ReactiveMongoComponents {
  implicit def executionContext = cc.executionContext
  def collection: Future[BSONCollection] = reactiveMongoApi.database.map(db => db.collection("users"))

  def get(_id:String) = Action.async { implicit request =>
    BSONObjectID.parse(_id) match {
      case Success(objId) => userRepo.findOne(objId).map(_.map(user=>Ok(Json.toJson(user))).getOrElse(NotFound("no such user")))
      case Failure(_) => Future.successful(BadRequest("invalid user id"))
    }
  }
  def getAll()  = Action.async { implicit request =>
    userRepo.findAll().map( users => Ok(Json.toJson(users)))
  }
  def create() = Action.async(parse.json) { implicit request =>
    request.body.validate[User].map(user =>  userRepo.create(user).map( _ => Created("created"))).getOrElse(Future.successful(BadRequest("invalid data")))
  }
  def update(_id:String) = Action.async(parse.json) { implicit request =>
    request.body.validate[User].map(user => {
      BSONObjectID.parse(_id) match {
        case Success(objId) => userRepo.update(objId,user).map(_ => Ok("updated"))
        case Failure(_) => Future.successful(BadRequest("invalid user id"))
      }
    }).getOrElse(Future.successful(BadRequest("invalid data")))
  }
  def delete(_id:String) =  Action.async { implicit request =>
    BSONObjectID.parse(_id) match {
      case Success(objId) => userRepo.delete(objId).map(_ => Ok("deleted"))
      case Failure(_) => Future.successful(BadRequest("invalid user id"))
    }
  }

}
