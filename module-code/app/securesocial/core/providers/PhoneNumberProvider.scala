/**
 * Copyright 2012 Jorge Aliss (jaliss at gmail dot com) - twitter: @jaliss
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package securesocial.core.providers

import play.api.data.Form
import play.api.data.Forms._
import securesocial.core._
import play.api.mvc.{PlainResult, Results, Result, Request}
import utils.{GravatarHelper, PasswordHasher}
import play.api.{Play, Application}
import Play.current
import com.typesafe.plugin._
import securesocial.controllers.TemplatesPlugin
import org.joda.time.DateTime

/**
 * A phone number provider
 */
class PhoneNumberProvider(application: Application) extends IdentityProvider(application) {

  override def id = PhoneNumberProvider.PhoneNumber

  def authMethod = AuthenticationMethod.PhoneNumber

  val InvalidCredentials = "securesocial.login.invalidCredentials"

  def doAuth[A]()(implicit request: Request[A]): Either[Result, SocialUser] = {
    val form = PhoneNumberProvider.loginForm.bindFromRequest()
    form.fold(
      errors => Left(badRequest(errors, request)),
      credentials => {
        val userId = UserId(credentials._1, id)
        val result = for (
          user <- UserService.find(userId) ;
          pinfo <- user.passwordInfo ;
          //hasher <- Registry.hashers.get(pinfo.hasher) if pinfo.password == credentials._2
          hasher <- Registry.hashers.get(pinfo.hasher) if hasher.matches(pinfo, credentials._2)
        ) yield (
          Right(SocialUser(user))
        )
        result.getOrElse(
          Left(badRequest(PhoneNumberProvider.loginForm, request, Some(InvalidCredentials)))
        )
      }
    )
  }

  private def badRequest[A](f: Form[(String,String)], request: Request[A], msg: Option[String] = None): PlainResult = {
    Results.BadRequest(use[TemplatesPlugin].getLoginPage(request, f, msg))
  }

  def fillProfile(user: SocialUser) = {
    // Do nothing
    user
  }
}

object PhoneNumberProvider {
  val PhoneNumber = "phonenumber"

  val loginForm = Form(
    tuple(
      "number" -> nonEmptyText,
      "code" -> nonEmptyText
    )
  )
}
