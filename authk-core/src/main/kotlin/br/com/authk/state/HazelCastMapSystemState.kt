package br.com.authk.state

import br.com.authk.data.AffinityHost
import com.hazelcast.config.Config
import com.hazelcast.core.Hazelcast
import com.hazelcast.core.HazelcastInstance
import com.hazelcast.core.IMap
import java.util.*

public class HazelCastMapSystemState private constructor(): State{
    private var state : IMap<String, String>
    private val mapName = "state"
    private var hazelcastInstance : HazelcastInstance

    init {
        val cfg = Config(UUID.randomUUID().toString())
        hazelcastInstance = Hazelcast.getOrCreateHazelcastInstance(cfg)
        state = hazelcastInstance.getMap(mapName)
    }

    companion object{
        private val instance = HazelCastMapSystemState()

        fun instance() : HazelCastMapSystemState{
            return instance;
        }
    }

    override fun get(key: String): Any? {
        return state[key]
    }

    override fun store(key: String, value: String) {
        state.putAsync(key, value)
    }

    override fun keyHasLocalAffinity(key: String) : Boolean {
        return state.localKeySet().contains(key)
    }

    override fun getAffinityHost(key: String): AffinityHost? {
        var host : AffinityHost? = null

        if(!state.localKeySet().contains(key)){
            var clusterMember = hazelcastInstance!!.partitionService.getPartition(key).owner
            host = AffinityHost(clusterMember.address.host)
        }

        return host
    }

    override fun keySet(): Set<String> {
        return state.localKeySet();
    }
}