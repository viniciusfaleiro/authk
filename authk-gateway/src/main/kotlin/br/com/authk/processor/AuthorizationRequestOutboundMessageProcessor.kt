package br.com.authk.processor

import br.com.authk.config.Configuration
import br.com.authk.context.Context
import br.com.authk.data.ContextEvent
import br.com.authk.data.ContextKey
import br.com.authk.data.InternalAuthorizationRequest
import br.com.authk.data.Message
import br.com.authk.rpc.authorization.AuthorizationGrpc
import br.com.authk.rpc.authorization.Empty
import br.com.authk.rpc.authorization.AuthorizationRequest
import br.com.authk.util.TPSLogger
import com.lmax.disruptor.EventHandler
import io.grpc.ManagedChannelBuilder
import io.grpc.stub.StreamObserver
import java.lang.RuntimeException

class AuthorizationRequestOutboundMessageProcessor: EventHandler<ContextEvent> {
    private val tpsLogger = TPSLogger(this.javaClass.canonicalName)
    private val routingMap : Map<String, String> = mutableMapOf()
    private val stubMap : MutableMap<String, AuthorizationGrpc.AuthorizationStub> = mutableMapOf()

    override fun onEvent(event: ContextEvent?, sequence: Long, wtv: Boolean) {
        tpsLogger.event()

        var req = event?.context?.get(ContextKey.OPERATION_REQUEST_ID.toString()) as InternalAuthorizationRequest
        var destinationHost : String?

        if(req != null){
            destinationHost = routingMap[req.getCardNumber()]

            if(destinationHost == null){
                destinationHost = Configuration.instance().grpcDefaultOutboundHost()
            }

            if(!stubMap.containsKey(destinationHost)){
                val mngChannel = ManagedChannelBuilder.forAddress(Configuration.instance().grpcDefaultOutboundHost(), Configuration.instance().grpcDefaultOutboundPort())
                    .usePlaintext()
                    .build()

                if(!stubMap.containsKey(destinationHost)){
                    stubMap[destinationHost] = AuthorizationGrpc.newStub(mngChannel)
                }
            }

            val authService = stubMap[destinationHost]

            val authRequest = (event!!.context!!.get(ContextKey.OPERATION_REQUEST_ID.toString()) as InternalAuthorizationRequest).asAuthorizationRequest()

            authService?.authorize(AuthResponseStreamObserver())?.onNext(authRequest)
        }
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