package com.example.client

import com.example.service.Request
import com.example.service.ServiceGrpcWeb
import scalapb.grpc.Channels
import scalapb.grpcweb.Metadata
import slinky.core._
import slinky.core.annotations.react
import slinky.core.facade.Hooks._
import slinky.web.html._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Failure
import scala.util.Success

@react object Unary {
  type Props = Unit

  val stub = ServiceGrpcWeb.stub(Channels.grpcwebChannel("http://localhost:9000"))

  val component: FunctionalComponent[Props] = FunctionalComponent { _ =>
    val (status, setStatus) = useState("Request pending")

    useEffect(
      () => {
        val req                = Request(payload = "Hello!")
        val metadata: Metadata = Metadata("custom-header-1" -> "unary-value")

        stub.unary(req, metadata).onComplete {
          case Success(value) =>
            setStatus(s"Request success: ${value.payload}")
          case Failure(ex) =>
            setStatus(s"Request failure: $ex")
        }
        setStatus("Request sent")
      },
      Seq.empty
    )

    div(
      h2("Unary request:"),
      p(status)
    )
  }
}
