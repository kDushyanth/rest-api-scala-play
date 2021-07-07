package repo

import models.User

import javax.inject._
import reactivemongo.api.bson.collection.BSONCollection
import play.modules.reactivemongo.ReactiveMongoApi
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserRepo @Inject()(
                                 implicit executionContext: ExecutionContext,
                                 reactiveMongoApi: ReactiveMongoApi
                               ) {
  def collection: Future[BSONCollection] = reactiveMongoApi.database.map(db => db.collection("users"))

  import reactivemongo.api.{Cursor, ReadPreference}
  import reactivemongo.api.bson.{BSONDocument, BSONObjectID}

  def findAll(limit: Int = 100): Future[Seq[User]] = {

    collection.flatMap(_.find(BSONDocument(), Option.empty[User])
      .cursor[User](ReadPreference.Primary)
      .collect[Seq](limit, Cursor.FailOnError[Seq[User]]())
    )
  }
  def findOne(id: BSONObjectID): Future[Option[User]] = {
    collection.flatMap(_.find(BSONDocument("_id" -> id), Option.empty[User]).one[User])
  }
  import reactivemongo.api.commands.WriteResult

  def create(user: User) = {
    collection.flatMap(_.insert.one(user))
  }
  def update(id: BSONObjectID, user: User):Future[WriteResult] = {
    collection.flatMap( _.update.one(BSONDocument("_id" -> id),user) )
  }

  def delete(id: BSONObjectID):Future[WriteResult] = {
    collection.flatMap( _.delete().one(BSONDocument("_id" -> id), Some(1)))
  }
}