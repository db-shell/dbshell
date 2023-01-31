import org.springframework.beans.factory.annotation.Autowired
import org.springframework.shell.Shell

class Test {
    @Autowired
    private val shell: Shell? = null
    fun runShell() {
        val command = "hello"
        assert(true)
        //shell!!.evaluate { "$command 1 2" }
    }
}