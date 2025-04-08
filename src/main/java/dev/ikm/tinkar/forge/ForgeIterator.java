package dev.ikm.tinkar.forge;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class ForgeIterator<T> implements Iterator<T> {

    private final AtomicInteger iteratorIndex;
    private final Iterator<T> entityIterator;
    private final Consumer<Integer> progressUpdate;

    public ForgeIterator(Stream<T> entityStream, Consumer<Integer> progressUpdate) {
        this.entityIterator = entityStream.iterator();
        this.iteratorIndex = new AtomicInteger(0);
        this.progressUpdate = progressUpdate;
    }

    @Override
    public boolean hasNext() {
        return entityIterator.hasNext();
    }

    @Override
    public T next() {
        int index = iteratorIndex.incrementAndGet();
        progressUpdate.accept(index);
        return entityIterator.next();
    }
}
