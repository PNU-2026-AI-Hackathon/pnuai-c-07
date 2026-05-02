package apptive.fin.apicollector.tasklet;

import apptive.fin.apicollector.config.CollectorProperties;
import apptive.fin.apicollector.raw.ProductRawRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.StepContribution;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeactivateMissingProductTasklet implements Tasklet {

    private final ProductRawRepository productRawRepository;
    private final CollectorProperties properties;

    @Override
    public RepeatStatus execute(
            StepContribution contribution,
            ChunkContext chunkContext
    ) {
        log.info(
                "DeactivateMissingProductTasklet skipped. source={}, rawCount={}",
                properties.source(),
                productRawRepository.count()
        );
        return RepeatStatus.FINISHED;
    }
}
