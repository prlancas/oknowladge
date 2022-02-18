package com.prlancas.oknowledge

import com.prlancas.oknowledge.io.IOInterface
import com.prlancas.oknowledge.io.IOCommandLine
import com.prlancas.oknowledge.io.LegasyErrorReporter
import info.ephyra.nlp.OpenNLP
import info.ephyra.nlp.LingPipe
import info.ephyra.nlp.SnowballStemmer
import info.ephyra.nlp.StanfordParser
import info.ephyra.nlp.NETagger
import info.ephyra.nlp.StanfordNeTagger
import info.ephyra.nlp.semantics.ontologies.WordNet
import info.ephyra.nlp.indices.FunctionWords
import info.ephyra.nlp.indices.Prepositions
import info.ephyra.nlp.indices.IrregularVerbs
import info.ephyra.nlp.indices.WordFrequencies
import info.ephyra.querygeneration.generators.QuestionReformulationG
import info.ephyra.questionanalysis.QuestionInterpreter
import info.ephyra.answerselection.filters.AnswerPatternFilter
import info.ephyra.nlp.semantics.ontologies.Ontology
import info.ephyra.questionanalysis.QuestionAnalysis
import info.ephyra.querygeneration.QueryGeneration
import info.ephyra.querygeneration.generators.BagOfWordsG
import info.ephyra.querygeneration.generators.BagOfTermsG
import info.ephyra.querygeneration.generators.PredicateG
import info.ephyra.querygeneration.generators.QuestionInterpretationG
import info.ephyra.search.Search
import info.ephyra.answerselection.AnswerSelection
import info.ephyra.answerselection.filters.AnswerTypeFilter
import info.ephyra.answerselection.filters.WebDocumentFetcherFilter
import info.ephyra.answerselection.filters.PredicateExtractionFilter
import info.ephyra.answerselection.filters.FactoidsFromPredicatesFilter
import info.ephyra.answerselection.filters.TruncationFilter
import info.ephyra.answerselection.filters.StopwordFilter
import info.ephyra.answerselection.filters.QuestionKeywordsFilter
import info.ephyra.answerselection.filters.ScoreNormalizationFilter
import info.ephyra.answerselection.filters.ScoreCombinationFilter
import info.ephyra.answerselection.filters.FactoidSubsetFilter
import info.ephyra.answerselection.filters.DuplicateFilter
import info.ephyra.answerselection.filters.ScoreSorterFilter
import com.prlancas.oknowledge.io.Logger
import info.ephyra.questionanalysis.AnalyzedQuestion
import info.ephyra.questionanalysis.QuestionNormalizer
import info.ephyra.search.Result
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.Exception

open class OpenKnowledge(
    val dir: String = "",
    val logger: Logger = Logger(),
    val ioInterface: IOInterface = IOCommandLine()

) {

    init {
        LegasyErrorReporter.ioInterface = ioInterface;
        LegasyErrorReporter.logger = logger;
        ioInterface.initialising()

        // create tokenizer
        ioInterface.statusMsg("Creating tokenizer...")
        if (!OpenNLP.createTokenizer(
                dir +
                        "res/nlp/tokenizer/opennlp/EnglishTok.bin.gz"
            )
        ) ioInterface.errorMsg("Could not create tokenizer.")
        //		LingPipe.createTokenizer();

        // create sentence detector
        ioInterface.statusMsg("Creating sentence detector...")
        if (!OpenNLP.createSentenceDetector(
                dir +
                        "res/nlp/sentencedetector/opennlp/EnglishSD.bin.gz"
            )
        ) ioInterface.errorMsg("Could not create sentence detector.")
        LingPipe.createSentenceDetector()

        // create stemmer
        ioInterface.statusMsg("Creating stemmer...")
        SnowballStemmer.create()

        // create part of speech tagger
        ioInterface.statusMsg("Creating POS tagger...")
        if (!OpenNLP.createPosTagger(
                dir + "res/nlp/postagger/opennlp/tag.bin.gz",
                dir + "res/nlp/postagger/opennlp/tagdict"
            )
        ) ioInterface.errorMsg("Could not create OpenNLP POS tagger.")
        //		if (!StanfordPosTagger.init(dir + "res/nlp/postagger/stanford/" +
//				"wsj3t0-18-bidirectional/train-wsj-0-18.holder"))
//			msgPrinter.errorMsg("Could not create Stanford POS tagger.");

        // create chunker
        ioInterface.statusMsg("Creating chunker...")
        if (!OpenNLP.createChunker(
                dir +
                        "res/nlp/phrasechunker/opennlp/EnglishChunk.bin.gz"
            )
        ) ioInterface.errorMsg("Could not create chunker.")

        // create syntactic parser
        ioInterface.statusMsg("Creating syntactic parser...")
        //		if (!OpenNLP.createParser(dir + "res/nlp/syntacticparser/opennlp/"))
//			msgPrinter.printErrorMsg("Could not create OpenNLP parser.");
        try {
            StanfordParser.initialize()
        } catch (e: Exception) {
            ioInterface.errorMsg("Could not create Stanford parser.")
        }

        // create named entity taggers
        ioInterface.statusMsg("Creating NE taggers...")
        NETagger.loadListTaggers(dir + "res/nlp/netagger/lists/")
        NETagger.loadRegExTaggers(dir + "res/nlp/netagger/patterns.lst")
        ioInterface.statusMsg("  ...loading models")
        //		if (!NETagger.loadNameFinders(dir + "res/nlp/netagger/opennlp/"))
//			msgPrinter.printErrorMsg("Could not create OpenNLP NE tagger.");
        if (!StanfordNeTagger.isInitialized() && !StanfordNeTagger.init()) ioInterface.errorMsg("Could not create Stanford NE tagger.")
        ioInterface.statusMsg("  ...done")

        // create linker
//		msgPrinter.printStatusMsg("Creating linker...");
//		if (!OpenNLP.createLinker(dir + "res/nlp/corefresolver/opennlp/"))
//			msgPrinter.printErrorMsg("Could not create linker.");

        // create WordNet dictionary
        ioInterface.statusMsg("Creating WordNet dictionary...")
        if (!WordNet.initialize(
                dir +
                        "res/ontologies/wordnet/file_properties.xml"
            )
        ) ioInterface.errorMsg("Could not create WordNet dictionary.")

        // load function words (numbers are excluded)
        ioInterface.statusMsg("Loading function verbs...")
        if (!FunctionWords.loadIndex(
                dir +
                        "res/indices/functionwords_nonumbers"
            )
        ) ioInterface.errorMsg("Could not load function words.")

        // load prepositions
        ioInterface.statusMsg("Loading prepositions...")
        if (!Prepositions.loadIndex(
                dir +
                        "res/indices/prepositions"
            )
        ) ioInterface.errorMsg("Could not load prepositions.")

        // load irregular verbs
        ioInterface.statusMsg("Loading irregular verbs...")
        if (!IrregularVerbs.loadVerbs(dir + "res/indices/irregularverbs")) ioInterface.errorMsg("Could not load irregular verbs.")

        // load word frequencies
        ioInterface.statusMsg("Loading word frequencies...")
        if (!WordFrequencies.loadIndex(dir + "res/indices/wordfrequencies")) ioInterface.errorMsg("Could not load word frequencies.")

        // load query reformulators
        ioInterface.statusMsg("Loading query reformulators...")
        if (!QuestionReformulationG.loadReformulators(
                dir +
                        "res/reformulations/"
            )
        ) ioInterface.errorMsg("Could not load query reformulators.")

        // load answer types
//		msgPrinter.printStatusMsg("Loading answer types...");
//		if (!AnswerTypeTester.loadAnswerTypes(dir +
//				"res/answertypes/patterns/answertypepatterns"))
//			msgPrinter.printErrorMsg("Could not load answer types.");

        // load question patterns
        ioInterface.statusMsg("Loading question patterns...")
        if (!QuestionInterpreter.loadPatterns(
                dir +
                        "res/patternlearning/questionpatterns/"
            )
        ) ioInterface.errorMsg("Could not load question patterns.")

        // load answer patterns
        ioInterface.statusMsg("Loading answer patterns...")
        if (!AnswerPatternFilter.loadPatterns(
                dir +
                        "res/patternlearning/answerpatterns/"
            )
        ) ioInterface.errorMsg("Could not load answer patterns.")
    }

    /**
     * Initializes the pipeline for factoid questions.
     */
    protected fun initFactoid() {
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
        //		Search.addKnowledgeMiner(new BingKM());
//		Search.addKnowledgeMiner(new GoogleKM());
//		Search.addKnowledgeMiner(new YahooKM());
//		for (String[] indriIndices : IndriKM.getIndriIndices())
//			Search.addKnowledgeMiner(new IndriKM(indriIndices, false));
//		for (String[] indriServers : IndriKM.getIndriServers())
//			Search.addKnowledgeMiner(new IndriKM(indriServers, true));
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
        AnswerSelection.addFilter(ScoreNormalizationFilter(NORMALIZER))
        AnswerSelection.addFilter(ScoreCombinationFilter())
        AnswerSelection.addFilter(FactoidSubsetFilter())
        AnswerSelection.addFilter(DuplicateFilter())
        AnswerSelection.addFilter(ScoreSorterFilter())
    }

    /**
     * Runs the pipeline and returns an array of up to `maxAnswers`
     * results that have a score of at least `absThresh`.
     *
     * @param aq analyzed question
     * @param maxAnswers maximum number of answers
     * @param absThresh absolute threshold for scores
     * @return array of results
     */
    protected fun runPipeline(
        aq: AnalyzedQuestion?, maxAnswers: Int,
        absThresh: Float
    ): List<Result> {
        // query generation
        ioInterface.printGeneratingQueries()
        val queries = QueryGeneration.getQueries(aq)

        // search
        ioInterface.printSearching()
        var results = Search.doSearch(queries)

        // answer selection
        ioInterface.printSelectingAnswers()
        results = AnswerSelection.getResults(results, maxAnswers, absThresh)
        return results.asList()
    }

    /**
     *
     * A command line interface for Ephyra.
     *
     *
     * Repeatedly queries the user for a question, asks the system the
     * question and prints out and logs the results.
     *
     *
     * The command `exit` can be used to quit the program.
     */
    fun startChitChat() {
        while (true) {
            // query user for question, quit if user types in "exit"
            ioInterface.questionPrompt()
            var question = ioInterface.getLine().trim { it <= ' ' }
            if (question.equals("exit", ignoreCase = true)) System.exit(0)

            // determine question type and extract question string
            var type: String
            if (question.matches(Regex("(?i)$FACTOID:.*+"))) {
                // factoid question
                type = FACTOID
                question = question.split(":".toRegex(), 2).toTypedArray()[1].trim { it <= ' ' }
            } else if (question.matches(Regex("(?i)$LIST:.*+"))) {
                // list question
                type = LIST
                question = question.split(":".toRegex(), 2).toTypedArray()[1].trim { it <= ' ' }
            } else {
                // question type unspecified
                type = FACTOID // default type
            }

            // ask question
            ioInterface.printAnswers(if (type == FACTOID) {
                askFactoid(
                    question, FACTOID_MAX_ANSWERS,
                    FACTOID_ABS_THRESH
                ).also {
                    logger.logFactoidStart(question)
                    logger.logResults(it)
                    logger.logFactoidEnd()
                }
            } else {
                askList(question, LIST_REL_THRESH).also {
                    logger.logListStart(question)
                    logger.logResults(it)
                    logger.logListEnd()
                }
            })
        }
    }

    /**
     * Asks Ephyra a factoid question and returns up to `maxAnswers`
     * results that have a score of at least `absThresh`.
     *
     * @param question factoid question
     * @param maxAnswers maximum number of answers
     * @param absThresh absolute threshold for scores
     * @return array of results
     */
    open fun askFactoid(
        question: String, maxAnswers: Int,
        absThresh: Float
    ): List<Result> {
        // initialize pipeline
        initFactoid()

        // analyze question
        ioInterface.printAnalyzingQuestion()
        val aq = QuestionAnalysis.analyze(question)

        // get answers
        return runPipeline(aq, maxAnswers, absThresh)
    }

    /**
     * Asks Ephyra a factoid question and returns a single result or
     * `null` if no answer could be found.
     *
     * @param question factoid question
     * @return single result or `null`
     */
    fun askFactoid(question: String) =
        askFactoid(question, 1, 0f).firstOrNull()

    /**
     * Asks Ephyra a list question and returns results that have a score of at
     * least `relThresh * top score`.
     *
     * @param question list question
     * @param relThresh relative threshold for scores
     * @return array of results
     */
    open fun askList(question: String?, relThresh: Float): List<Result> {

        val results = askFactoid(QuestionNormalizer.transformList(question), Int.MAX_VALUE, 0f)

        // get results with a score of at least relThresh * top score
        return if (results.isNotEmpty()) {
            val topScore = results[0].score
            return results.filter { result -> result.score >= relThresh * topScore}
        } else {
            emptyList()
        }
    }

    companion object {
        /** Factoid question type.  */
        protected const val FACTOID = "FACTOID"

        /** List question type.  */
        protected const val LIST = "LIST"

        /** Maximum number of factoid answers.  */
        protected const val FACTOID_MAX_ANSWERS = 1

        /** Absolute threshold for factoid answer scores.  */
        protected const val FACTOID_ABS_THRESH = 0f

        /** Relative threshold for list answer scores (fraction of top score).  */
        protected const val LIST_REL_THRESH = 0.1f

        /** Serialized classifier for score normalization.  */
        const val NORMALIZER = "res/scorenormalization/classifiers/" +
                "AdaBoost70_" +
                "Score+Extractors_" +
                "TREC10+TREC11+TREC12+TREC13+TREC14+TREC15+TREC8+TREC9" +
                ".serialized"

    }
}

fun main() {
    OpenKnowledge().startChitChat()
}