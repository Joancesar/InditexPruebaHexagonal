package com.inditex.zarachallenge.infrastructure.messaging;

import com.inditex.zarachallenge.application.model.event.ProductAvailabilityEvent;
import com.inditex.zarachallenge.domain.Product;
import com.inditex.zarachallenge.domain.Size;
import com.inditex.zarachallenge.domain.enu.SizeType;
import com.inditex.zarachallenge.infrastructure.config.ZaraInitialConfig;
import com.inditex.zarachallenge.infrastructure.persistence.SizeRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.util.ResourceUtils;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Import({TestChannelBinderConfiguration.class})
class KafkaListenerIT {

    @Autowired
    private InputDestination inputDestination;

    @Autowired
    private KafkaListener kafkaListener;

    @Autowired
    private SizeRepository sizeRepository;

    @MockBean
    private ZaraInitialConfig zaraInitialConfig;

    private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    @Value("classpath:events.csv")
    Resource resourceFile;

    @Autowired
    DataSource dataSource;
    @AfterAll
    void clear() throws SQLException {
        ScriptUtils.executeSqlScript(dataSource.getConnection(), new ClassPathResource("clear.sql"));
    }
    @Test
    void whenMessageIsReceivedThenSizeIsUpdated() {
        Size size = new Size();
        size.setSizeId(12358L);
        size.setSizeType(SizeType.M);
        size.setAvailability(false);
        size.setLastUpdated(Timestamp.valueOf("2022-11-25 10:30:15"));
        Product product = new Product();
        product.setId(1L);
        size.setProduct(product);

        sizeRepository.save(size);
        Timestamp updatedTime = Timestamp.from(Instant.now());
        Message<ProductAvailabilityEvent> message =
                MessageBuilder.withPayload(
                        ProductAvailabilityEvent.builder()
                                .sizeId(12358L)
                                .availability(true)
                                .update(updatedTime)
                                .build()
                ).build();

        inputDestination.send(message);

        Optional<Size> actualSize = sizeRepository.findById(12358L);

        assertTrue(actualSize.isPresent());
        assertTrue(actualSize.get().isAvailability());
        assertEquals(format.format(updatedTime), format.format(actualSize.get().getLastUpdated()));
    }

    @Test
    void whenMessagesAreReceivedThenUpdateEntities() throws IOException {
        List<ProductAvailabilityEvent> stocks = Files
            .readAllLines(ResourceUtils.getFile(resourceFile.getURL()).toPath()).stream()
            .map(line -> convertStock(Arrays.asList(line.trim().split(",")))).toList();
        stocks.forEach(stock -> {
            inputDestination.send(new GenericMessage<>(stock));

            Optional<Size> actualSize = sizeRepository.findById(stock.getSizeId());

            actualSize.ifPresent((size) -> {
                assertEquals(stock.isAvailability(), actualSize.get().isAvailability());
                assertEquals(format.format(stock.getUpdate()), format.format(actualSize.get().getLastUpdated()));
            });
        });
    }

    public ProductAvailabilityEvent convertStock(List<String> stock) {
        return ProductAvailabilityEvent.builder().sizeId(Long.parseLong(stock.get(0)))
                .availability(Boolean.parseBoolean(stock.get(1)))
                .update(Timestamp.valueOf(
                    LocalDateTime.parse(stock.get(2), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX"))))
                .build();
    }
}
