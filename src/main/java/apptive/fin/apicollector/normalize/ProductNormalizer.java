package apptive.fin.apicollector.normalize;

import apptive.fin.apicollector.Source;
import apptive.fin.apicollector.raw.ProductRaw;

public interface ProductNormalizer {

    Source source();

    ProductDraft normalize(ProductRaw rawProduct);
}
