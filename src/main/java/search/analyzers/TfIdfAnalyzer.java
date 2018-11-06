package search.analyzers;

import datastructures.concrete.ChainedHashSet;
import datastructures.concrete.KVPair;
import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IList;
import datastructures.interfaces.ISet;
import search.models.Webpage;

import java.net.URI;

/**
 * This class is responsible for computing how "relevant" any given document is
 * to a given search query.
 *
 * See the spec for more details.
 */
public class TfIdfAnalyzer {
    // This field must contain the IDF score for every single word in all
    // the documents.
    private IDictionary<String, Double> idfScores;

    // This field must contain the TF-IDF vector for each webpage you were given
    // in the constructor.
    //
    // We will use each webpage's page URI as a unique key.
    private IDictionary<URI, IDictionary<String, Double>> documentTfIdfVectors;

    // Feel free to add extra fields and helper methods.

    public TfIdfAnalyzer(ISet<Webpage> webpages) {
        // Implementation note: We have commented these method calls out so your
        // search engine doesn't immediately crash when you try running it for the
        // first time.
        //
        // You should uncomment these lines when you're ready to begin working
        // on this class.

        this.idfScores = new ChainedHashDictionary<String, Double>();
        this.documentTfIdfVectors = this.computeAllDocumentTfIdfVectors(webpages);
    }

    // Note: this method, strictly speaking, doesn't need to exist. However,
    // we've included it so we can add some unit tests to help verify that your
    // constructor correctly initializes your fields.
    public IDictionary<URI, IDictionary<String, Double>> getDocumentTfIdfVectors() {
        return this.documentTfIdfVectors;
    }

    // Note: these private methods are suggestions or hints on how to structure your
    // code. However, since they're private, you're not obligated to implement exactly
    // these methods: feel free to change or modify these methods however you want. The
    // important thing is that your 'computeRelevance' method ultimately returns the
    // correct answer in an efficient manner.

    /**
     * Return a dictionary mapping every single unique word found
     * in every single document to their IDF score.
     */
    //    private IDictionary<String, Double> computeIdfScores(ISet<Webpage> pages) {
    //        IDictionary<String, Double> output = new ChainedHashDictionary<String, Double>();
    //        for (Webpage page: pages) {
    //            ISet<String> reference = new ChainedHashSet<String>();
    //            for (String term: page.getWords()) {
    //                if (!output.containsKey(term)) {
    //                    output.put(term, 1.0);
    //                }
    //                if (!reference.contains(term)) {
    //                    output.put(term, output.get(term) + 1.0);
    //                    reference.add(term);
    //                }
    //            }
    //        }
    //        for (KVPair<String, Double> pair: output) {
    //            output.put(pair.getKey(), Math.log(pair.getValue() / pages.size()));
    //        }
    //        return output;
    //    }

    /**
     * Returns a dictionary mapping every unique word found in the given list
     * to their term frequency (TF) score.
     *
     * The input list represents the words contained within a single document.
     */
    private IDictionary<String, Double> computeTfScores(IList<String> words) {
        IDictionary<String, Double> output = new ChainedHashDictionary<String, Double>();
        for (String term: words) {
            if (!output.containsKey(term)) {
                output.put(term, 0.0);
            }
            output.put(term, output.get(term) + 1.0 / words.size());
        }
        return output;
    }

    /**
     * See spec for more details on what this method should do.
     */
    private IDictionary<URI, IDictionary<String, Double>> computeAllDocumentTfIdfVectors(ISet<Webpage> pages) {
        // Hint: this method should use the idfScores field and
        // call the computeTfScores(...) method.
        IDictionary<String, Double> idf = new ChainedHashDictionary<String, Double>();
        IDictionary<URI, IDictionary<String, Double>> tf = makeDictionary();
        for (Webpage page: pages) {
            ISet<String> reference = new ChainedHashSet<String>();
            IDictionary<String, Double> tfVal = new ChainedHashDictionary<String, Double>();
            for (String term: page.getWords()) {
                if (!reference.contains(term)) {
                    if (!idf.containsKey(term)) {
                        idf.put(term, 1.0);
                    } else {
                        idf.put(term, idf.get(term) + 1.0);
                    }
                    reference.add(term);
                }
                if (!tfVal.containsKey(term)) {
                    tfVal.put(term, 0.0);
                } 
                tfVal.put(term, tfVal.get(term) + 1.0 / page.getWords().size());
            }
            tf.put(page.getUri(), tfVal);
        }
        IDictionary<URI, IDictionary<String, Double>> output = makeDictionary();
        for (KVPair<URI, IDictionary<String, Double>> pair: tf) {
            IDictionary<String, Double> temp = new ChainedHashDictionary<String, Double>();
            for (KVPair<String, Double> val: pair.getValue()) {
                String key = val.getKey();
                double idfVal = Math.log(pages.size() / idf.get(key));
                temp.put(key, val.getValue() * idfVal);
                idfScores.put(key, idfVal);
            }
            output.put(pair.getKey(), temp);
        }
        return output;
    }
    
    private IDictionary<URI, IDictionary<String, Double>> makeDictionary() {
        return new ChainedHashDictionary<URI, IDictionary<String, Double>>();
    }

    /**
     * Returns the cosine similarity between the TF-IDF vector for the given query and the
     * URI's document.
     *
     * Precondition: the given uri must have been one of the uris within the list of
     *               webpages given to the constructor.
     */
    public Double computeRelevance(IList<String> query, URI pageUri) {
        // Note: The pseudocode we gave you is not very efficient. When implementing,
        // this method, you should:
        //
        // 1. Figure out what information can be precomputed in your constructor.
        //    Add a third field containing that information.
        //
        // 2. See if you can combine or merge one or more loops.
        IDictionary<String, Double> docVec = documentTfIdfVectors.get(pageUri);
        IDictionary<String, Double> queryVec = new ChainedHashDictionary<String, Double>();
        double numerator = 0.0;
        for (KVPair<String, Double> pair: computeTfScores(query)) {
            String key = pair.getKey();
            double queryWordScore = idfScores.get(key) * pair.getValue();
            queryVec.put(key, queryWordScore);
            if (docVec.containsKey(key)) {
                numerator += docVec.get(key) * queryWordScore;
            }
        }
        double denominator = norm(docVec) * norm(queryVec);
        return denominator == 0.0 ? 0.0 : numerator / denominator;
    }
    
    private double norm(IDictionary<String, Double> vector) {
        double output = 0.0;
        for (KVPair<String, Double> pair: vector) {
            output += Math.pow(pair.getValue(), 2.0);
        }
        return Math.sqrt(output);
    }
                
}
