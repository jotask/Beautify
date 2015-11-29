package uk.ac.aber.beautify.custom;

import uk.ac.aber.beautify.core.BeautifyFilters;
import uk.ac.aber.beautify.filters.Filter;

/**
 * Created by Jose Vives on 14/11/2015.
 *
 * @author Jose Vives.
 * @since 14/11/2015
 */
public class JoseFilter implements BeautifyFilters {

    @Override
    public Filter autoBeautify() {
        return new Jose();
    }

    @Override
    public String getAuthor() {
        return "Jose Vives";
    }
}