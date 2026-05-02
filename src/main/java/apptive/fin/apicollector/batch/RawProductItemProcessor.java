package apptive.fin.apicollector.batch;

import apptive.fin.apicollector.raw.ProductRaw;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;

@Component
@RequiredArgsConstructor
public class RawProductItemProcessor<T> implements ItemProcessor<ProductRaw, T> {

    @Override
    public T process(ProductRaw item) throws Exception {
//        JsonNode json =
    }

}
