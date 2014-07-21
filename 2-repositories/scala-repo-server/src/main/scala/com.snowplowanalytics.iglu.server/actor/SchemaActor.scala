/*
* Copyright (c) 2014 Snowplow Analytics Ltd. All rights reserved.
*
* This program is licensed to you under the Apache License Version 2.0,
* and you may not use this file except in compliance with the
* Apache License Version 2.0.
* You may obtain a copy of the Apache License Version 2.0 at
* http://www.apache.org/licenses/LICENSE-2.0.
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the Apache License Version 2.0 is distributed on
* an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
* express or implied.  See the Apache License Version 2.0 for the specific
* language governing permissions and limitations there under.
*/
package com.snowplowanalytics.iglu.server
package actor

// This project
import model.SchemaDAO
import util.PostgresDB

// Akka
import akka.actor.Actor

object SchemaActor {
  case class GetSchema(vendor: String, name: String, format: String,
    version: String)
  case class GetSchemasFromVendor(vendor: String)
  case class GetSchemasFromName(vendor: String, name: String)
  case class GetSchemasFromFormat(vendor: String, name: String, format: String)
  case class AddSchema(vendor: String, name: String, format: String,
    version: String, schema: String)
}

class SchemaActor extends Actor {
  import SchemaActor._

  private val schema = new SchemaDAO(PostgresDB.db)

  def receive = {
    case GetSchema(v, n, f, vs) => sender ! schema.get(v, n, f, vs)
    case GetSchemasFromVendor(v) => sender ! schema.getFromVendor(v)
    case GetSchemasFromName(v, n) => sender ! schema.getFromName(v, n)
    case GetSchemasFromFormat(v, n, f) =>
      sender ! schema.getFromFormat(v, n, f)
    case AddSchema(v, n, f, vs, s) => sender ! schema.add(v, n, f, vs, s)
  }
}
