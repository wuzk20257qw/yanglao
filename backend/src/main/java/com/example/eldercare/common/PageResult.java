package com.example.eldercare.common;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@NoArgsConstructor
public class PageResult<T> {
    private List<T> records;
    private Long total;
    private Integer size;
    private Integer current;
    private Integer pages;

    public PageResult(List<T> records, Long total, Integer size, Integer current) {
        this.records = records;
        this.total = total;
        this.size = size;
        this.current = current;
        this.pages = (int) Math.ceil((double) total / size);
    }

    public static <T> PageResult<T> of(List<T> records, Long total, Integer size, Integer current) {
        return new PageResult<>(records, total, size, current);
    }

    public static <T> PageResult<T> of(Page<T> page) {
        return new PageResult<>(
            page.getContent(),
            page.getTotalElements(),
            page.getSize(),
            page.getNumber()
        );
    }
}
