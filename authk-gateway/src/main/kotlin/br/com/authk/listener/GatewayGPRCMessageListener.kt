package br.com.authk.listener

import br.com.authk.config.Configuration
import br.com.authk.context.Context
import br.com.authk.data.Message
import br.com.authk.factory.RingID
import br.com.authk.ring.RingPublisher
import br.com.authk.rpc.authorization.AuthorizationGrpc
import br.com.authk.rpc.authorization.AuthorizationRequest
import br.com.authk.rpc.authorization.Empty
import br.com.authk.sequence.StatefullSequencer
import br.com.authk.util.TPSLogger
import io.grpc.ServerBuilder
import io.grpc.stub.StreamObserver
import java.lang.RuntimeException
import java.lang.management.ManagementFactory
import java.net.InetAddress
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

class GatewayGPRCMessageListener : Listener {
    override fun listen() {
        var server = ServerBuilder.forPort(Configuration.instance().grpcServerPort()).addService(AuthorizationService()).build()
        server.start()

        //server.awaitTermination()
    }

    class AuthorizationService : AuthorizationGrpc.AuthorizationImplBase() {
        override fun authorize(responseObserver: StreamObserver<Empty>?): StreamObserver<AuthorizationRequest> {
            return AuthorizationRequestStreamObserver()
        }
    }

    class AuthorizationRequestStreamObserver (): StreamObserver<AuthorizationRequest> {
        private val publisher: RingPublisher = RingPublisher()
        private val sequencer = StatefullSequencer()
        private val tpsLogger = TPSLogger(this.javaClass.canonicalName)

        override fun onError(p0: Throwable?) {
            p0!!.printStackTrace()
            throw RuntimeException(p0)
        }

        override fun onCompleted() {
        }

        override fun onNext(authReq: AuthorizationRequest?) {
            tpsLogger.event()

            val ctx = Context()
            ctx.save(Context.MESSAGE_ID_KEY, Message(
                "0100",
                InetAddress.getLocalHost().hostName,
                null,
                authReq,
                sequencer.next(),
                UUID.randomUUID(),
                null,
                ManagementFactory.getRuntimeMXBean().name,
                ZonedDateTime.now(ZoneId.systemDefault())))

            publisher.publish(ctx, RingID.INBOUND_TO_PRE_BUSINESS)
        }
    }
}