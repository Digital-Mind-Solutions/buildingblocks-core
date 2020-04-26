package org.digitalmind.buildingblocks.core.dtobase;

import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

@SuperBuilder
@AllArgsConstructor
public class PageWrapperDTO<T> implements Page<T> {
    private Page<T> value;

    @Override
    public Pageable getPageable() {
        return value.getPageable();
    }

    @Override
    public int getTotalPages() {
        return value.getTotalPages();
    }

    @Override
    public long getTotalElements() {
        return value.getTotalElements();
    }

    @Override
    public int getNumber() {
        return value.getNumber();
    }

    @Override
    public int getSize() {
        return value.getSize();
    }

    @Override
    public int getNumberOfElements() {
        return value.getNumberOfElements();
    }

    @Override
    public List<T> getContent() {
        return value.getContent();
    }

    @Override
    public boolean hasContent() {
        return value.hasContent();
    }

    @Override
    public Sort getSort() {
        return value.getSort();
    }

    @Override
    public boolean isFirst() {
        return value.isFirst();
    }

    @Override
    public boolean isLast() {
        return value.isLast();
    }

    @Override
    public boolean hasNext() {
        return value.hasNext();
    }

    @Override
    public boolean hasPrevious() {
        return value.hasPrevious();
    }

    @Override
    public Pageable nextPageable() {
        return value.nextPageable();
    }

    @Override
    public Pageable previousPageable() {
        return value.previousPageable();
    }

    @Override
    public <U> Page<U> map(Function<? super T, ? extends U> converter) {
        return value.map(converter);
    }

    @Override
    public Iterator<T> iterator() {
        return value.iterator();
    }

}
