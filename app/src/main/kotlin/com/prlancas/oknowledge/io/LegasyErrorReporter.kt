package com.prlancas.oknowledge.io

import info.ephyra.answerselection.filters.Filter
import info.ephyra.nlp.semantics.Predicate
import info.ephyra.querygeneration.Query
import info.ephyra.questionanalysis.QuestionInterpretation

class LegasyErrorReporter {

    companion object {

        lateinit var ioInterface: IOInterface
        lateinit var logger: Logger

        @JvmStatic
        fun errorMsg(error: String) {
            ioInterface.errorMsg(error)
        }

        @JvmStatic
        fun printResolvedQuestion(resolvedQuestions: String) {
            ioInterface.printResolvedQuestion(resolvedQuestions)
        }

        @JvmStatic
        fun printNormalization(stemmed: String) {
            ioInterface.printResolvedQuestion(stemmed)
            logger.logNormalization(stemmed)
        }

        @JvmStatic
        fun printAnswerTypes(ats: Array<String>) {
            ioInterface.printAnswerTypes(ats)
            logger.logAnswerTypes(ats)
        }

        @JvmStatic
        fun printSearchError(e: Exception) {
            ioInterface.printSearchError(e)
        }

        @JvmStatic
        fun printInterpretations(qis: Array<QuestionInterpretation>) {
            ioInterface.printInterpretations(qis)
            logger.logInterpretations(qis)
        }

        @JvmStatic
        fun statusMsg(msg: String) {
            ioInterface.statusMsg(msg)
        }

        @JvmStatic
        fun printUsage(msg: String) {
            ioInterface.printUsage(msg)
        }

        @JvmStatic
        fun printPredicates(ps: Array<Predicate>) {
            ioInterface.printPredicates(ps)
            logger.logPredicates(ps)
        }

        @JvmStatic
        fun printQueryStrings(queries: Array<Query>) {
            ioInterface.printQueryStrings(queries)
            logger.logQueryStrings(queries)
        }

        @JvmStatic
        fun printQuestionString(qss: String) {
            ioInterface.printQuestionString(qss)
        }

        @JvmStatic
        fun initialising() {
            ioInterface.initialising()
        }

        @JvmStatic
        fun printLoadingTRECData() {
            ioInterface.printLoadingTRECData()
        }

        @JvmStatic
        fun printInterpretingQuestions() {
            ioInterface.printInterpretingQuestions()
        }

        @JvmStatic
        fun printFormingQueries() {
            ioInterface.printFormingQueries()
        }

        @JvmStatic
        fun printFetchingPassages() {
            ioInterface.printFetchingPassages()
        }

        @JvmStatic
        fun printExtractingPatterns() {
            ioInterface.printExtractingPatterns()
        }

        @JvmStatic
        fun printSavingPatterns() {
            ioInterface.printSavingPatterns()
        }

        @JvmStatic
        fun printLoadingPatterns() {
            ioInterface.printLoadingPatterns()
        }

        @JvmStatic
        fun printAssessingPatterns() {
            ioInterface.printAssessingPatterns()
        }

        @JvmStatic
        fun printFilteringPatterns() {
            ioInterface.printFilteringPatterns()
        }

        @JvmStatic
        fun statusMsgTimestamp(s: String) {
            ioInterface.statusMsgTimestamp(s)
        }

        @JvmStatic
        fun printHttpError(s: String) {
            ioInterface.printHttpError(s)
        }

        @JvmStatic
        fun printFilterStarted(filter: Filter, length: Int) {
            ioInterface.printFilterStarted(filter, length)
        }

        @JvmStatic
        fun printFilterFinished(filter: Filter, length: Int) {
            ioInterface.printFilterFinished(filter, length)
        }

    }


}