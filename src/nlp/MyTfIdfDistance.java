package nlp;

import com.aliasi.spell.TokenizedDistance;
import com.aliasi.tokenizer.TokenizerFactory;

import com.aliasi.util.Counter;
import com.aliasi.util.ObjectToCounterMap;
import com.aliasi.util.Strings;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * Based on aliasi
 * @author 
 */
public class MyTfIdfDistance extends TokenizedDistance
    implements com.aliasi.corpus.TextHandler {

    private int mDocCount = 0;
    private final ObjectToCounterMap<String> mDocFrequency
        = new ObjectToCounterMap<String>();

    public MyTfIdfDistance(TokenizerFactory tokenizerFactory) {
        super(tokenizerFactory);
    }

    /**
     * Add the specified character sequence as a document for
     * training. The training documents are only used for computing
     * inverse-document frequencies.  The more documents in which
     * a document appears, the less it will be weighted during
     * comparison.
     *
     * @param doc Character sequence to add to training set.
     * @deprecated Use {@link #handle(CharSequence)} instead.
     */
    @Deprecated
    public void trainIdf(CharSequence doc) {
        char[] cs = Strings.toCharArray(doc);
        handle(cs,0,cs.length);
    }

    /**
     * Add the specified character slice as a document for training.
     * This method provides an implementation of the {@link com.aliasi.corpus.TextHandler}
     * interface based on the method {@link #trainIdf(CharSequence)}.
     * See {@link #trainIdf(CharSequence)} for more information.
     *
     * @param cs Underlying character array.
     * @param start Index of first character of document.
     * @param length Number of characters in the document.
     * @throws IndexOutOfBoundsException If the start index
     * is not within the array bounds, or if the start index
     * plus the length minus one is not within the array bounds.
     * @deprecated Use {@link #handle(CharSequence)} instead.
     */
    @Deprecated
    public void handle(char[] cs, int start, int length) {
        for (String token : tokenSet(cs,start,length))
            mDocFrequency.increment(token);
        ++mDocCount;
    }

    /**
     * Add the specified character sequence as a document for training.
     * See {@link #trainIdf(CharSequence)} for more information.
     *
     * @param cSeq Characters to trai.
     */
    public void handle(CharSequence cSeq) {
        char[] cs = Strings.toCharArray(cSeq);
        for (String token : tokenSet(cs,0,cs.length))
            mDocFrequency.increment(token);
        ++mDocCount;

    }

    /**
     * Return the TF/IDF distance between the specified character
     * sequences.  This distance depends on the training instances
     * supplied.  See the class documentation above for more
     * information.
     *
     * @param cSeq1 First character sequence.
     * @param cSeq2 Second character sequence.
     * @return The TF/IDF distance between the two sequences.
     */
    public double distance(CharSequence cSeq1, CharSequence cSeq2) {
        return 1.0 - proximity(cSeq1,cSeq2);
    }

    /**
     * Returns the TF/IDF proximity between the specified character
     * sequences.  The proximity depends on training instances
     * and the tokenizer factory; see the class documentation above
     * for details.
     *
     * @param cSeq1 First character sequence.
     * @param cSeq2 Second character sequence.
     * @return The TF/IDF proximity between the two sequences.
     */
    public double proximity(CharSequence cSeq1, CharSequence cSeq2) {
        // really only need to create one of these; other can just it and add
        ObjectToCounterMap<String> tf1 = termFrequencyVector(cSeq1);
        ObjectToCounterMap<String> tf2 = termFrequencyVector(cSeq2);
        double len1 = 0.0;
        double len2 = 0.0;
        double prod = 0.0;
        for (Map.Entry<String,Counter> entry : tf1.entrySet()) {
            String term = entry.getKey();
            Counter count1 = entry.getValue();
            double tfIdf1 = tfIdf(term,count1);
            len1 += tfIdf1 * tfIdf1;
            Counter count2 = tf2.remove(term);
            if (count2 == null) continue;
            double tfIdf2 = tfIdf(term,count2);
            len2 += tfIdf2 * tfIdf2;
            prod += tfIdf1 * tfIdf2;
        }
        // increment length for terms in cSeq2 but not in cSeq1
        for (Map.Entry<String,Counter> entry : tf2.entrySet()) {
            String term = entry.getKey();
            Counter count2 = entry.getValue();
            double tfIdf2 = tfIdf(term,count2);
            len2 += tfIdf2 * tfIdf2;
        }
        if (len1 == 0)
            return len2 == 0.0 ? 1.0 : 0.0;
        if (len2 == 0) return 0.0;
        double prox = prod / Math.sqrt(len1 * len2);
        return prox < 0.0
            ? 0.0
            : (prox > 1.0
               ? 1.0
               : prox);
    }


    /**
     * Returns the number of training documents that contained
     * the specified term.
     *
     * @param term Term to test.
     * @return The number of training documents that contained the
     * specified term.
     */
    public int docFrequency(String term) {
        return mDocFrequency.getCount(term);
    }

    /**
     * Return the inverse-document frequency for the specified
     * term.  See the class doducmentation above for a formal
     * definition.
     *
     * @param term The term whose IDF is returned.
     * @return The IDF of the specified term.
     */
    public double idf(String term) {
        int df = mDocFrequency.getCount(term);
        if (df == 0) return 0.0;
        return java.lang.Math.log(((double) mDocCount)/((double) df));
    }

    /**
     * Returns the total number of training documents.
     *
     * @return The total number of training documents.
     */
    public int numDocuments() {
        return mDocCount;
    }

    /**
     * Returns the number of terms that have been seen
     * during training.
     *
     * @return The number of terms for this distance.
     */
    public int numTerms() {
        return mDocFrequency.size();
    }

    /**
     * Returns the set of known terms for this distance.  The set will
     * contain every token that was derived from a training instance.
     * Only terms in the returned term set will contribute to
     * similarity.  All other terms have an inverse-document frequency
     * of zero, so will not be matched, though they will contribute
     * to length during the cosine calculation.
     *
     * @return The set of terms for this distance measure.
     */
    public Set<String> termSet() {
        return Collections.<String>unmodifiableSet(mDocFrequency.keySet());
    }
    
    public double tfIdf(String term, Counter count) {
        double idf = idf(term);
        double tf = count.doubleValue();
        return Math.sqrt(tf * idf);
    }

    
}