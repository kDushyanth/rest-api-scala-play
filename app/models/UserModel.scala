package models



import play.api.libs.json.{Json, OFormat}
import reactivemongo.bson.BSONObjectID
import reactivemongo.bson._




case class User(_id:Option[BSONObjectID],name:String, email:String, age:Int);

object User {

  implicit object UserBSONReader extends BSONDocumentReader[User] {
    def read(doc: BSONDocument): User = {
      User(
        doc.getAs[BSONObjectID]("_id"),
        doc.getAs[String]("name").get,
        doc.getAs[String]("email").get,
        doc.getAs[Int]("age").get
      )
    }
  }

  implicit object UserBSONWriter extends BSONDocumentWriter[User] {
    def write(user: User): BSONDocument = {
      BSONDocument(
        "_id" -> user._id,
        "name" -> user.name,
        "email" -> user.email,
        "age" -> user.age,
      )
    }
  }
  import reactivemongo.play.json.BSONObjectIDFormat
  implicit val userFormatter:OFormat[User] = Json.format[User]
}
