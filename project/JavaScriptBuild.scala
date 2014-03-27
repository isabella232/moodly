import java.net.InetSocketAddress
import sbt._
import sbt.Keys._
import play.PlayRunHook
import play.Project._
import com.typesafe.sbt.packager.Keys._
import scala.languageFeature.postfixOps

object JavaScriptBuild {
  val uiDirectory = SettingKey[File]("ui-directory")

  val npmInstall = TaskKey[File]("npm-install")

  val gulpBuild = TaskKey[Unit]("gulp-build")

  val javaScriptUiSettings = Seq(

    // the JavaScript application resides in "ui"
    uiDirectory <<= (baseDirectory in Compile) {
      _ / "ui"
    },

    npmInstall <<= (uiDirectory, streams) map {
      (base, s) =>
        s.log.info("Run 'npm install'")
        val exitCode = npmProcess(base, "install").run().exitValue()

        if (exitCode != 0) {
          sys.error(s"npm install failed with exitCode=$exitCode}")
        }
        base
    },

    gulpBuild <<= (npmInstall, streams) map {
      (base, s) =>
        s.log.info("Run 'gulp'")
        val exitCode = gulpProcess(base).run().exitValue()

        if (exitCode != 0) {
          sys.error(s"gulp build failed with exitCode=$exitCode}")
        }
    },
    playRunHooks <+= npmInstall.map(ui => gulpRunHook(ui)),

//    test in Test <<= (test in Test).dependsOn(gulpBuild),

//    stage <<= stage.dependsOn(gulpBuild),

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