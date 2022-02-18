package com.prlancas.oknowledge.trec/*package com.prlancas.oknowledge

import com.prlancas.oknowledge.trec.OpenEphyraCorpus.askFactoid
import com.prlancas.oknowledge.trec.OpenEphyraCorpus
import info.ephyra.trec.TRECQuestion
import info.ephyra.trec.TREC8To12Parser
import com.prlancas.oknowledge.EphyraTREC8To11
import com.prlancas.oknowledge.io.Logger
import info.ephyra.trec.TRECPattern
import com.prlancas.oknowledge.io.MsgPrinter
import info.ephyra.search.Result
import info.ephyra.trec.TREC13To16Parser
import java.util.*
import java.util.regex.Pattern
import kotlin.jvm.JvmStatic

/**
 *
 * Runs and evaluates Ephyra on the data from the TREC 8-11 QA tracks.
 *
 *
 * This class extends `OpenEphyraCorpus`.
 *
 * @author Nico Schlaefer
 * @version 2007-07-11
 */
object EphyraTREC8To11 : OpenEphyraCorpus() {
    /** Maximum number of factoid answers.  */
    internal const val FACTOID_MAX_ANSWERS = 5

    /** Absolute threshold for factoid answer scores.  */
    internal const val FACTOID_ABS_THRESH = 0f

    /** Log file for the results returned by Ephyra.  */
    private var logFile: String? = null

    /** Load answers from log file?  */
    private var loadLog = false

    /** Question strings.  */
    internal var qss: Array<String?>

    /** Corresponding regular expressions that describe correct answers.  */
    internal var regexs: Array<String?>

    /**
     * Loads questions and patterns from files.
     *
     * @param qFile name of the question file
     * @param pFile name of the pattern file
     */
    private fun loadTRECData(qFile: String, pFile: String) {
        // load questions from file
        val questions = TREC8To12Parser.loadQuestions(qFile)
        qss = arrayOfNulls(questions.size)
        for (i in questions.indices) qss[i] = questions[i].questionString

        // load patterns from file
        val patterns = TREC8To12Parser.loadPatternsAligned(pFile)
        regexs = arrayOfNulls(questions.size)
        for (i in questions.indices) if (i < patterns.size && patterns[i] != null) regexs[i] = patterns[i]!!
            .regexs[0]
    }

    /**
     * Initializes Ephyra, asks the questions and evaluates and logs the
     * answers.
     */
    private fun runAndEval() {
        // initialize Ephyra
        val ephyra = EphyraTREC8To11()
        var precision = 0f
        var mrr = 0f
        for (i in qss.indices) {
            MsgPrinter.printQuestion(qss[i])
            Logger.enableLogging(false)

            // ask Ephyra or load answer from log file
            var results: Array<Result?>? = null
            if (loadLog) results = TREC13To16Parser.loadResults(
                qss[i], "FACTOID",
                logFile
            )
            if (results == null) {  // answer not loaded from log file
                Logger.enableLogging(true)
                Logger.logFactoidStart(qss[i])
                results = ephyra.askFactoid(
                    qss[i], FACTOID_MAX_ANSWERS,
                    FACTOID_ABS_THRESH
                )
            }

            // evaluate answers
            val correct = BooleanArray(results.size)
            var firstCorrect = 0
            if (regexs[i] != null) {
                val p = Pattern.compile(regexs[i])
                for (j in results.indices) {
                    val m = p.matcher(results[j]!!.answer)
                    correct[j] = m.find()
                    if (correct[j] && firstCorrect == 0) firstCorrect = j + 1
                }
            }
            if (firstCorrect > 0) {
                precision++
                mrr += 1f / firstCorrect
            }
            Logger.logResultsJudged(results, correct)
            Logger.logFactoidEnd()
        }
        precision /= qss.size.toFloat()
        mrr /= qss.size.toFloat()
        Logger.logScores(precision, mrr)
    }

    /**
     * Runs and evaluates Epyhra on TREC data.
     *
     * @param args argument 1: name of the question file<br></br>
     * argument 2: name of the pattern file<br></br>
     * [argument 3: log=logfile (if not set an unambiguous file name
     * is generated automatically)]<br></br>
     * [argument 5: load_log (answers are loaded from the log file
     * instead of querying Ephyra)]
     */
    @JvmStatic
    fun main(args: Array<String>) {
        // enable output of status and error messages
        MsgPrinter.enableStatusMsgs(true)
        MsgPrinter.enableErrorMsgs(true)
        if (args.size < 2) {
            MsgPrinter.printUsage(
                "java EphyraTREC8To11 question_file " +
                        "pattern_file [log=logfile] [load_log]"
            )
            System.exit(1)
        }

        // load questions and patterns
        loadTRECData(args[0], args[1])
        for (i in 2 until args.size) if (args[i].matches("log=.*")) {
            // set log file
            logFile = args[i].substring(4)
        } else if (args[i] == "load_log") {
            // answers are loaded from log file
            loadLog = true
        }

        // if log file not set, generate unambiguous name
        if (logFile == null) {
            var n = ""
            val m = Pattern.compile("\\d++").matcher(args[0])
            if (m.find()) n = m.group(0)
            var date = ""
            val c: Calendar = GregorianCalendar()
            date += c[Calendar.DAY_OF_MONTH]
            if (date.length == 1) date = "0$date"
            date = (c[Calendar.MONTH] + 1).toString() + date
            if (date.length == 3) date = "0$date"
            date = c[Calendar.YEAR].toString() + date
            logFile = "log/TREC" + n + "_" + date
        }
        Logger.setLogfile(logFile)

        // ask Ephyra the questions and evaluate the answers
        runAndEval()
    }
}

 */