package model

import com.dimafeng.testcontainers.{DockerComposeContainer, ExposedService, ForAllTestContainer}
import org.scalatest.BeforeAndAfterAll
import org.scalatest.OptionValues._
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import software.amazon.awssdk.regions.Region

import java.io.File
import java.net.URI


class MessagesSpec
    extends AnyWordSpecLike
    with Matchers
    with BeforeAndAfterAll
    with ForAllTestContainer {
  override val container: DockerComposeContainer =
    new DockerComposeContainer(
      composeFiles =
        new File(getClass.getResource("/docker-compose.yml").getFile),
      Seq(
        ExposedService("localstack", 4566)
      )
    )

  override def beforeAll(): Unit = {
    super.beforeAll()

    MessagesOnDymamodb.createTable(messages)
  }

  lazy val messages: MessagesOnDymamodb = {
    val (localstackContainerHost, localstackContainerHostPort) =
      (
        container.getServiceHost("localstack", 4566),
        container.getServicePort("localstack", 4566)
      )

    MessagesOnDymamodb(
      new URI(s"http://${localstackContainerHost}:${localstackContainerHostPort}"),
      Region.of("ap-northeast-1")
    )
  }

  "messages" should {
    "追加済のメッセージを取得できる" in {
      val message = Message("hello")
      messages.add(message)
      val messageOpt = messages.get(message.id)
      messageOpt.value shouldBe message
    }
  }
}

/**
docker-compose.ymlとともにtest-containersを使ってみた

test-containersはxxx, yyyができます。

test-containersはJavaのライブラリですが、Scala用のFacadeライブラリも存在します。
ここではScalaから利用していきます。

今回以下のようなコードのテストをtest-containersを使って実装していきたいと思います。

--- コード

ここからtest-containersを利用した実装についてです。

まずはSpecファイルを実装していくうえで1番基本的なところから紹介してます。


まずはSpec classを実装します。
scalatest上のどのSpec traitを使うかは自由です。
注目すべき点は`ForAllTestContainer`をmixinすることです。

testcontainersを利用するSpec class は `ForAllTestContainer`または`ForEachTestContainer` をmixinして実装します。
このtraitではoverrideしたフィールドで指定したcontainerの起動と終了を、Specの実行に合わせてよしなにしてくれます。
`ForEachTestContainer`ではテストケースごとにコンテナを起動と停止を行います。
`ForAllTestContainer`ではテスト開始時にコンテナを起動して、全てのテストケースの終了時に停止させます。




コンテナを起動する上では以下のような選択肢があります。

docker imageを指定する

作成済のdockerfileを指定する

docker-composeを指定する

まずは1番シンプルなdocker imageの指定をする方法で実装してきます。

次に作成済みのdockerfileからで実装していきます

さらに次にmodulesを利用した実装をします

最後にdocker-compose.ymlを利用した方法を紹介していきます。

```
new DockerComposeContainer()
のようにfileとテスト中に接続するサービスの公開を指定して初期化します。
```

*/
