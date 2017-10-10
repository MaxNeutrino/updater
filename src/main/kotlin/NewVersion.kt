class NewVersion private constructor() {
    private object Holder { val INSTANCE = NewVersion() }

    companion object {
        val instance: NewVersion by lazy { Holder.INSTANCE }
    }

    var url:String = ""
    var targetPath = ""
}