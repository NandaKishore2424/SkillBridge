package com.college.skillbridge.dto;

import lombok.Data;
import org.springframework.data.domain.Page;
import java.util.List;

@Data
public class PaginatedResponse<T> {
    private List<T> content;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
    
    public PaginatedResponse(Page<T> page) {
        this.content = page.getContent();
        this.pageNo = page.getNumber();
        this.pageSize = page.getSize();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.last = page.isLast();
    }
}