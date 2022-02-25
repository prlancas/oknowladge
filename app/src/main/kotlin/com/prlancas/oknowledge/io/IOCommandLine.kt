package com.prlancas.oknowledge.io

import info.ephyra.answerselection.filters.Filter
import info.ephyra.nlp.semantics.Predicate
import info.ephyra.querygeneration.Query
import info.ephyra.questionanalysis.QuestionInterpretation
import info.ephyra.search.Result
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.FileWriter
import java.io.IOException
import java.io.InputStreamReader
import java.lang.Exception
import java.text.SimpleDateFormat

/**
 *
 * Prints out status and error messages as well as results to the standard
 * output. The output of status and error messages can be enabled or disabled.
 * By default, all status and error messages are disabled.
 *
 *
 * All print methods are thread-save to avoid overlapping outputs from
 * different threads.
 *
 * @author Nico Schlaefer
 * @version 2007-07-14
 */
class IOCommandLine(
    var statusMsgs: Boolean = true,
    var errorMsgs: Boolean = true
) : IOInterface {

    /**
     * Prints out an arbitrary status message.
     *
     * @param status a status message
     */
    override fun statusMsg(status: String) {
        if (statusMsgs) printMessage(status)
    }

    /**
     * Prints out an arbitrary status message with a timestamp.
     *
     * @param status a status message
     */
    override fun statusMsgTimestamp(status: String) {
        if (statusMsgs) statusMsg("$status ($timestamp)")
    }

    /**
     * Prints out a target.
     *
     * @param target a target
     */
    override fun printTarget(target: String) {
        var target = target
        if (statusMsgs) {
            target = "Target: $target"
            var line = ""
            for (i in target.indices) line += "="
            printMessage(
                """
    
    $line
    """.trimIndent()
            )
            printMessage(target)
            printMessage(line)
        }
    }

    /**
     * Prints out a question.
     *
     * @param question a question
     */
    override fun printQuestion(question: String) {
        var question = question
        if (statusMsgs) {
            question = "Question: $question"
            var line = ""
            for (i in 0 until question.length) line += "-"
            printMessage(
                """
    
    $line
    """.trimIndent()
            )
            printMessage(question)
            printMessage(line)
        }
    }

    /**
     * Prints out the status message that the engine is in the initialization
     * phase.
     */
    override fun initialising() {
        if (statusMsgs) printMessage("+++++ Initializing engine (" + timestamp + ") +++++")
    }

    /**
     * Prints out the status message that the engine is in the coreference
     * resolution phase.
     */
    override fun printResolvingCoreferences() {
        if (statusMsgs) printMessage(
            """
    
    +++++ Resolving Coreferences ($timestamp) +++++
    """.trimIndent()
        )
    }

    /**
     * Prints out the status message that the engine is in the question analysis
     * phase.
     */
    override fun printAnalyzingQuestion() {
        if (statusMsgs) printMessage(
            """
    
    +++++ Analyzing question ($timestamp) +++++
    """.trimIndent()
        )
    }

    /**
     * Prints out the status message that the engine is in the query generation
     * phase.
     */
    override fun printGeneratingQueries() {
        if (statusMsgs) printMessage(
            """
    
    +++++ Generating queries ($timestamp) +++++
    """.trimIndent()
        )
    }

    /**
     * Prints out the status message that the engine is in the search phase.
     */
    override fun printSearching() {
        if (statusMsgs) printMessage(
            """
    
    +++++ Searching ($timestamp) +++++
    """.trimIndent()
        )
    }

    /**
     * Prints out the status message that the engine is in the answer selection
     * phase.
     */
    override fun printSelectingAnswers() {
        if (statusMsgs) printMessage(
            """
    
    +++++ Selecting Answers ($timestamp) +++++
    """.trimIndent()
        )
    }

    /**
     * Prints out the status message that the TREC data is being loaded.
     */
    override fun printLoadingTRECData() {
        if (statusMsgs) printMessage(
            """
    
    +++++ Loading TREC data ($timestamp) +++++
    """.trimIndent()
        )
    }

    /**
     * Prints out the status message that the questions are being interpreted.
     */
    override fun printInterpretingQuestions() {
        if (statusMsgs) printMessage(
            """
    
    +++++ Interpreting questions ($timestamp) +++++
    """.trimIndent()
        )
    }

    /**
     * Prints out the status message that queries are being formed.
     */
    override fun printFormingQueries() {
        if (statusMsgs) printMessage(
            """
    
    +++++ Forming queries ($timestamp) +++++
    """.trimIndent()
        )
    }

    /**
     * Prints out the status message that text passages are being fetched.
     */
    override fun printFetchingPassages() {
        if (statusMsgs) printMessage(
            """
    
    +++++ Fetching passages ($timestamp) +++++
    """.trimIndent()
        )
    }

    /**
     * Prints out the status message that patterns are being extracted.
     */
    override fun printExtractingPatterns() {
        if (statusMsgs) printMessage(
            """
    
    +++++ Extracting patterns ($timestamp) +++++
    """.trimIndent()
        )
    }

    /**
     * Prints out the status message that patterns are being saved.
     */
    override fun printSavingPatterns() {
        if (statusMsgs) printMessage(
            """
    
    +++++ Saving patterns ($timestamp) +++++
    """.trimIndent()
        )
    }

    /**
     * Prints out the status message that patterns are being loaded.
     */
    override fun printLoadingPatterns() {
        if (statusMsgs) printMessage(
            """
    
    +++++ Loading patterns ($timestamp) +++++
    """.trimIndent()
        )
    }

    /**
     * Prints out the status message that patterns are being assessed.
     */
    override fun printAssessingPatterns() {
        if (statusMsgs) printMessage(
            """
    
    +++++ Assessing patterns ($timestamp) +++++
    """.trimIndent()
        )
    }

    /**
     * Prints out the status message that patterns are being filtered.
     */
    override fun printFilteringPatterns() {
        if (statusMsgs) printMessage(
            """
    
    +++++ Filtering patterns ($timestamp) +++++
    """.trimIndent()
        )
    }

    /**
     * Prints out the question string.
     *
     * @param qs question string
     */
    override fun printQuestionString(qs: String) {
        if (statusMsgs) printMessage("\nQuestion: $qs")
    }

    /**
     * Prints out the question string with resolved coreferences.
     *
     * @param res resolved question string
     */
    override fun printResolvedQuestion(res: String) {
        if (statusMsgs) printMessage("\nResolved question: $res")
    }

    /**
     * Prints out the normalization of a question.
     *
     * @param qn question normalization
     */
    override fun printNormalization(qn: String) {
        if (statusMsgs) printMessage("Normalization: $qn")
    }

    /**
     * Prints out the answer types.
     *
     * @param ats answer types
     */
    override fun printAnswerTypes(ats: Array<String>) {
        if (statusMsgs) {
            printMessage("\nAnswer types:")
            if (ats.size == 0) printMessage("-")
            for (at in ats) printMessage(at)
        }
    }

    /**
     * Prints out the interpretations of a question.
     *
     * @param qis `QuestionInterpretation` array
     */
    override fun printInterpretations(
        qis: Array<QuestionInterpretation>
    ) {
        if (statusMsgs) {
            printMessage("\nInterpretations:")
            if (qis.size == 0) printMessage("-")
            for (qi in qis) {
                printMessage(qi.toString())
            }
        }
    }

    /**
     * Prints out the predicates in a question.
     *
     * @param ps `Predicate` array
     */
    override fun printPredicates(ps: Array<Predicate>) {
        if (statusMsgs) {
            printMessage("\nPredicates:")
            if (ps.size == 0) printMessage("-")
            for (p in ps) {
                printMessage(p.toStringMultiLine())
            }
        }
    }

    /**
     * Prints out query strings.
     *
     * @param queries `Query` objects
     */
    override fun printQueryStrings(queries: Array<Query>) {
        if (statusMsgs) {
            printMessage("Query strings:")
            for (query in queries) printMessage(query.queryString)
        }
    }

    /**
     * Prints out the status message that a filter has started its work in the
     * answer selection phase, plus the number of results passed to the filter.
     *
     * @param    filter        the filter that has just started its work
     * @param    resCount    the number of results passed to the filter
     */
    override fun printFilterStarted(
        filter: Filter?,
        resCount: Int
    ) {
        if (statusMsgs && filter != null) {
            var filterName = filter.javaClass.name
            filterName = filterName.substring(filterName.lastIndexOf(".") + 1)
            printMessage(
                "Filter \"" + filterName + "\" started, " + resCount +
                        " Results (" + timestamp + ")"
            )
        }
    }

    /**
     * Prints out the status message that a filter has finished its work in the
     * answer selection phase, plus the number of remaining results.
     *
     * @param    filter        the filter that has just finished its work
     * @param    resCount    the number of remaining results
     */
    override fun printFilterFinished(
        filter: Filter?,
        resCount: Int
    ) {
        if (statusMsgs && filter != null) {
            var filterName = filter.javaClass.name
            filterName = filterName.substring(filterName.lastIndexOf(".") + 1)
            printMessage(
                "Filter \"" + filterName + "\" finished, " + resCount +
                        " Results (" + timestamp + ")"
            )
        }
    }

    /**
     * Prints out an arbitrary error message.
     *
     * @param error an error message
     */
    override fun errorMsg(error: String?) {
        if (errorMsgs) System.err.println(error)
    }

    /**
     * Prints out an arbitrary error message with a timestamp.
     *
     * @param error an error message
     */
    override fun printErrorMsgTimestamp(error: String) {
        if (errorMsgs) errorMsg(error + " (" + timestamp + ")")
    }

    /**
     * Prints out a search error message.
     *
     * @param e an `Exception` that has been thrown
     */
    override fun printSearchError(e: Exception?) {
        if (errorMsgs) {
            System.err.println("\nSearch error:")
            System.err.println(e)
        }
    }

    /**
     * Prints out an HTTP error message.
     *
     * @param error an error msg
     */
    override fun printHttpError(error: String?) {
        if (errorMsgs) {
            System.err.println("\nHTTP error:")
            System.err.println(error)
        }
    }

    /**
     * Prints out instructions on how to use the program.
     *
     * @param instr instructions
     */
    override fun printUsage(instr: String) {
        printMessage("Usage: $instr")
    }

    /**
     * Prints a prompt for a question.
     */
    override fun questionPrompt() {
        print("\nQuestion: ")
    }

    /**
     * Prints out the answers.
     *
     * @param results `Result` objects
     */
    override fun printAnswers(results: List<Result>) {
        printMessage("\nAnswer:")
        results.forEachIndexed() {i, result ->
            printMessage("[${i + 1}]\t ${result.answer}")
            printMessage("\tScore: ${result.score}")
            if (result.docID != null) printMessage("\tDocument: ${result.docID}")
        }
    }

    /**
     * Prints out an "answer unknown" message.
     */
    override fun printAnswerUnknown() {
        printMessage("\nSorry, I don't know the answer.")
    }

    /**	the DateFormat object used in getTimespamt
     */
    private val timestampFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    /**
     * @return    a timestamp String for logging
     */
	@get:Synchronized
    val timestamp: String
        get() = timestampFormatter.format(System.currentTimeMillis())
    private var logWriter: BufferedWriter? = null

    /**	print a message
     * @param    message        the message to print
     */
    fun printMessage(message: String) {
        println(message)
        if (logWriter != null) try {
            logWriter!!.write(message)
            logWriter!!.newLine()
            logWriter!!.flush()
        } catch (e: Exception) {
        }
    }

    /**	set the log file
     * @param    logFile        the path and name of the filte to write log entries to (in addition to System.out)
     */
    fun setLogFile(logFile: String?) {
        try {
            if (logFile == null) {
                if (logWriter != null) {
                    logWriter!!.close()
                    logWriter = null
                }
            } else {
                if (logWriter != null) logWriter!!.close()
                logWriter = BufferedWriter(FileWriter(logFile))
            }
        } catch (e: Exception) {
            logWriter = null
        }
    }

    /**
     * Reads a line from the command prompt.
     *
     * @return user input
     */
    override fun getLine(): String = try {
            BufferedReader(InputStreamReader(System.`in`)).readLine()
        } catch (e: IOException) {
            ""
        }
}