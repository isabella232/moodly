import java.net.InetSocketAddress
import sbt._
import sbt.Keys._
import play.PlayRunHook
import play.Project._

object JavaScriptBuild {
  val uiDirectory = SettingKey[File]("ui-directory")

  val npmInstall = TaskKey[Unit]("npm-install")

  val javaScriptUiSettings = Seq(

    // the JavaScript application resides in "ui"
    uiDirectory <<= (baseDirectory in Compile) {
      _ / "ui"
    },

    npmInstall <<= (uiDirectory) map {
      (base) =>
        val exitCode = npmProcess(base, "install").run().exitValue()

        if (exitCode != 0) {
          sys.error(s"npm install failed with exitCode=$exitCode}")
        }
    },

//    playRunHooks <+= uiDirectory.map(ui => gulpRunHook(ui)),

    // add "npm" commands in sbt
    commands <++= uiDirectory {
      base => Seq(npmCommand(base))
    }
  )

  def gulpProcess(base: File, args: String*) = Process("node" :: "node_modules/.bin/gulp" :: args.toList, base)

  def npmProcess(base: File, args: String*) = Process("npm" :: args.toList, base)

  def npmCommand(base: File) = Command.args("npm", "<npm-command>") {
    (state, args) =>
      npmProcess(base, args: _ *) !;
      state
  }

  def gulpRunHook(base: File): PlayRunHook = new PlayRunHook {

      var process: Option[Process] = None

      override def afterStarted(addr: InetSocketAddress): Unit = {
        // call grunt to generate public assets
        gulpProcess(base).run()
        // watch for modifications
        process = Some(gulpProcess(base, "watch").run())
      }

      override def afterStopped(): Unit = {
        // Stop grunt when play run stops
        process.map(p => p.destroy())
        process = None
      }

    }

}