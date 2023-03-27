package com.buchu.greenfarm.util;

public class PageRequest {
    private int page = 1;

    public void setPage(String page) {
        try {
            this.page = Integer.parseInt(page);
        } catch (NumberFormatException e) {
            this.page = 1;
        }
    }

    public int getPage() {
        return page;
    }

    public org.springframework.data.domain.PageRequest of() {
        return org.springframework.data.domain.PageRequest.of(
                this.page - 1, UtilConst.DEFAULT_PAGE_SIZE);
    }
}
