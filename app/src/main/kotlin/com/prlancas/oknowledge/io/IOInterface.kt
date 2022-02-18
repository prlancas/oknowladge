package com.prlancas.oknowledge.io

import info.ephyra.answerselection.filters.Filter
import info.ephyra.nlp.semantics.Predicate
import info.ephyra.querygeneration.Query
import info.ephyra.questionanalysis.QuestionInterpretation
import info.ephyra.search.Result
import java.lang.Exception

interface IOInterface {
    /**
     * Prints out an arbitrary status message.
     *
     * @param status a status message
     */
    fun statusMsg(status: String)

    /**
     * Prints out an arbitrary status message with a timestamp.
     *
     * @param status a status message
     */
    fun statusMsgTimestamp(status: String)

    /**
     * Prints out a target.
     *
     * @param target a target
     */
    fun printTarget(target: String)

    /**
     * Prints out a question.
     *
     * @param question a question
     */
    fun printQuestion(question: String)

    /**
     * Prints out the status message that the engine is in the initialization
     * phase.
     */
    fun initialising()

    /**
     * Prints out the status message that the engine is in the coreference
     * resolution phase.
     */
    fun printResolvingCoreferences()

    /**
     * Prints out the status message that the engine is in the question analysis
     * phase.
     */
    fun printAnalyzingQuestion()

    /**
     * Prints out the status message that the engine is in the query generation
     * phase.
     */
    fun printGeneratingQueries()

    /**
     * Prints out the status message that the engine is in the search phase.
     */
    fun printSearching()

    /**
     * Prints out the status message that the engine is in the answer selection
     * phase.
     */
    fun printSelectingAnswers()

    /**
     * Prints out the status message that the TREC data is being loaded.
     */
    fun printLoadingTRECData()

    /**
     * Prints out the status message that the questions are being interpreted.
     */
    fun printInterpretingQuestions()

    /**
     * Prints out the status message that queries are being formed.
     */
    fun printFormingQueries()

    /**
     * Prints out the status message that text passages are being fetched.
     */
    fun printFetchingPassages()

    /**
     * Prints out the status message that patterns are being extracted.
     */
    fun printExtractingPatterns()

    /**
     * Prints out the status message that patterns are being saved.
     */
    fun printSavingPatterns()

    /**
     * Prints out the status message that patterns are being loaded.
     */
    fun printLoadingPatterns()

    /**
     * Prints out the status message that patterns are being assessed.
     */
    fun printAssessingPatterns()

    /**
     * Prints out the status message that patterns are being filtered.
     */
    fun printFilteringPatterns()

    /**
     * Prints out the question string.
     *
     * @param qs question string
     */
    fun printQuestionString(qs: String)

    /**
     * Prints out the question string with resolved coreferences.
     *
     * @param res resolved question string
     */
    fun printResolvedQuestion(res: String)

    /**
     * Prints out the normalization of a question.
     *
     * @param qn question normalization
     */
    fun printNormalization(qn: String)

    /**
     * Prints out the answer types.
     *
     * @param ats answer types
     */
    fun printAnswerTypes(ats: Array<String>)

    /**
     * Prints out the interpretations of a question.
     *
     * @param qis `QuestionInterpretation` array
     */
    fun printInterpretations(
        qis: Array<QuestionInterpretation>
    )

    /**
     * Prints out the predicates in a question.
     *
     * @param ps `Predicate` array
     */
    fun printPredicates(ps: Array<Predicate>)

    /**
     * Prints out query strings.
     *
     * @param queries `Query` objects
     */
    fun printQueryStrings(queries: Array<Query>)

    /**
     * Prints out the status message that a filter has started its work in the
     * answer selection phase, plus the number of results passed to the filter.
     *
     * @param    filter        the filter that has just started its work
     * @param    resCount    the number of results passed to the filter
     */
    fun printFilterStarted(
        filter: Filter?,
        resCount: Int
    )

    /**
     * Prints out the status message that a filter has finished its work in the
     * answer selection phase, plus the number of remaining results.
     *
     * @param    filter        the filter that has just finished its work
     * @param    resCount    the number of remaining results
     */
    fun printFilterFinished(
        filter: Filter?,
        resCount: Int
    )

    /**
     * Prints out an arbitrary error message.
     *
     * @param error an error message
     */
    fun errorMsg(error: String?)

    /**
     * Prints out an arbitrary error message with a timestamp.
     *
     * @param error an error message
     */
    fun printErrorMsgTimestamp(error: String)

    /**
     * Prints out a search error message.
     *
     * @param e an `Exception` that has been thrown
     */
    fun printSearchError(e: Exception?)

    /**
     * Prints out an HTTP error message.
     *
     * @param error an error msg
     */
    fun printHttpError(error: String?)

    /**
     * Prints out instructions on how to use the program.
     *
     * @param instr instructions
     */
    fun printUsage(instr: String)

    /**
     * Prints a prompt for a question.
     */
    fun questionPrompt()

    /**
     * Prints out the answers.
     *
     * @param results `Result` objects
     */
    fun printAnswers(results: List<Result>)

    /**
     * Prints out an "answer unknown" message.
     */
    fun printAnswerUnknown()


    fun getLine(): String
}