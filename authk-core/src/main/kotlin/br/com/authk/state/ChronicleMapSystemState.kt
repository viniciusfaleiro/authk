package br.com.authk.state

import br.com.authk.data.AffinityHost
import net.openhft.chronicle.map.ChronicleMap
import net.openhft.chronicle.map.ChronicleMapBuilder
import java.io.File

class ChronicleMapSystemState private constructor(): State {
    private val avgSize : Double = 256.0
    private val entries : Long = 1_000_000
    private val mapName = "c:/tmp/system_state.dat"
    private val state : MutableMap<String, String>

    init {
        state = loadMap()
    }

    companion object{
        private val instance = ChronicleMapSystemState()

        fun instance() : ChronicleMapSystemState{
            return instance;
        }
    }

    override fun get(key: String): Any? {
        return state[key]
    }

    override fun store(key: String, value: String) {
        state[key] = value
    }

    private fun loadMap() : ChronicleMap<String, String> {
        return ChronicleMapBuilder
            .of(String::class.java, String::class.java)
            .averageKeySize(avgSize)
            .averageValueSize(avgSize)
            .entries(entries)
            .createPersistedTo(File(mapName));
    }

    override fun keyHasLocalAffinity(key: String): Boolean {
        return true
    }

    override fun getAffinityHost(key: String): AffinityHost? {
        return null
    }

    override fun keySet(): Set<String> {
        return state.keys
    }
}