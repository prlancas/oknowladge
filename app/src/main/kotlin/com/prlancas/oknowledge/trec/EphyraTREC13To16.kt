package com.prlancas.oknowledge.trec

/*package com.prlancas.oknowledge

import info.ephyra.querygeneration.QueryGeneration
import info.ephyra.search.Search
import info.ephyra.search.searchers.IndriKM
import info.ephyra.answerselection.AnswerSelection
import info.ephyra.answerselection.filters.ScoreResetterFilter
import info.ephyra.answerselection.filters.SentenceExtractionFilter
import info.ephyra.answerselection.filters.CutKeywordsFilter
import info.ephyra.answerselection.filters.CutStatementProviderFilter
import info.ephyra.answerselection.filters.SentenceSplitterFilter
import info.ephyra.answerselection.filters.DuplicateSnippetFilter
import info.ephyra.answerselection.filters.ProperNameFilter
import info.ephyra.answerselection.filters.DirectSpeechFilter
import info.ephyra.answerselection.filters.TermFilter
import info.ephyra.answerselection.filters.WikipediaGoogleTermImportanceFilter
import info.ephyra.answerselection.filters.WebTermImportanceFilter
import info.ephyra.answerselection.filters.ScoreSorterFilter
import info.ephyra.answerselection.filters.ResultLengthFilter
import com.prlancas.oknowledge.io.MsgPrinter
import info.ephyra.questionanalysis.QuestionNormalizer
import info.ephyra.questionanalysis.KeywordExtractor
import info.ephyra.questionanalysis.AnalyzedQuestion
import info.ephyra.querygeneration.generators.BagOfWordsG
import info.ephyra.trec.TRECTarget
import info.ephyra.nlp.NETagger
import info.ephyra.trec.TRECAnswer
import info.ephyra.answerselection.definitional.Dossier
import info.ephyra.questionanalysis.QuestionInterpreter
import info.ephyra.answerselection.filters.NuggetEvaluationFilter
import info.ephyra.trec.TRECPattern
import info.ephyra.trec.TargetPreprocessor
import info.ephyra.questionanalysis.QuestionAnalysis
import info.ephyra.trec.CorefResolver
import info.ephyra.answerselection.filters.OverlapAnalysisFilter
import com.prlancas.oknowledge.io.Logger
import info.ephyra.search.Result
import info.ephyra.trec.TREC13To16Parser
import java.util.*
import java.util.regex.Pattern
import kotlin.jvm.JvmStatic

/**
 *
 * Runs and evaluates Ephyra on the data from the TREC 13-16 QA tracks.
 *
 *
 * This class extends `OpenEphyraCorpus`.
 *
 * @author Nico Schlaefer
 * @version 2008-03-23
 */
class EphyraTREC13To16 : OpenEphyraCorpus() {
    // Layout 1
    /**
     * Initializes the pipeline for 'other' questions.
     */
    protected fun initOther() {
        // query generation
        QueryGeneration.clearQueryGenerators()

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

        //	initialize scores
        AnswerSelection.addFilter(ScoreResetterFilter())

        //	extract sentences from snippets
        AnswerSelection.addFilter(SentenceExtractionFilter())

        //	cut meaningless introductions from sentences
        AnswerSelection.addFilter(CutKeywordsFilter())
        AnswerSelection.addFilter(CutStatementProviderFilter())
        AnswerSelection.addFilter(SentenceSplitterFilter())
        AnswerSelection.addFilter(CutKeywordsFilter())

        //	remove duplicates
        AnswerSelection.addFilter(DuplicateSnippetFilter())

        //	throw out enumerations of proper names
        AnswerSelection.addFilter(ProperNameFilter())

        //	throw out direct speech snippets, rarely contain useful information
        AnswerSelection.addFilter(DirectSpeechFilter())

        //	sort out snippets containing no new terms
        AnswerSelection.addFilter(TermFilter())
        AnswerSelection.addFilter(
            WikipediaGoogleTermImportanceFilter(
                WebTermImportanceFilter.LOG_LENGTH_NORMALIZATION,
                WebTermImportanceFilter.LOG_LENGTH_NORMALIZATION,
                false
            )
        )
        AnswerSelection.addFilter(ScoreSorterFilter())

        //	cut off result
        AnswerSelection.addFilter(ResultLengthFilter(3000))
    }
    // Layout 2
    //	/**
    //	 * Initializes the pipeline for 'other' questions.
    //	 */
    //	protected void initOther() {
    //		// query generation
    //		QueryGeneration.clearQueryGenerators();
    //		
    //		// search
    //		// - knowledge miners for unstructured knowledge sources
    //		Search.clearKnowledgeMiners();
    //		for (String[] indriIndices : IndriKM.getIndriIndices())
    //			Search.addKnowledgeMiner(new IndriKM(indriIndices, false));
    //		for (String[] indriServers : IndriKM.getIndriServers())
    //			Search.addKnowledgeMiner(new IndriKM(indriServers, true));
    //		// - knowledge annotators for (semi-)structured knowledge sources
    //		Search.clearKnowledgeAnnotators();
    //		
    //		// answer extraction and selection
    //		// (the filters are applied in this order)
    //		AnswerSelection.clearFilters();
    //		
    //		//	initialize scores
    //		AnswerSelection.addFilter(new ScoreResetterFilter());
    //		
    //		//	extract sentences from snippets
    //		AnswerSelection.addFilter(new SentenceExtractionFilter());
    //		
    //		//	cut meaningless introductions from sentences
    //		AnswerSelection.addFilter(new CutKeywordsFilter());
    //		AnswerSelection.addFilter(new CutStatementProviderFilter());
    //		AnswerSelection.addFilter(new SentenceSplitterFilter());
    //		AnswerSelection.addFilter(new CutKeywordsFilter());
    //		
    //		//	remove duplicates
    //		AnswerSelection.addFilter(new DuplicateSnippetFilter());
    //		
    //		//	throw out enumerations of proper names
    //		AnswerSelection.addFilter(new ProperNameFilter());
    //		
    //		//	throw out direct speech snippets, rarely contain useful information
    //		AnswerSelection.addFilter(new DirectSpeechFilter());
    //		
    //		AnswerSelection.addFilter(
    //				new WikipediaGoogleWebTermImportanceFilter(
    //					WebTermImportanceFilter.LOG_LENGTH_NORMALIZATION,
    //					WebTermImportanceFilter.LOG_LENGTH_NORMALIZATION,
    //					false
    //				)
    //			);
    //		AnswerSelection.addFilter(new ScoreSorterFilter());
    //		
    //		//	cut off result
    //		AnswerSelection.addFilter(new ResultLengthFilter(3000));
    //	}
    // Layout 3
    //	/**
    //	 * Initializes the pipeline for 'other' questions.
    //	 */
    //	protected void initOther() {
    //		// query generation
    //		QueryGeneration.clearQueryGenerators();
    //		
    //		// search
    //		// - knowledge miners for unstructured knowledge sources
    //		Search.clearKnowledgeMiners();
    //		for (String[] indriIndices : IndriKM.getIndriIndices())
    //			Search.addKnowledgeMiner(new IndriDocumentKM(indriIndices, false));
    //		for (String[] indriServers : IndriKM.getIndriServers())
    //			Search.addKnowledgeMiner(new IndriDocumentKM(indriServers, true));
    //		// - knowledge annotators for (semi-)structured knowledge sources
    //		Search.clearKnowledgeAnnotators();
    //		
    //		// answer extraction and selection
    //		// (the filters are applied in this order)
    //		AnswerSelection.clearFilters();
    //		
    //		//	initialize scores
    //		AnswerSelection.addFilter(new ScoreResetterFilter());
    //		
    //		//	extract sentences from snippets
    //		AnswerSelection.addFilter(new SentenceExtractionFilter());
    //		
    //		//	cut meaningless introductions from sentences
    //		AnswerSelection.addFilter(new CutKeywordsFilter());
    //		AnswerSelection.addFilter(new CutStatementProviderFilter());
    //		AnswerSelection.addFilter(new SentenceSplitterFilter());
    //		AnswerSelection.addFilter(new CutKeywordsFilter());
    //		
    //		//	remove duplicates
    //		AnswerSelection.addFilter(new DuplicateSnippetFilter());
    //		
    //		//	throw out enumerations of proper names
    //		AnswerSelection.addFilter(new ProperNameFilter());
    //		
    //		//	throw out direct speech snippets, rarely contain useful information
    //		AnswerSelection.addFilter(new DirectSpeechFilter());
    //		
    //		//	sort out snippets containing no new terms
    //		AnswerSelection.addFilter(new TermFilter());
    //		
    //		AnswerSelection.addFilter(
    //				new WikipediaGoogleWebTermImportanceFilter(
    //					WebTermImportanceFilter.LOG_LENGTH_NORMALIZATION,
    //					WebTermImportanceFilter.LOG_LENGTH_NORMALIZATION,
    //					false
    //				)
    //			);
    //		AnswerSelection.addFilter(new ScoreSorterFilter());
    //		
    //		//	cut off result
    //		AnswerSelection.addFilter(new ResultLengthFilter(3000));
    //	}
    /**
     * Asks Ephyra an 'other' question.
     *
     * @param question other question
     * @return array of results
     */
    fun askOther(question: String?): Array<Result?> {
        // initialize pipeline
        initOther()

        // query generation
        MsgPrinter.printGeneratingQueries()
        val qn = QuestionNormalizer.normalize(question)
        MsgPrinter.printNormalization(qn) // print normalized question string
        Logger.logNormalization(qn) // log normalized question string
        val kws = KeywordExtractor.getKeywords(qn)
        val aq = AnalyzedQuestion(question)
        aq.keywords = kws
        aq.isFactoid = false
        val gen = BagOfWordsG()
        val queries = gen.generateQueries(aq)
        for (q in queries.indices) queries[q].originalQueryString = question
        MsgPrinter.printQueryStrings(queries) // print query strings
        Logger.logQueryStrings(queries) // log query strings

        // search
        MsgPrinter.printSearching()
        var results = Search.doSearch(queries)

        // answer selection
        MsgPrinter.printSelectingAnswers()
        results = AnswerSelection.getResults(results, Int.MAX_VALUE, 0f)
        return results
    }

    /**
     * Asks Ephyra an 'other' question, making use of the target description and
     * previous questions and answers.
     *
     * @param target the target the 'other' question is about
     * @return array of results
     */
    fun askOther(target: TRECTarget): Array<Result?> {
        //	get target type from interpretations of factoid/list questions
        val factoidQuestions = target.questions
        val props = ArrayList<String>()
        val vals = ArrayList<String>()
        val sentences = ArrayList<String>()
        val targetTokens = NETagger.tokenize(target.targetDesc)
        for (tt in targetTokens) sentences.add(tt)

        //	collect properties and answers from FACTOID and LIST questions
        for (fq in factoidQuestions) {
            val qi = fq.interpretation
            if (qi != null) {
                val prop = qi.property
                val answers = fq.answers
                if (answers.size != 0) {

                    //	collect property/value pair
                    val `val` = answers[0].answerString
                    props.add(prop)
                    vals.add(`val`)
                    //					MsgPrinter.printStatusMsg("Dossier on '" + target.getTargetDesc() + "' contains: '" + prop + "' is '" + val + "'");

                    //	remember answer sentence for previous results
                    val questionTokens = NETagger.tokenize(fq.questionString)
                    for (qt in questionTokens) sentences.add(qt)
                }
            }
        }

        //	filter out results that bring no new terms but ones contained in the target, a previous question, or an answert to a previous question
        TermFilter.setPreviousResultsTerms(sentences.toTypedArray())

        //	initialize Dossier
//		Dossier dossier = Dossier.getDossier(target.getTargetDesc(), target.getTargetType(), props.toArray(new String[props.size()]), vals.toArray(new String[vals.size()]));
        val dossier = Dossier.getDossier(target.targetDesc, null, props.toTypedArray(), vals.toTypedArray())
        //		MsgPrinter.printStatusMsg("Target type of '" + target.getTargetDesc() + "' is " + dossier.getTargetType());
        val rawResults = ArrayList<Result?>()

        //	collect missing properties
        val missingProps = dossier.missingPropertyNames
        for (mp in missingProps) {

            //	generate FACTOID question from template
            val question = QuestionInterpreter.getQuestion(target.targetDesc, mp)

            //	if valid template exists, ask FACTOID question
            if (question != null) {
//				MsgPrinter.printStatusMsg("Building Dossier on '" + target.getTargetDesc() + "', would ask this question now: '" + question + "'");
//				Logger.enableLogging(false);
//				Result res = this.askFactoid(question);
//				Logger.enableLogging(true);
//				
//				//	if question could be answered, add new property and value to dossier
//				if (res != null) {
//					dossier.setProperty(mp, res.getAnswer());
//					MsgPrinter.printStatusMsg("Dossier on '" + target.getTargetDesc() + "' extended: '" + mp + "' set to '" + res.getAnswer() + "'");
//					rawResults.add(res);
//					String sentence = res.getSentence();
//					
//					//	get supporting sentence of answer and, if existing, remember it as nugget
//					if (sentence != null) {
//						Result newRes = new Result(sentence, res.getQuery(), res.getDocID(), res.getHitPos());
//						newRes.setScore(res.getScore() + 2);
//						rawResults.add(newRes);
//					}
//				}
            }
        }
        NuggetEvaluationFilter.setTargetID(target.id)

        //	collect BagOfWords results for target
        var nuggets = askOther(target.targetDesc)
        for (r in nuggets) rawResults.add(r)
        nuggets = rawResults.toTypedArray()
        NuggetEvaluationFilter.targetFinished()

        //	reset term filter
        TermFilter.setPreviousResultsTerms(null)
        NuggetEvaluationFilter.setTargetID(null)
        return nuggets
    }

    companion object {
        /** Maximum number of factoid answers.  */
        protected const val FACTOID_MAX_ANSWERS = 1

        /** Absolute threshold for factoid answer scores.  */
        protected const val FACTOID_ABS_THRESH = 0f

        /** Relative threshold for list answer scores (fraction of top score).  */
        protected const val LIST_REL_THRESH = 0.1f

        /** Target objects containing the TREC questions.  */
        private lateinit var targets: Array<TRECTarget>

        /** Tag that uniquely identifies the run (also used as output file name).  */
        private var runTag: String? = null

        /** Log file for the results returned by Ephyra.  */
        private var logFile: String? = null

        /** Log file used as a source for answers to some of the questions.  */
        private var inputLogFile: String? = null

        /** Load answers to factoid questions from log file?  */
        private var factoidLog = false

        /** Load answers to list questions from log file?  */
        private var listLog = false

        /** Load answers to "Other" questions from log file?  */
        private var otherLog = false

        /** Patterns for factoid questions (optional).  */
        private var factoidPatterns: Array<TRECPattern>?

        /** Patterns for list questions (optional).  */
        private var listPatterns: Array<TRECPattern>?

        /** Scores for the factoid questions within a target.  */
        private var factoidQuestionScores = ArrayList<Float>()

        /** Scores for the list questions within a target.  */
        private var listQuestionScores = ArrayList<Float>()

        /** Factoid scores for the targets.  */
        private var factoidTargetScores = ArrayList<Float>()

        /** List scores for the targets.  */
        private var listTargetScores = ArrayList<Float>()

        /**
         * Calculates the score for a single factoid question.
         *
         * @param qid ID of the question
         * @param results the results from Ephyra
         * @param absThresh absolute confidence threshold for results
         * @return for each answer a flag that is true iff the answer is correct
         */
        private fun evalFactoidQuestion(
            qid: String, results: Array<Result?>?,
            absThresh: Float
        ): BooleanArray {
            // get pattern if available
            var results = results
            var pattern: TRECPattern? = null
            for (factoidPattern in factoidPatterns!!) if (factoidPattern.id == qid) {
                pattern = factoidPattern
                break
            }

            // drop result if its score does not satisfy the threshold
            if (results!!.size > 0 && results[0]!!.score < absThresh) results = arrayOfNulls(0)

            // evaluate result with pattern
            return if (results.size > 0 && pattern != null) {
                val firstAnswer = results[0]!!.answer
                for (regex in pattern.regexs) if (firstAnswer.matches(".*?$regex.*+")) {
                    // answer correct if it matches one of the patterns
                    factoidQuestionScores.add(1f)
                    return booleanArrayOf(true)
                }

                // answer wrong if it does not match one of the patterns
                factoidQuestionScores.add(0f)
                booleanArrayOf(false)
            } else if (results.size == 0 && pattern == null) {
                // answer correct if neither result nor pattern available
                factoidQuestionScores.add(1f)
                booleanArrayOf(true)
            } else {
                // answer wrong if either result or pattern available
                factoidQuestionScores.add(0f)
                booleanArrayOf(false)
            }
        }

        /**
         * Calculates the score for a single list question.
         *
         * @param qid ID of the question
         * @param results the results from Ephyra
         * @param relThresh relative confidence threshold for results
         * @return for each answer a flag that is true iff the answer is correct
         */
        private fun evalListQuestion(
            qid: String, results: Array<Result?>?,
            relThresh: Float
        ): BooleanArray {
            // get pattern
            var pattern: TRECPattern? = null
            for (listPattern in listPatterns!!) if (listPattern.id == qid) {
                pattern = listPattern
                break
            }
            if (pattern == null) return BooleanArray(0) // pattern not available

            // get results with a score of at least relThresh * top score
            val resultList = ArrayList<Result?>()
            if (results!!.size > 0) {
                val topScore = results[0]!!.score
                for (result in results) if (result!!.score >= relThresh * topScore) resultList.add(result)
            }
            var f = 0f // F measure
            val correct = BooleanArray(resultList.size) // correct results
            if (resultList.size > 0) {
                val regexs = pattern.regexs
                val total = regexs.size // total number of known answers
                val returned = resultList.size // number of returned results
                var covered = 0 // number of answers covered by the results
                for (regex in regexs) {
                    var found = false
                    for (i in resultList.indices) {
                        val answer = resultList[i]!!.answer
                        if (answer.matches(".*?$regex.*+")) {
                            if (!found) {
                                covered++
                                found = true
                            }
                            correct[i] = true
                        }
                    }
                }
                if (covered > 0) {
                    val recall = covered.toFloat() / total
                    val precision = covered.toFloat() / returned
                    f = 2 * recall * precision / (recall + precision)
                }
            }
            listQuestionScores.add(f)
            return correct
        }

        /**
         * Calculates the factoid score for the current target.
         *
         * @return the score or `-1` if there are no factoid questions
         */
        private fun evalFactoidTarget(): Float {
            // sum of factoid question scores
            var sum = 0f
            for (score in factoidQuestionScores) sum += score
            // number of factoid questions
            val num = factoidQuestionScores.size
            if (num == 0) return (-1).toFloat()
            // factoid score for the target
            val score = sum / num

            // clear scores
            factoidQuestionScores = ArrayList()
            factoidTargetScores.add(score)
            return score
        }

        /**
         * Calculates the list score for the current target.
         *
         * @return the score or `-1` if there are no list questions
         */
        private fun evalListTarget(): Float {
            // sum of list question scores
            var sum = 0f
            for (score in listQuestionScores) sum += score
            // number of list questions
            val num = listQuestionScores.size
            if (num == 0) return (-1).toFloat()
            // list score for the target
            val score = sum / num

            // clear scores
            listQuestionScores = ArrayList()
            listTargetScores.add(score)
            return score
        }

        /**
         * Calculates the total score for the factoid component and logs the score.
         *
         * @param absThresh absolute confidence threshold for results
         * @return the score or `-1` if there are no factoid questions
         */
        private fun evalFactoidTotal(absThresh: Float): Float {
            // sum of factoid target scores
            var sum = 0f
            for (score in factoidTargetScores) sum += score
            // number of targets with factoid questions
            val num = factoidTargetScores.size
            if (num == 0) return (-1).toFloat()
            // score for the factoid component
            val score = sum / num

            // clear scores
            factoidTargetScores = ArrayList()

            // log score for factoid component
            Logger.logFactoidScore(score, absThresh)
            return score
        }

        /**
         * Calculates the total score for the list component and logs the score.
         *
         * @param relThresh relative confidence threshold for results
         * @return the score or `-1` if there are no list questions
         */
        private fun evalListTotal(relThresh: Float): Float {
            // sum of list target scores
            var sum = 0f
            for (score in listTargetScores) sum += score
            // number of targets with list questions
            val num = listTargetScores.size
            if (num == 0) return (-1).toFloat()
            // score for the list component
            val score = sum / num

            // clear scores
            listTargetScores = ArrayList()

            // log score for list component
            Logger.logListScore(score, relThresh)
            return score
        }

        /**
         * Initializes Ephyra, asks the questions or loads the answers from a log
         * file, evaluates the answers if patterns are available and logs and saves
         * the answers.
         */
        private fun runAndEval() {
            // initialize Ephyra
            val ephyra = EphyraTREC13To16()

            // evaluate for multiple thresholds
            var firstThreshold = true
            //		for (float fAbsThresh = FACTOID_ABS_THRESH;
//			 fAbsThresh <= 1; fAbsThresh += 0.01) {
            val fAbsThresh = FACTOID_ABS_THRESH
            //		for (float lRelThresh = LIST_REL_THRESH;
//			 lRelThresh <= 1; lRelThresh += 0.01) {
            val lRelThresh = LIST_REL_THRESH
            for (target in targets) {
                MsgPrinter.printTarget(target.targetDesc)

                // normalize target description, determine target types
                if (firstThreshold) TargetPreprocessor.preprocess(target)
                val targetDesc = target.targetDesc
                val condensedTarget = target.condensedTarget
                val questions = target.questions

                // condensed target is used as contextual information
                QuestionAnalysis.setContext(condensedTarget)
                for (i in questions.indices) {
                    MsgPrinter.printQuestion(questions[i].questionString)
                    val id = questions[i].id
                    val type = questions[i].type
                    var qs: String?
                    qs = if (type == "FACTOID" || type == "LIST") {
                        // resolve coreferences in factoid and list questions
                        if (firstThreshold) {
                            MsgPrinter.printResolvingCoreferences()
                            CorefResolver.resolvePronounsToTarget(target, i)
                        }
                        questions[i].questionString
                    } else {
                        targetDesc
                    }

                    // set pattern used to evaluate answers for overlap analysis
                    OverlapAnalysisFilter.setPattern(null)
                    if (type == "FACTOID") {
                        for (pattern in factoidPatterns!!) {
                            if (pattern.id == id) {
                                OverlapAnalysisFilter.setPattern(pattern)
                                break
                            }
                        }
                    }

                    // ask Ephyra or load answer from log file
                    var results: Array<Result?>? = null
                    if (type == "FACTOID" && factoidLog ||
                        type == "LIST" && listLog ||
                        type == "OTHER" && otherLog
                    ) {
                        results = TREC13To16Parser.loadResults(qs, type, inputLogFile)
                    }
                    if (results == null) { // answer not loaded from log file
                        if (type == "FACTOID") {
                            Logger.logFactoidStart(qs)
                            results = ephyra.askFactoid(
                                qs, FACTOID_MAX_ANSWERS,
                                FACTOID_ABS_THRESH
                            )
                            //						results = new Result[0];
                            Logger.logResults(results)
                            Logger.logFactoidEnd()
                        } else if (type == "LIST") {
                            Logger.logListStart(qs)
                            results = ephyra.askList(qs, LIST_REL_THRESH)
                            //						results = new Result[0];
                            Logger.logResults(results)
                            Logger.logListEnd()
                        } else {
                            Logger.logDefinitionalStart(qs)
                            results = ephyra.askOther(target)
                            //						results = new Result[0];
                            Logger.logResults(results)
                            Logger.logDefinitionalEnd()
                        }
                    }

                    // calculate question score if patterns are available
                    var correct: BooleanArray? = null
                    if (type == "FACTOID" && factoidPatterns != null) correct = evalFactoidQuestion(
                        id,
                        results,
                        fAbsThresh
                    ) else if (type == "LIST" && listPatterns != null) correct =
                        evalListQuestion(id, results, lRelThresh)

                    // update target data structure
                    var answers = arrayOfNulls<TRECAnswer>(results!!.size)
                    for (j in results.indices) {
                        val answer = results[j]!!.answer
                        val supportDoc = results[j]!!.docID
                        answers[j] = TRECAnswer(id, answer, supportDoc)
                    }
                    questions[i].answers = answers
                    if (results.size > 0) {
                        val qi = results[0]!!.query.interpretation
                        if (qi != null) questions[i].interpretation = qi
                    }
                    if (answers.size == 0) {  // no answer found
                        answers = arrayOfNulls(1)
                        if (type == "FACTOID") answers[0] = TRECAnswer(id, null, "NIL") else answers[0] = TRECAnswer(
                            id, "No answers found.",
                            "XIE19960101.0001"
                        )
                    }

                    // save answers to output file
                    TREC13To16Parser.saveAnswers(
                        "log/" + runTag, answers, correct,
                        runTag
                    )
                }

                // calculate target scores if patterns are available
                if (factoidPatterns != null) evalFactoidTarget()
                if (listPatterns != null) evalListTarget()
            }

            // calculate component scores and log scores if patterns are available
            if (factoidPatterns != null) evalFactoidTotal(fAbsThresh)
            if (listPatterns != null) evalListTotal(lRelThresh)
            firstThreshold = false
            //		}
//		}
        }

        /**
         * Runs Ephyra on the TREC questions.
         *
         * @param args argument 1: questionfile<br></br>
         * [argument 2: tag=runtag (uniquely identifies the run, also
         * used as output file name, if not set an
         * unambiguous tag is generated automatically)]<br></br>
         * [argument 3: log=logfile (if not set an unambiguous file name
         * is generated automatically)]<br></br>
         * [argument 4: lin=input_logfile (specifies a separate logfile
         * that is used as a source for answers to some of
         * the	questions, if not set the standard log file
         * is used)]<br></br>
         * [argument 5: lflags=[f][l][o] (answers to these types of
         * questions are loaded from the log file instead
         * of querying Ephyra,	e.g. "flo" for factoid, list
         * and other questions)]<br></br>
         * [argument 6: fp=factoid_patternfile]<br></br>
         * [argument 7: lp=list_patternfile]
         */
        @JvmStatic
        fun main(args: Array<String>) {
            // enable output of status and error messages
            MsgPrinter.enableStatusMsgs(true)
            MsgPrinter.enableErrorMsgs(true)
            if (args.size < 1) {
                MsgPrinter.printUsage(
                    "java EphyraTREC13To16 questionfile " +
                            "[tag=runtag] [log=logfile] " +
                            "[lin=input_logfile] " +
                            "[lflags=[f][l][o]] " +
                            "[fp=factoid_patternfile] " +
                            "[lp=list_patternfile]"
                )
                System.exit(1)
            }

            // load targets
            targets = TREC13To16Parser.loadTargets(args[0])
            for (i in 1 until args.size) if (args[i].matches("tag=.*")) {
                // set run tag
                runTag = args[i].substring(4)
            } else if (args[i].matches("log=.*")) {
                // set log file
                logFile = args[i].substring(4)
            } else if (args[i].matches("lin=.*")) {
                // set separate input log file
                inputLogFile = args[i].substring(4)
            } else if (args[i].matches("lflags=.*")) {
                // answers for some question types are loaded from log file
                val flags = args[i].substring(7).lowercase(Locale.getDefault())
                if (flags.contains("f")) factoidLog = true
                if (flags.contains("l")) listLog = true
                if (flags.contains("o")) otherLog = true
            } else if (args[i].matches("fp=.*")) {
                // load factoid patterns
                factoidPatterns = TREC13To16Parser.loadPatterns(args[i].substring(3))
            } else if (args[i].matches("lp=.*")) {
                // load list patterns
                listPatterns = TREC13To16Parser.loadPatterns(args[i].substring(3))
            }

            // if run tag or log file not set, generate unambiguous names
            if (runTag == null || logFile == null) {
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
                if (runTag == null) runTag = "TREC" + n + "_" + date + "_out"
                if (logFile == null) logFile = "log/TREC" + n + "_" + date
            }

            // if input log file not set, use standard log file
            if (inputLogFile == null) inputLogFile = logFile

            // enable logging
            Logger.setLogfile(logFile)
            Logger.enableLogging(true)

            // run Ephyra on questions, evaluate answers if patterns available
            runAndEval()
        }
    }
}

 */