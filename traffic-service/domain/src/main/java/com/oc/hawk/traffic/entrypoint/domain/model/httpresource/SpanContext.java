package com.oc.hawk.traffic.entrypoint.domain.model.httpresource;

import lombok.Getter;

import java.util.Objects;

@Getter
public class SpanContext {

    private final String spanId;

    private final String parentSpanId;

    private final String traceId;
    private final Kind kind;

    public SpanContext(String spanId, String parentSpanId, String traceId, String kind) {
        this.spanId = spanId;
        this.parentSpanId = parentSpanId;
        this.traceId = traceId;
        this.kind = Kind.of(kind);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SpanContext that = (SpanContext) o;
        return Objects.equals(spanId, that.spanId) &&
                Objects.equals(traceId, that.traceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(spanId, traceId);
    }
}
