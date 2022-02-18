package com.prlancas.oknowledge.io

import info.ephyra.nlp.semantics.Predicate
import info.ephyra.querygeneration.Query
import info.ephyra.questionanalysis.QuestionInterpretation
import info.ephyra.search.Result
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.PrintWriter
import java.text.DecimalFormat
import java.util.*

/**
 * Logs all questions that are posed to the system and the answers that are
 * returned to a file. Logging can be enabled or disabled. By default, logging
 * is enabled.
 *
 * @author Nico Schlaefer
 * @version 2005-09-30
 */
class Logger(
    val logfile: File = File("log/OpenEphyra"),
    val enabled: Boolean = true,
    val out: PrintWriter = PrintWriter(FileOutputStream(logfile, true))
) {


    /**
     * Starts an entry for a factoid question.
     *
     * @param question question string
     * @return true, iff logging was successful
     */
    fun logFactoidStart(question: String): Boolean {
        // logging is disabled or log file is not specified
        if (!enabled) return false
        try {
            out.println("<factoid>")
            out.println("\t<time>")
            out.println("\t\t" + Date().toString())
            out.println("\t</time>")
            out.println("\t<question>")
            out.println("\t\t" + question)
            out.println("\t</question>")
            out.close()
        } catch (e: IOException) {
            return false
        }
        return true
    }

    /**
     * Ends an entry for a factoid question.
     *
     * @return true, iff logging was successful
     */
    fun logFactoidEnd(): Boolean {
        // logging is disabled or log file is not specified
        if (!enabled) return false
        try {
            val out = PrintWriter(FileOutputStream(logfile, true))
            out.println("</factoid>")
            out.close()
        } catch (e: IOException) {
            return false
        }
        return true
    }

    /**
     * Starts an entry for a list question.
     *
     * @param question question string
     * @return true, iff logging was successful
     */
    fun logListStart(question: String): Boolean {
        // logging is disabled or log file is not specified
        if (!enabled) return false
        try {
            val out = PrintWriter(FileOutputStream(logfile, true))
            out.println("<list>")
            out.println("\t<time>")
            out.println("\t\t" + Date().toString())
            out.println("\t</time>")
            out.println("\t<question>")
            out.println("\t\t" + question)
            out.println("\t</question>")
            out.close()
        } catch (e: IOException) {
            return false
        }
        return true
    }

    /**
     * Ends an entry for a list question.
     *
     * @return true, iff logging was successful
     */
    fun logListEnd(): Boolean {
        // logging is disabled or log file is not specified
        if (!enabled) return false
        try {
            val out = PrintWriter(FileOutputStream(logfile, true))
            out.println("</list>")
            out.close()
        } catch (e: IOException) {
            return false
        }
        return true
    }

    /**
     * Starts an entry for a definitional question.
     *
     * @param question question string
     * @return true, iff logging was successful
     */
    fun logDefinitionalStart(question: String): Boolean {
        // logging is disabled or log file is not specified
        if (!enabled) return false
        try {
            val out = PrintWriter(FileOutputStream(logfile, true))
            out.println("<definitional>")
            out.println("\t<time>")
            out.println("\t\t" + Date().toString())
            out.println("\t</time>")
            out.println("\t<question>")
            out.println("\t\t" + question)
            out.println("\t</question>")
            out.close()
        } catch (e: IOException) {
            return false
        }
        return true
    }

    /**
     * Ends an entry for a definitional question.
     *
     * @return true, iff logging was successful
     */
    fun logDefinitionalEnd(): Boolean {
        // logging is disabled or log file is not specified
        if (!enabled) return false
        try {
            val out = PrintWriter(FileOutputStream(logfile, true))
            out.println("</definitional>")
            out.close()
        } catch (e: IOException) {
            return false
        }
        return true
    }

    /**
     * Logs the normalization of a question.
     *
     * @param qn question normalization
     * @return true, iff logging was successful
     */
    fun logNormalization(qn: String): Boolean {
        // logging is disabled or log file is not specified
        if (!enabled) return false
        try {
            val out = PrintWriter(FileOutputStream(logfile, true))
            out.println("\t<normalization>")
            out.println("\t\t" + qn)
            out.println("\t</normalization>")
            out.close()
        } catch (e: IOException) {
            return false
        }
        return true
    }

    /**
     * Logs the determined answer types.
     *
     * @param ats answer types
     * @return true, iff logging was successful
     */
    fun logAnswerTypes(ats: Array<String>): Boolean {
        // logging is disabled or log file is not specified
        if (!enabled) return false
        try {
            val out = PrintWriter(FileOutputStream(logfile, true))
            for (at in ats) {
                out.println("\t<answertype>")
                out.println("\t\t" + at)
                out.println("\t</answertype>")
            }
            out.close()
        } catch (e: IOException) {
            return false
        }
        return true
    }

    /**
     * Logs the interpretations of a question.
     *
     * @param qis question interpretations
     * @return true, iff logging was successful
     */
    fun logInterpretations(qis: Array<QuestionInterpretation>): Boolean {
        // logging is disabled or log file is not specified
        if (!enabled) return false
        try {
            val out = PrintWriter(FileOutputStream(logfile, true))
            for (qi in qis) {
                out.println("\t<interpretation>")
                out.println("\t\t<property>")
                out.println("\t\t\t" + qi.property)
                out.println("\t\t</property>")
                out.println("\t\t<target>")
                out.println("\t\t\t" + qi.target)
                out.println("\t\t</target>")
                for (context in qi.context) {
                    out.println("\t\t<context>")
                    out.println("\t\t\t" + context)
                    out.println("\t\t</context>")
                }
                out.println("\t</interpretation>")
            }
            out.close()
        } catch (e: IOException) {
            return false
        }
        return true
    }

    /**
     * Logs the predicates in a question.
     *
     * @param ps predicates
     * @return true, iff logging was successful
     */
    fun logPredicates(ps: Array<Predicate>): Boolean {
        // logging is disabled or log file is not specified
        if (!enabled) return false
        try {
            val out = PrintWriter(FileOutputStream(logfile, true))
            for (p in ps) {
                out.println("\t<predicate>")
                out.println("\t\t" + p.annotated)
                out.println("\t</predicate>")
            }
            out.close()
        } catch (e: IOException) {
            return false
        }
        return true
    }

    /**
     * Logs the query strings.
     *
     * @param queries the queries
     * @return true, iff logging was successful
     */
    fun logQueryStrings(queries: Array<Query>): Boolean {
        // logging is disabled or log file is not specified
        if (!enabled) return false
        try {
            val out = PrintWriter(FileOutputStream(logfile, true))
            for (query in queries) {
                out.println("\t<querystring>")
                out.println("\t\t" + query.queryString)
                out.println("\t</querystring>")
            }
            out.close()
        } catch (e: IOException) {
            return false
        }
        return true
    }

    /**
     * Logs the results returned by the QA engine.
     *
     * @param results the results
     * @return true, iff logging was successful
     */
    fun logResults(results: List<Result>): Boolean {
        // logging is disabled or log file is not specified
        if (!enabled) return false
        try {
            val out = PrintWriter(FileOutputStream(logfile, true))
            results.forEach() {
                out.println("\t<result>")
                out.println("\t\t<answer>")
                out.println("\t\t\t" + it.answer)
                out.println("\t\t</answer>")
                out.println("\t\t<score>")
                out.println("\t\t\t" + it.score)
                out.println("\t\t</score>")
                if (it.docID != null) {
                    out.println("\t\t<docid>")
                    out.println("\t\t\t" + it.docID)
                    out.println("\t\t</docid>")
                }
                val qi = it.query.interpretation
                if (qi != null) {
                    out.println("\t\t<interpretation>")
                    out.println("\t\t\t<property>")
                    out.println("\t\t\t\t" + qi.property)
                    out.println("\t\t\t</property>")
                    out.println("\t\t\t<target>")
                    out.println("\t\t\t\t" + qi.target)
                    out.println("\t\t\t</target>")
                    for (context in qi.context) {
                        out.println("\t\t\t<context>")
                        out.println("\t\t\t\t" + context)
                        out.println("\t\t\t</context>")
                    }
                    out.println("\t\t</interpretation>")
                }
                out.println("\t</result>")
            }
            out.close()
        } catch (e: IOException) {
            return false
        }
        return true
    }

    /**
     * Logs results with true/false judgements.
     *
     * @param results the results
     * @param correct judgements
     * @return true, iff logging was successful
     */
    fun logResultsJudged(results: Array<Result>, correct: BooleanArray): Boolean {
        // logging is disabled or log file is not specified
        if (!enabled) return false
        try {
            val out = PrintWriter(FileOutputStream(logfile, true))
            for (i in results.indices) {
                out.println("\t<result>")
                out.println("\t\t<answer>")
                out.println("\t\t\t" + results[i].answer)
                out.println("\t\t</answer>")
                out.println("\t\t<score>")
                out.println("\t\t\t" + results[i].score)
                out.println("\t\t</score>")
                if (results[i].docID != null) {
                    out.println("\t\t<docid>")
                    out.println("\t\t\t" + results[i].docID)
                    out.println("\t\t</docid>")
                }
                out.println("\t\t<correct>")
                out.println(
                    if (correct[i]) "\t\t\ttrue" else "\t\t\tfalse"
                )
                out.println("\t\t</correct>")
                out.println("\t</result>")
            }
            out.close()
        } catch (e: IOException) {
            return false
        }
        return true
    }

    /**
     * Logs the resulting precision and MRR.
     *
     * @param precision the precision
     * @param mrr mean reciprocal rank
     * @return true, iff logging was successful
     */
    fun logScores(precision: Float, mrr: Float): Boolean {
        // logging is disabled or log file is not specified
        if (!enabled) return false
        val df = DecimalFormat()
        df.maximumFractionDigits = 3
        df.minimumFractionDigits = 3
        try {
            val out = PrintWriter(FileOutputStream(logfile, true))
            out.println("<scores>")
            out.println("\t<precision>")
            out.println("\t\t" + df.format(precision.toDouble()))
            out.println("\t</precision>")
            out.println("\t<mrr>")
            out.println("\t\t" + df.format(mrr.toDouble()))
            out.println("\t</mrr>")
            out.println("</scores>")
            out.close()
        } catch (e: IOException) {
            return false
        }
        return true
    }

    /**
     * Logs the score of the factoid component.
     *
     * @param score the score
     * @param absThresh absolute confidence threshold for results
     */
    fun logFactoidScore(score: Float, absThresh: Float): Boolean {
        // logging is disabled or log file is not specified
        if (!enabled) return false
        val df = DecimalFormat()
        df.maximumFractionDigits = 3
        df.minimumFractionDigits = 3
        try {
            val out = PrintWriter(FileOutputStream(logfile, true))
            out.println("<factoidscore abs_thresh=\"$absThresh\">")
            out.println("\t" + df.format(score.toDouble()))
            out.println("</factoidscore>")
            out.close()
        } catch (e: IOException) {
            return false
        }
        return true
    }

    /**
     * Logs the score of the list component.
     *
     * @param score the score
     * @param relThresh relative confidence threshold for results
     */
    fun logListScore(score: Float, relThresh: Float): Boolean {
        // logging is disabled or log file is not specified
        if (!enabled) return false
        val df = DecimalFormat()
        df.maximumFractionDigits = 3
        df.minimumFractionDigits = 3
        try {
            val out = PrintWriter(FileOutputStream(logfile, true))
            out.println("<listscore rel_thresh=\"$relThresh\">")
            out.println("\t" + df.format(score.toDouble()))
            out.println("</listscore>")
            out.close()
        } catch (e: IOException) {
            return false
        }
        return true
    }
}