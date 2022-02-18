/*package com.prlancas.oknowledge.trec

import com.prlancas.oknowledge.OpenEphyra
import info.ephyra.nlp.semantics.ontologies.Ontology
import info.ephyra.nlp.semantics.ontologies.WordNet
import info.ephyra.questionanalysis.QuestionAnalysis
import info.ephyra.querygeneration.QueryGeneration
import info.ephyra.querygeneration.generators.BagOfWordsG
import info.ephyra.querygeneration.generators.BagOfTermsG
import info.ephyra.querygeneration.generators.PredicateG
import info.ephyra.querygeneration.generators.QuestionInterpretationG
import info.ephyra.querygeneration.generators.QuestionReformulationG
import info.ephyra.search.Search
import info.ephyra.search.searchers.IndriKM
import info.ephyra.answerselection.AnswerSelection
import info.ephyra.answerselection.filters.AnswerTypeFilter
import info.ephyra.answerselection.filters.AnswerPatternFilter
import info.ephyra.answerselection.filters.WebDocumentFetcherFilter
import info.ephyra.answerselection.filters.PredicateExtractionFilter
import info.ephyra.answerselection.filters.FactoidsFromPredicatesFilter
import info.ephyra.answerselection.filters.TruncationFilter
import info.ephyra.search.searchers.BingKM
import info.ephyra.answerselection.filters.StopwordFilter
import info.ephyra.answerselection.filters.QuestionKeywordsFilter
import info.ephyra.answerselection.filters.AnswerProjectionFilter
import info.ephyra.answerselection.filters.ScoreNormalizationFilter
import info.ephyra.answerselection.filters.ScoreCombinationFilter
import info.ephyra.answerselection.filters.FactoidSubsetFilter
import info.ephyra.answerselection.filters.DuplicateFilter
import info.ephyra.answerselection.filters.ScoreSorterFilter
import info.ephyra.answerselection.filters.ResultLengthFilter
import com.prlancas.oknowledge.io.MsgPrinter
import info.ephyra.questionanalysis.QuestionNormalizer
import kotlin.jvm.JvmStatic
import com.prlancas.oknowledge.io.Logger
import info.ephyra.search.Result
import java.util.ArrayList

/**
 *
 * A modified version of `OpenEphyra` that is optimized for the
 * TREC evaluation. If no answers are found, the question is assumed to ask for
 * a proper name and the pipeline is rerun to improve the recall. This setup
 * extracts answers from the Web and projects them onto a local corpus.
 *
 *
 * This class extends `OpenEphyra`.
 *
 * @author Nico Schlaefer
 * @version 2008-01-26
 */
open class OpenEphyraCorpus : OpenEphyra() {
    /**
     * Initializes the pipeline for factoid questions, using a local corpus as a
     * knowledge source.
     */
    protected fun initFactoidCorpus() {
        // question analysis
        val wordNet: Ontology = WordNet()
        // - dictionaries for term extraction
        QuestionAnalysis.clearDictionaries()
        QuestionAnalysis.addDictionary(wordNet)
        // - ontologies for term expansion
        QuestionAnalysis.clearOntologies()
        QuestionAnalysis.addOntology(wordNet)

        // query generation
        QueryGeneration.clearQueryGenerators()
        QueryGeneration.addQueryGenerator(BagOfWordsG())
        QueryGeneration.addQueryGenerator(BagOfTermsG())
        QueryGeneration.addQueryGenerator(PredicateG())
        QueryGeneration.addQueryGenerator(QuestionInterpretationG())
        QueryGeneration.addQueryGenerator(QuestionReformulationG())

        // search
        // - knowledge miners for unstructured knowledge sources
        Search.clearKnowledgeMiners()
        for (indriIndices in IndriKM.getIndriIndices()) Search.addKnowledgeMiner(IndriKM(indriIndices, false))
        for (indriServers in IndriKM.getIndriServers()) Search.addKnowledgeMiner(IndriKM(indriServers, true))
        // - knowledge annotators for (semi-)structured knowledge sources
        Search.clearKnowledgeAnnotators()

        // answer extraction and selection
        // (the filters are applied in this order)
        AnswerSelection.clearFilters()
        // - answer extraction filters
        AnswerSelection.addFilter(AnswerTypeFilter())
        AnswerSelection.addFilter(AnswerPatternFilter())
        AnswerSelection.addFilter(WebDocumentFetcherFilter())
        AnswerSelection.addFilter(PredicateExtractionFilter())
        AnswerSelection.addFilter(FactoidsFromPredicatesFilter())
        AnswerSelection.addFilter(TruncationFilter())
        // - answer selection filters
    }

    /**
     * Initializes the pipeline for factoid questions, using the Web as a
     * knowledge source.
     *
     * @param resultsCorp results retrieved from the corpus
     */
    protected fun initFactoidWeb(resultsCorp: Array<Result?>?) {
        // question analysis
        val wordNet: Ontology = WordNet()
        // - dictionaries for term extraction
        QuestionAnalysis.clearDictionaries()
        QuestionAnalysis.addDictionary(wordNet)
        // - ontologies for term expansion
        QuestionAnalysis.clearOntologies()
        QuestionAnalysis.addOntology(wordNet)

        // query generation
        QueryGeneration.clearQueryGenerators()
        QueryGeneration.addQueryGenerator(BagOfWordsG())
        QueryGeneration.addQueryGenerator(BagOfTermsG())
        QueryGeneration.addQueryGenerator(PredicateG())
        QueryGeneration.addQueryGenerator(QuestionInterpretationG())
        QueryGeneration.addQueryGenerator(QuestionReformulationG())

        // search
        // - knowledge miners for unstructured knowledge sources
        Search.clearKnowledgeMiners()
        Search.addKnowledgeMiner(BingKM())
        //		Search.addKnowledgeMiner(new GoogleKM());
//		Search.addKnowledgeMiner(new YahooKM());
        // - knowledge annotators for (semi-)structured knowledge sources
        Search.clearKnowledgeAnnotators()

        // answer extraction and selection
        // (the filters are applied in this order)
        AnswerSelection.clearFilters()
        // - answer extraction filters
        AnswerSelection.addFilter(AnswerTypeFilter())
        AnswerSelection.addFilter(AnswerPatternFilter())
        AnswerSelection.addFilter(WebDocumentFetcherFilter())
        AnswerSelection.addFilter(PredicateExtractionFilter())
        AnswerSelection.addFilter(FactoidsFromPredicatesFilter())
        AnswerSelection.addFilter(TruncationFilter())
        // - answer selection filters
        AnswerSelection.addFilter(StopwordFilter())
        AnswerSelection.addFilter(QuestionKeywordsFilter())
        AnswerSelection.addFilter(AnswerProjectionFilter(resultsCorp))
        AnswerSelection.addFilter(ScoreNormalizationFilter(NORMALIZER))
        AnswerSelection.addFilter(ScoreCombinationFilter())
        AnswerSelection.addFilter(FactoidSubsetFilter())
        AnswerSelection.addFilter(DuplicateFilter())
        AnswerSelection.addFilter(ScoreSorterFilter())
        AnswerSelection.addFilter(ResultLengthFilter())
    }

    /**
     * Asks Ephyra a factoid question and returns up to `maxAnswers`
     * results that have a score of at least `absThresh`. This method
     * is optimized for the TREC evaluation: if the answer type cannot be
     * determined and no answers are found, it simply returns a list of proper
     * names.
     *
     * @param question factoid question
     * @param maxAnswers maximum number of answers
     * @param absThresh absolute threshold for scores
     * @return array of results
     */
    override fun askFactoid(
        question: String?, maxAnswers: Int,
        absThresh: Float
    ): Array<Result?> {
        // initialize pipeline
        initFactoidCorpus()

        // analyze question
        MsgPrinter.printAnalyzingQuestion()
        val aq = QuestionAnalysis.analyze(question)

        // get corpus answers
        var resultsCorp = runPipeline(aq, Int.MAX_VALUE, Float.NEGATIVE_INFINITY)

        // get web answers and project them
        initFactoidWeb(resultsCorp)
        var results = runPipeline(aq, maxAnswers, absThresh)

        // return results if any
        if (results.size > 0) return results
        if (aq.answerTypes.size == 0) {
            // assume that question asks for a proper name
            aq.answerTypes = arrayOf("NEproperName")

            // get corpus answers (only factoid answers)
            initFactoidCorpus()
            resultsCorp = runPipeline(aq, Int.MAX_VALUE, 0f)

            // get web answers and project them
            initFactoidWeb(resultsCorp)
            results = runPipeline(aq, maxAnswers, absThresh)
        }
        return results
    }

    /**
     * Asks Ephyra a list question and returns results that have a score of at
     * least `relThresh * top score`. This method is optimized for
     * the TREC evaluation: if no answers are found, it simply returns a list
     * of proper names.
     *
     * @param question list question
     * @param relThresh relative threshold for scores
     * @return array of results
     */
    override fun askList(question: String?, relThresh: Float): Array<Result?>? {
        var question = question
        question = QuestionNormalizer.transformList(question)
        var results = askFactoid(question, Int.MAX_VALUE, 0f)
        if (results.size == 0) {
            // assume that question asks for proper names
            val aq = QuestionAnalysis.analyze(question)
            aq.answerTypes = arrayOf("NEproperName")

            // get corpus answers (only factoid answers)
            initFactoidCorpus()
            val resultsCorp = runPipeline(aq, Int.MAX_VALUE, 0f)

            // get web answers and project them
            initFactoidWeb(resultsCorp)
            results = runPipeline(aq, Int.MAX_VALUE, 0f)
        }

        // get results with a score of at least relThresh * top score
        val confident = ArrayList<Result?>()
        if (results.size > 0) {
            val topScore = results[0]!!.score
            for (result in results) if (result!!.score >= relThresh * topScore) confident.add(result)
        }
        return confident.toTypedArray()
    }

    companion object {
        /**
         * Entry point of Ephyra. Initializes the engine and starts the command line
         * interface.
         *
         * @param args command line arguments are ignored
         */
        @JvmStatic
        fun main(args: Array<String>) {
            // enable output of status and error messages
            MsgPrinter.enableStatusMsgs(true)
            MsgPrinter.enableErrorMsgs(true)

            // set log file and enable logging
            Logger.setLogfile("log/OpenEphyraCorpus")
            Logger.enableLogging(true)

            // initialize Ephyra and start command line interface
            OpenEphyraCorpus().commandLine()
        }
    }
}
*/