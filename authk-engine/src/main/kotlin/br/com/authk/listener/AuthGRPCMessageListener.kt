package br.com.authk.listener

import br.com.authk.config.Configuration
import br.com.authk.ring.RingPublisher
import br.com.authk.rpc.authorization.AuthorizationGrpc
import br.com.authk.rpc.authorization.AuthorizationRequest
import br.com.authk.rpc.authorization.AuthorizationResponse
import br.com.authk.rpc.authorization.Empty
import br.com.authk.sequence.StatefullSequencer
import br.com.authk.util.TPSLogger
import io.grpc.ServerBuilder
import io.grpc.stub.StreamObserver
import java.lang.RuntimeException
import java.nio.file.Files.getOwner
import com.hazelcast.core.Partition
import com.hazelcast.core.Hazelcast
import com.hazelcast.core.PartitionService

class AuthGRPCMessageListener : Listener {
    override fun listen() {
        var server = ServerBuilder.forPort(Configuration.instance().grpcServerPort()).addService(AuthorizationService()).build()
        server.start()

        server.awaitTermination()
    }

    class AuthorizationService : AuthorizationGrpc.AuthorizationImplBase() {
        override fun authorize(responseObserver: StreamObserver<Empty>?): StreamObserver<AuthorizationRequest> {
            return AuthorizationResponseStreamObserver()
        }
    }

    class AuthorizationResponseStreamObserver (): StreamObserver<AuthorizationRequest> {
        private val publisher: RingPublisher = RingPublisher()
        private val sequencer = StatefullSequencer()
        private val tpsLogger = TPSLogger(this.javaClass.canonicalName)

        override fun onError(p0: Throwable?) {
            p0!!.printStackTrace()
            throw RuntimeException(p0)
        }

        override fun onCompleted() {
        }

        override fun onNext(authReq: AuthorizationRequest) {
            println("Req Auth:" + authReq.requestId + " Cartão:" + authReq.accountNumber + " Valor:" + authReq.transactionAmount + " Data:" + authReq.dateTime)
        }
    }
}