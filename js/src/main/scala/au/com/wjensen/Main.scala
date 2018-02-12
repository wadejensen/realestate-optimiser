package au.com.wjensen

import io.scalajs.nodejs.http.{Http, Server, ServerResponse}

import scala.scalajs.js.JSApp
import io.scalajs.nodejs.console
import io.scalajs.npm.express._

import scala.scalajs.js

object Main extends JSApp {

  def main(): Unit = {

    val app = Express()
    app.get("/", (req: Request, res: Response) => res.send("Hello World"))

    def connect: js.Function = () => {
      console.log("Example app listening at http://%s:%s", "localhost", 8081)
    }

    val server: Server = app.listen(8081, connect)
  }
}
