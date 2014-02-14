package informationextractor;

import edu.stanford.nlp.ling.CoreAnnotation;
import java.util.Collections;
import java.util.*;

import edu.stanford.nlp.pipeline.Annotator;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import static edu.stanford.nlp.pipeline.Annotator.TOKENIZE_AND_SSPLIT;

public class DictionaryAnnotator implements Annotator, CoreAnnotation<String> {
    public static final String ANNOTATOR_CLASS = "dictionary";

    public static final String ONTOLOGY_DICTIONARY = ANNOTATOR_CLASS;
    public static final Annotator.Requirement DICTIONARY_REQUIREMENT = new Annotator.Requirement(ONTOLOGY_DICTIONARY);

    private Properties props;

    public DictionaryAnnotator(String annotatorClass, Properties props) {
        this.props = props;
    }

    @Override
    public void annotate(Annotation annotation) {
        List<CoreLabel> tokens = annotation.get(TokensAnnotation.class);
        HashMap dictionary  = RDFParser.getOntologyDictionary();
        for (CoreLabel token : tokens) {
            String token_word = token.word();
            String dictEntry = null;
            if (dictionary.containsKey(token_word)){
                dictEntry = dictionary.get(token_word).toString();
            }
            token.set(DictionaryAnnotator.class, dictEntry);
        }
    }

    @Override
    public Set<Annotator.Requirement> requirementsSatisfied() {
        return Collections.singleton(DICTIONARY_REQUIREMENT);
    }
    
    @Override
    public Set<Annotator.Requirement> requires() {
        return TOKENIZE_AND_SSPLIT;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public Class<String> getType() {
        return String.class;
    }
}
