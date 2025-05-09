package net.earelin.tasklist.mongo;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

@Configuration
public class MongoConfig {

  private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME;

  @Bean
  public MongoCustomConversions mongoCustomConversions() {
    return new MongoCustomConversions(Arrays.asList(
        new ZonedDateTimeToStringConverter(),
        new StringToZonedDateTimeConverter()
    ));
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
