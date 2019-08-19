package br.com.authk.processor

import br.com.authk.config.Configuration
import br.com.authk.context.Context
import br.com.authk.data.ContextEvent
import br.com.authk.data.Message
import br.com.authk.rpc.authorization.AuthorizationGrpc
import br.com.authk.rpc.authorization.AuthorizationResponse
import br.com.authk.rpc.authorization.Empty
import br.com.authk.util.TPSLogger
import com.lmax.disruptor.EventHandler
import io.grpc.ManagedChannelBuilder
import io.grpc.stub.StreamObserver
import java.lang.RuntimeException

class AuthGRPCOutboundMessageProcessor: EventHandler<ContextEvent> {
    private val tpsLogger = TPSLogger(this.javaClass.canonicalName)

    private val stubMap : MutableMap<String, AuthorizationGrpc.AuthorizationStub> = mutableMapOf()

    override fun onEvent(event: ContextEvent?, sequence: Long, wtv: Boolean) {
        tpsLogger.event()

        val msg = (event!!.context!!.get(Context.MESSAGE_ID_KEY) as Message);

        val producerHost = msg.producerHost
        var destinationHost : String = Configuration.instance().grpcDefaultOutboundHost()

        if(!stubMap.containsKey(producerHost)){
           val mngChannel = ManagedChannelBuilder.forAddress(Configuration.instance().grpcDefaultOutboundHost(), Configuration.instance().grpcDefaultOutboundPort())
                .usePlaintext()
                .build()

            destinationHost = producerHost ?: destinationHost

            if(!stubMap.containsKey(destinationHost)){
                stubMap[destinationHost] = AuthorizationGrpc.newStub(mngChannel)
            }
        }

        val authResponse = AuthorizationResponse.newBuilder()
            .setResponseCode("0100")
            .build()

        val reqStreamObserver = stubMap[destinationHost]!!.response(AuthResponseStreamObserver())

        reqStreamObserver.onNext(authResponse)
    }

    class AuthResponseStreamObserver : StreamObserver<Empty>{
        override fun onNext(p0: Empty?) {
        }

        override fun onError(p0: Throwable?) {
            p0!!.printStackTrace()
            throw RuntimeException(p0)
        }

        override fun onCompleted() {
        }
    }
}