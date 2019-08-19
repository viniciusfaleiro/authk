package br.com.authk.config

import br.com.authk.factory.LocalQueueFactory
import br.com.authk.factory.RingBufferFactory

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
}