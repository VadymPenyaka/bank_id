package peniaka.bankid.util;

import graphql.language.StringValue;
import graphql.schema.*;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.UUID;

@Component
public class CustomScalars {

    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer() {
        return wiringBuilder -> wiringBuilder
                .scalar(uuidScalarType())
                .scalar(localDateScalarType());
    }

    private GraphQLScalarType uuidScalarType() {
        return GraphQLScalarType.newScalar()
                .name("UUID")
                .description("UUID scalar type")
                .coercing(new Coercing<UUID, String>() {
                    @Override
                    public String serialize(Object dataFetcherResult) throws CoercingSerializeException {
                        if (dataFetcherResult instanceof UUID) {
                            return dataFetcherResult.toString();
                        } else if (dataFetcherResult instanceof String) {
                            try {
                                UUID.fromString((String) dataFetcherResult);
                                return (String) dataFetcherResult;
                            } catch (IllegalArgumentException e) {
                                throw new CoercingSerializeException("Invalid UUID format: " + dataFetcherResult);
                            }
                        }
                        throw new CoercingSerializeException("Expected a UUID object or UUID string");
                    }

                    @Override
                    public UUID parseValue(Object input) throws CoercingParseValueException {
                        try {
                            if (input instanceof String) {
                                return UUID.fromString((String) input);
                            }
                            if (input instanceof UUID) {
                                return (UUID) input;
                            }
                            throw new CoercingParseValueException("Expected a String or UUID");
                        } catch (IllegalArgumentException e) {
                            throw new CoercingParseValueException("Invalid UUID format: " + input);
                        }
                    }

                    @Override
                    public UUID parseLiteral(Object input) throws CoercingParseLiteralException {
                        if (input instanceof StringValue) {
                            try {
                                return UUID.fromString(((StringValue) input).getValue());
                            } catch (IllegalArgumentException e) {
                                throw new CoercingParseLiteralException("Invalid UUID format: " + input);
                            }
                        }
                        throw new CoercingParseLiteralException("Expected a StringValue");
                    }
                })
                .build();
    }

    private GraphQLScalarType localDateScalarType() {
        return GraphQLScalarType.newScalar()
                .name("Local")
                .description("Java LocalDate scalar type")
                .coercing(new Coercing<LocalDate, String>() {
                    @Override
                    public String serialize(Object dataFetcherResult) throws CoercingSerializeException {
                        if (dataFetcherResult instanceof LocalDate) {
                            return ((LocalDate) dataFetcherResult).format(DateTimeFormatter.ISO_LOCAL_DATE);
                        } else if (dataFetcherResult instanceof String) {
                            try {
                                LocalDate.parse((String) dataFetcherResult, DateTimeFormatter.ISO_LOCAL_DATE);
                                return (String) dataFetcherResult;
                            } catch (DateTimeParseException e) {
                                throw new CoercingSerializeException("Invalid LocalDate format: " + dataFetcherResult);
                            }
                        }
                        throw new CoercingSerializeException("Expected a LocalDate object or ISO date string (YYYY-MM-DD)");
                    }

                    @Override
                    public LocalDate parseValue(Object input) throws CoercingParseValueException {
                        try {
                            if (input instanceof String) {
                                return LocalDate.parse((String) input, DateTimeFormatter.ISO_LOCAL_DATE);
                            }
                            if (input instanceof LocalDate) {
                                return (LocalDate) input;
                            }
                            throw new CoercingParseValueException("Expected a String or LocalDate");
                        } catch (DateTimeParseException e) {
                            throw new CoercingParseValueException("Invalid LocalDate format: " + input + ". Expected format YYYY-MM-DD");
                        }
                    }

                    @Override
                    public LocalDate parseLiteral(Object input) throws CoercingParseLiteralException {
                        if (input instanceof StringValue) {
                            try {
                                return LocalDate.parse(((StringValue) input).getValue(), DateTimeFormatter.ISO_LOCAL_DATE);
                            } catch (DateTimeParseException e) {
                                throw new CoercingParseLiteralException("Invalid LocalDate format: " + input + ". Expected format YYYY-MM-DD");
                            }
                        }
                        throw new CoercingParseLiteralException("Expected a StringValue");
                    }
                })
                .build();
    }
}