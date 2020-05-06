package center.tilda.tildaapp

object Database {
    val listaObjava: ArrayList<Objava> = ArrayList<Objava>()

    fun add(novaObjava: Objava){
        // Proverava da li smemo da unesemo novu objavu pre samog dodavanja
        this.listaObjava.add(novaObjava)
    }
}