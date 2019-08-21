package br.com.authk.config

import br.com.authk.factory.LocalQueueFactory
import br.com.authk.factory.RingBufferFactory
import java.time.Duration

abstract class Configuration {
    companion object{
        private lateinit var config : Configuration;

        fun instance() : Configuration{
            return config
        }

        fun config(cfg : Configuration){
            config = cfg;
        }
    }

    abstract fun localQueueFactory() : LocalQueueFactory
    abstract fun ringBufferFactory() : RingBufferFactory
    abstract fun grpcDefaultOutboundHost() : String
    abstract fun grpcDefaultOutboundPort() : Int
    abstract fun grpcServerPort() : Int
    abstract fun adviseKeySet(): Boolean
    abstract fun keyAdviseFrequency() : Long
    abstract fun getAdministrationBrokerList() : String
    abstract fun getAdministrationTopic() : String
    abstract fun getAdministrationConsumerPoolDuration() : Duration

}