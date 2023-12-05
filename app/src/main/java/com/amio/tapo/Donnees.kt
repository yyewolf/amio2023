package com.amio.tapo

class Donnees {
    // graphs[salle][mote][label] = [DonneesMote]
    private var graphs: MutableMap<String, DonneesSalle> = mutableMapOf()

    fun addData(salle: String, mote: String, label: String, donnees: DonneesLabel) {
        if (graphs.containsKey(salle)) {
            if (graphs[salle]!!.containsKey(mote)) {
                if (graphs[salle]!![mote]!!.containsKey(label)) {
                    graphs[salle]!![mote]!![label]!!.add(donnees)
                } else {
                    graphs[salle]!![mote]!![label] = mutableListOf(donnees)
                }
            } else {
                graphs[salle]!![mote] = mutableMapOf<String, MutableList<DonneesLabel>>() as DonneesMote
                graphs[salle]!![mote]!![label] = mutableListOf(donnees)
            }
        } else {
            graphs[salle] = mutableMapOf<String, MutableList<DonneesMote>>() as DonneesSalle
            graphs[salle]!![mote] = mutableMapOf<String, MutableList<DonneesLabel>>() as DonneesMote
            graphs[salle]!![mote]!![label] = mutableListOf(donnees)
        }
    }

    fun salles(): List<String> {
        return graphs.keys.toList()
    }

    fun motes(salle: String): List<String> {
        return graphs[salle]!!.keys.toList()
    }

    fun labels(salle: String, mote: String): List<String> {
        return graphs[salle]!![mote]!!.keys.toList()
    }

    fun donnees(salle: String, mote: String, label: String): List<DonneesLabel> {
        return graphs[salle]!![mote]!![label]!!
    }

}