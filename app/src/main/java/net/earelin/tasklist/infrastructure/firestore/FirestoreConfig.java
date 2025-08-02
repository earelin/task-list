package net.earelin.tasklist.infrastructure.firestore;

import com.google.cloud.spring.data.firestore.mapping.FirestoreClassMapper;
import com.google.cloud.spring.data.firestore.mapping.FirestoreDefaultClassMapper;
import com.google.cloud.spring.data.firestore.mapping.FirestoreMappingContext;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.CustomConversions;

@Configuration
public class FirestoreConfig {

  private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME;

  @Bean
  public CustomConversions firestoreCustomConversions() {
    return new CustomConversions(CustomConversions.StoreConversions.NONE, Arrays.asList(
        new ZonedDateTimeToStringConverter(),
        new StringToZonedDateTimeConverter()
    ));
  }

  @Bean
  public FirestoreClassMapper firestoreClassMapper(FirestoreMappingContext mappingContext) {
    return new FirestoreDefaultClassMapper(mappingContext);
  }

  private static class ZonedDateTimeToStringConverter implements Converter<ZonedDateTime, String> {
    @Override
    public String convert(ZonedDateTime source) {
      return source.format(formatter);
    }
  }

  private static class StringToZonedDateTimeConverter implements Converter<String, ZonedDateTime> {
    @Override
    public ZonedDateTime convert(String source) {
      return ZonedDateTime.parse(source, formatter);
    }
  }
}
